 /*
  * Copyright (c) 2019-2020 The X-Cash Foundation
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
 package com.xcash.wallet.aidl.service;

 import android.app.Notification;
 import android.app.NotificationChannel;
 import android.app.NotificationManager;
 import android.app.PendingIntent;
 import android.app.Service;
 import android.content.Context;
 import android.content.Intent;
 import android.os.Handler;
 import android.os.IBinder;
 import android.os.RemoteCallbackList;
 import android.os.RemoteException;

 import com.my.monero.model.PendingTransaction;
 import com.my.monero.model.TransactionHistory;
 import com.my.monero.model.WalletManager;
 import com.xcash.base.utils.LogTool;
 import com.xcash.base.utils.TimeTool;
 import com.xcash.models.local.DelayVote;
 import com.xcash.utils.LanguageTool;
 import com.xcash.utils.database.AppDatabase;
 import com.xcash.utils.database.entity.Node;
 import com.xcash.utils.database.entity.TransactionInfo;
 import com.xcash.utils.database.entity.Wallet;
 import com.xcash.wallet.MainActivity;
 import com.xcash.wallet.R;
 import com.xcash.wallet.aidl.OnCreateTransactionListener;
 import com.xcash.wallet.aidl.OnNormalListener;
 import com.xcash.wallet.aidl.OnWalletDataListener;
 import com.xcash.wallet.aidl.OnWalletRefreshListener;
 import com.xcash.wallet.aidl.Transaction;
 import com.xcash.wallet.aidl.WalletOperateManager;
 import com.xcash.wallet.aidl.manager.XManager;
 import com.xcash.wallet.aidl.manager.XWalletController;

 import java.io.File;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.concurrent.PriorityBlockingQueue;

 public class WalletService extends Service {

     private final String CHANNEL_ID = "channel_1";
     private final int OPERATETYPE_DESTROY = -1;
     private final int OPERATETYPE_RUN = 1;
     private final int OPERATETYPE_STOP = 2;
     private final String TAG_LOADREFRESHWALLET = "loadRefreshWallet";
     private final String DPOPS_SUCCESS = "Success";
     private final int maxQueueSize = 10;
     private final PriorityBlockingQueue<WalletOperate> priorityBlockingQueueWalletOperate = new PriorityBlockingQueue<WalletOperate>();
     private final RemoteCallbackList<OnWalletRefreshListener> onWalletRefreshListenerList = new RemoteCallbackList<>();
     private final HashMap<String, WalletOperate> waitingWalletOperateHashMap = new HashMap<String, WalletOperate>();
     private final List<DelayVote> delayVotes = new ArrayList<DelayVote>();//need synchronized
     private boolean canRunWalletThread = true;
     private boolean walletThreadStatusRunning = false;
     private Thread walletThread;
     private boolean canRunCycleThread = true;
     private boolean cycleThreadStatusRunning = false;
     private Thread cycleThread;
     private Handler handler = new Handler();
     private MyBinder myBinder = new MyBinder();
     private int operateType = OPERATETYPE_STOP;
     private int runningWalletId = -1;
     private Context context;
     private String language;

     @Override
     protected void attachBaseContext(Context context) {
         language = LanguageTool.getSelectLanguage(context);
         WalletService.this.context = LanguageTool.initAppLanguage(context, language);
         super.attachBaseContext(WalletService.this.context);
     }

     @Override
     public void onCreate() {
         super.onCreate();
         loadWalletThread();
         loadCycleThread();
         startForeground();
     }

     @Override
     public int onStartCommand(Intent intent, int flags, int startId) {
         return super.onStartCommand(intent, flags, startId);
     }

     private void loadWalletThread() {
         if (walletThreadStatusRunning) {
             return;
         }
         walletThreadStatusRunning = true;
         operateType = OPERATETYPE_RUN;
         walletThread = new Thread(new WalletThreadRunnable());
         walletThread.start();
     }

     private void loadCycleThread() {
         if (cycleThreadStatusRunning) {
             return;
         }
         cycleThreadStatusRunning = true;
         cycleThread = new Thread(new CycleThreadRunnable());
         cycleThread.start();
     }

     private void startForeground() {
         Intent intent = new Intent(this, MainActivity.class);
         intent.setAction(Intent.ACTION_MAIN);
         intent.addCategory(Intent.CATEGORY_LAUNCHER);
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
         PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
         String title = getString(R.string.app_name);
         String content = getString(R.string.server_run_tips);
         Notification notification;
         if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
             NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
             notificationManager.createNotificationChannel(notificationChannel);
             notification = new Notification.Builder(this, CHANNEL_ID)
                     .setSmallIcon(R.mipmap.ic_launcher)
                     .setContentTitle(title)
                     .setContentText(content)
                     .setContentIntent(pendingIntent)
                     .build();
         } else {
             notification = new Notification.Builder(this)
                     .setSmallIcon(R.mipmap.ic_launcher)
                     .setContentTitle(title)
                     .setContentText(content)
                     .setContentIntent(pendingIntent)
                     .build();
         }
         notification.flags = Notification.FLAG_ONGOING_EVENT;
         notification.flags |= Notification.FLAG_NO_CLEAR;
         notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
         startForeground(1, notification);
     }

     @Override
     public IBinder onBind(Intent intent) {
         return myBinder;
     }

     @Override
     public void onDestroy() {
         super.onDestroy();
         if (handler != null) {
             handler.removeCallbacksAndMessages(null);
         }
         operateType = OPERATETYPE_DESTROY;
         canRunCycleThread = false;
     }

     private void callBack(WalletInfo walletInfo) {
         if (walletInfo == null) {
             return;
         }
         int num = onWalletRefreshListenerList.beginBroadcast();
         for (int i = 0; i < num; i++) {
             try {
                 OnWalletRefreshListener onWalletRefreshListener = onWalletRefreshListenerList.getBroadcastItem(i);
                 switch (walletInfo.getType()) {
                     case WalletInfo.TYPE_QUEUE_FULL_ERROR:
                         onWalletRefreshListener.queueFullError(walletInfo.getError());
                         break;
                     case WalletInfo.TYPE_BEGIN_LOAD_WALLET:
                         onWalletRefreshListener.beginLoadWallet(walletInfo.getWalletId());
                         break;
                     case WalletInfo.TYPE_SYNCHRONIZE_STATUS_ERROR:
                         onWalletRefreshListener.synchronizeStatusError(walletInfo.getWalletId(), walletInfo.getError());
                         break;
                     case WalletInfo.TYPE_SYNCHRONIZE_STATUS_SUCCESS:
                         onWalletRefreshListener.synchronizeStatusSuccess(walletInfo.getWalletId());
                         break;
                     case WalletInfo.TYPE_REFRESH_BALANCE:
                         onWalletRefreshListener.refreshBalance(walletInfo.getWalletId(), walletInfo.getBalance(), walletInfo.getUnlockedBalance());
                         break;
                     case WalletInfo.TYPE_BLOCK_PROGRESS:
                         onWalletRefreshListener.blockProgress(walletInfo.getWalletId(), walletInfo.isResult(), walletInfo.getBlockChainHeight(), walletInfo.getDaemonHeight(), walletInfo.getProgress());
                         break;
                     case WalletInfo.TYPE_REFRESH_TRANSACTION:
                         onWalletRefreshListener.refreshTransaction(walletInfo.getWalletId());
                         break;
                     case WalletInfo.TYPE_CLOSE_ACTIVE_WALLET:
                         onWalletRefreshListener.closeActiveWallet(walletInfo.getWalletId());
                         break;
                     case WalletInfo.TYPE_MONEY_SPENT:
                         onWalletRefreshListener.moneySpent(walletInfo.getWalletId(), walletInfo.getTxId(), walletInfo.getAmount(), walletInfo.isFullSynchronizeOnce());
                         break;
                     case WalletInfo.TYPE_MONEY_RECEIVE:
                         onWalletRefreshListener.moneyReceive(walletInfo.getWalletId(), walletInfo.getTxId(), walletInfo.getAmount(), walletInfo.isFullSynchronizeOnce());
                         break;
                     case WalletInfo.TYPE_UNCONFIRMED_MONEY_RECEIVE:
                         onWalletRefreshListener.unconfirmedMoneyReceive(walletInfo.getWalletId(), walletInfo.getTxId(), walletInfo.getAmount(), walletInfo.isFullSynchronizeOnce());
                         break;
                     default:
                         break;
                 }
             } catch (RemoteException e) {
                 e.printStackTrace();
             }
         }
         onWalletRefreshListenerList.finishBroadcast();
     }

     private void putWaitingWalletOperate(String key, WalletOperate walletOperate) {
         if (key == null || walletOperate == null) {
             return;
         }
         synchronized (waitingWalletOperateHashMap) {
             waitingWalletOperateHashMap.put(key, walletOperate);
         }
     }

     private void removeWaitingWalletOperate(String key) {
         if (key == null) {
             return;
         }
         synchronized (waitingWalletOperateHashMap) {
             waitingWalletOperateHashMap.remove(key);
         }
     }

     private void cancelWaitingWalletOperate(String tag) {
         if (tag == null) {
             return;
         }
         synchronized (waitingWalletOperateHashMap) {
             Iterator<Map.Entry<String, WalletOperate>> iterator = waitingWalletOperateHashMap.entrySet().iterator();
             List<WalletOperate> walletOperateList = new ArrayList<>();
             while (iterator.hasNext()) {
                 Map.Entry<String, WalletOperate> entry = iterator.next();
                 WalletOperate walletOperate = entry.getValue();
                 String theTag = walletOperate.getTag();
                 if (theTag != null && tag.equals(theTag)) {
                     walletOperateList.add(walletOperate);
                 }
             }
             for (int i = 0; i < walletOperateList.size(); i++) {
                 WalletOperate walletOperate = walletOperateList.get(i);
                 walletOperate.setCancel(true);
             }
         }
     }

     private void addToPriorityBlockingQueue(WalletOperate walletOperate) {
         try {
             int size = priorityBlockingQueueWalletOperate.size();
             if (size >= maxQueueSize) {
                 WalletInfo walletInfo = new WalletInfo();
                 walletInfo.setType(WalletInfo.TYPE_QUEUE_FULL_ERROR);
                 walletInfo.setError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.over_request_tips));
                 WalletService.this.callBack(walletInfo);
                 return;
             }
             priorityBlockingQueueWalletOperate.add(walletOperate);
             putWaitingWalletOperate(walletOperate.getKey(), walletOperate);
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     private void addWalletOperateSetDaemon(int type, com.xcash.wallet.aidl.OnNormalListener onNormalListener, Node node) {
         WalletOperate walletOperate = new WalletOperate();
         walletOperate.setKey(String.valueOf(TimeTool.getOnlyTimeWithoutSleep()));
         walletOperate.setType(type);
         walletOperate.setOnNormalListener(onNormalListener);
         walletOperate.setPriority(1);
         walletOperate.setNode(node);
         addToPriorityBlockingQueue(walletOperate);
     }

     private void addWalletOperateCheckWalletPassword(int type, com.xcash.wallet.aidl.OnNormalListener onNormalListener, String name, String password) {
         WalletOperate walletOperate = new WalletOperate();
         walletOperate.setKey(String.valueOf(TimeTool.getOnlyTimeWithoutSleep()));
         walletOperate.setType(type);
         walletOperate.setOnNormalListener(onNormalListener);
         walletOperate.setPriority(3);
         walletOperate.setName(name);
         walletOperate.setPassword(password);
         addToPriorityBlockingQueue(walletOperate);
     }

     private void addWalletOperate(int type, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener, int id, String name, String password, String passwordPrompt, String mnemonic, String addressKey, String privateViewKey, String privateSpendKey, long restoreHeight, boolean needReset, Node node) {
         WalletOperate walletOperate = new WalletOperate();
         walletOperate.setKey(String.valueOf(TimeTool.getOnlyTimeWithoutSleep()));
         walletOperate.setType(type);
         walletOperate.setOnWalletDataListener(onWalletDataListener);
         boolean ignore = false;
         switch (type) {
             case WalletOperate.TYPE_CREATE_WALLET:
                 walletOperate.setPriority(2);
                 walletOperate.setName(name);
                 walletOperate.setPassword(password);
                 walletOperate.setPasswordPrompt(passwordPrompt);
                 break;
             case WalletOperate.TYPE_IMPORT_WALLET_MNEMONIC:
                 walletOperate.setPriority(2);
                 walletOperate.setName(name);
                 walletOperate.setPassword(password);
                 walletOperate.setPasswordPrompt(passwordPrompt);
                 walletOperate.setMnemonic(mnemonic);
                 walletOperate.setRestoreHeight(restoreHeight);
                 break;
             case WalletOperate.TYPE_IMPORT_WALLET_KEYS:
                 walletOperate.setPriority(2);
                 walletOperate.setName(name);
                 walletOperate.setPassword(password);
                 walletOperate.setPasswordPrompt(passwordPrompt);
                 walletOperate.setAddressKey(addressKey);
                 walletOperate.setPrivateViewKey(privateViewKey);
                 walletOperate.setPrivateSpendKey(privateSpendKey);
                 walletOperate.setRestoreHeight(restoreHeight);
                 break;
             case WalletOperate.TYPE_LOAD_REFRESH_WALLET:
                 if (runningWalletId != id || needReset) {
                     cancelWaitingWalletOperate(TAG_LOADREFRESHWALLET);
                     walletOperate.setTag(TAG_LOADREFRESHWALLET);
                     walletOperate.setPriority(1);
                     walletOperate.setId(id);
                     walletOperate.setName(name);
                     walletOperate.setPassword(password);
                     walletOperate.setRestoreHeight(restoreHeight);
                 } else {
                     ignore = true;
                 }
                 break;
             case WalletOperate.TYPE_GET_WALLET_DATA:
                 walletOperate.setPriority(1);
                 walletOperate.setId(id);
                 walletOperate.setName(name);
                 walletOperate.setPassword(password);
                 break;
             default:
                 break;
         }
         if (!ignore) {
             addToPriorityBlockingQueue(walletOperate);
         }
     }

     private void addWalletOperateCreateTransaction(int type, com.xcash.wallet.aidl.OnCreateTransactionListener onCreateTransactionListener, String walletAddress, String amount, String ringSize, String paymentId, String description, int priority, boolean publicTransaction) {
         WalletOperate walletOperate = new WalletOperate();
         walletOperate.setKey(String.valueOf(TimeTool.getOnlyTimeWithoutSleep()));
         walletOperate.setType(type);
         walletOperate.setOnCreateTransactionListener(onCreateTransactionListener);
         walletOperate.setPriority(1);
         walletOperate.setWalletAddress(walletAddress);
         walletOperate.setAmount(amount);
         walletOperate.setRingSize(ringSize);
         walletOperate.setPaymentId(paymentId);
         walletOperate.setDescription(description);
         walletOperate.setPendingPriority(PendingTransaction.Priority.fromInteger(priority));
         walletOperate.setPublicTransaction(publicTransaction);
         addToPriorityBlockingQueue(walletOperate);
     }

     private void addWalletOperateNormal(int type, com.xcash.wallet.aidl.OnNormalListener onNormalListener) {
         WalletOperate walletOperate = new WalletOperate();
         walletOperate.setKey(String.valueOf(TimeTool.getOnlyTimeWithoutSleep()));
         walletOperate.setType(type);
         walletOperate.setOnNormalListener(onNormalListener);
         walletOperate.setPriority(1);
         addToPriorityBlockingQueue(walletOperate);
     }

     private void addWalletOperateCloseWallet(int type, com.xcash.wallet.aidl.OnNormalListener onNormalListener, int id) {
         WalletOperate walletOperate = new WalletOperate();
         walletOperate.setKey(String.valueOf(TimeTool.getOnlyTimeWithoutSleep()));
         walletOperate.setType(type);
         walletOperate.setOnNormalListener(onNormalListener);
         walletOperate.setPriority(1);
         walletOperate.setId(id);
         addToPriorityBlockingQueue(walletOperate);
     }

     private void addWalletOperateDelegate(int type, com.xcash.wallet.aidl.OnNormalListener onNormalListener, String item, String value, String delegate_name, String delegate_IP_address, String block_verifier_messages_public_key) {
         WalletOperate walletOperate = new WalletOperate();
         walletOperate.setKey(String.valueOf(TimeTool.getOnlyTimeWithoutSleep()));
         walletOperate.setType(type);
         walletOperate.setOnNormalListener(onNormalListener);
         walletOperate.setPriority(1);
         walletOperate.setItem(item);
         walletOperate.setValue(value);
         walletOperate.setDelegate_name(delegate_name);
         walletOperate.setDelegate_IP_address(delegate_IP_address);
         walletOperate.setBlock_verifier_messages_public_key(block_verifier_messages_public_key);
         addToPriorityBlockingQueue(walletOperate);
     }

     private void addDelayVote(long voteTimestamp, String value, OnNormalListener onNormalListener) {
         DelayVote delayVote = new DelayVote();
         delayVote.setVoteTimestamp(voteTimestamp);
         delayVote.setValue(value);
         delayVote.setOnNormalListener(onNormalListener);
         synchronized (delayVotes) {
             delayVotes.add(delayVote);
         }
     }

     class MyBinder extends WalletOperateManager.Stub {

         @Override
         public void setDaemon(String url, String username, String password, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws RemoteException {
             Node node = new Node();
             node.setUrl(url);
             node.setUsername(username);
             node.setPassword(password);
             addWalletOperateSetDaemon(WalletOperate.TYPE_SET_DAEMON_WALLET, onNormalListener, node);
         }

         @Override
         public void createWallet(String name, String password, String passwordPrompt, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws RemoteException {
             addWalletOperate(WalletOperate.TYPE_CREATE_WALLET, onWalletDataListener, 0, name, password, passwordPrompt, null, null, null, null, 0, false, null);
         }

         @Override
         public void importWalletMnemonic(String name, String password, String passwordPrompt, String mnemonic, long restoreHeight, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws RemoteException {
             addWalletOperate(WalletOperate.TYPE_IMPORT_WALLET_MNEMONIC, onWalletDataListener, 0, name, password, passwordPrompt, mnemonic, null, null, null, restoreHeight, false, null);
         }

         @Override
         public void importWalletKeys(String name, String password, String passwordPrompt, String addressKey, String privateViewKey, String privateSpendKey, long restoreHeight, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws RemoteException {
             addWalletOperate(WalletOperate.TYPE_IMPORT_WALLET_KEYS, onWalletDataListener, 0, name, password, passwordPrompt, null, addressKey, privateViewKey, privateSpendKey, restoreHeight, false, null);
         }

         @Override
         public void checkWalletPassword(String name, String password, OnNormalListener onNormalListener) throws RemoteException {
             addWalletOperateCheckWalletPassword(WalletOperate.TYPE_CHECK_WALLET_PASSWORD, onNormalListener, name, password);
         }

         @Override
         public void loadRefreshWallet(int id, String name, String password, long restoreHeight, boolean needReset, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws RemoteException {
             addWalletOperate(WalletOperate.TYPE_LOAD_REFRESH_WALLET, onWalletDataListener, id, name, password, null, null, null, null, null, restoreHeight, needReset, null);
         }

         @Override
         public void createTransaction(String walletAddress, String amount, String ringSize, String paymentId, String description, int priority, boolean publicTransaction, OnCreateTransactionListener onCreateTransactionListener) throws RemoteException {
             addWalletOperateCreateTransaction(WalletOperate.TYPE_CREATE_TRANSACTION, onCreateTransactionListener, walletAddress, amount, ringSize, paymentId, description, priority, publicTransaction);
         }

         @Override
         public void sendTransaction(OnNormalListener onNormalListener) throws RemoteException {
             addWalletOperateNormal(WalletOperate.TYPE_SEND_TRANSACTION, onNormalListener);
         }

         @Override
         public void closeActiveWallet(OnNormalListener onNormalListener) throws RemoteException {
             addWalletOperateNormal(WalletOperate.TYPE_CLOSE_ACTIVE_WALLET, onNormalListener);
         }

         @Override
         public void closeWallet(int id, OnNormalListener onNormalListener) throws RemoteException {
             addWalletOperateCloseWallet(WalletOperate.TYPE_CLOSE_WALLET, onNormalListener, id);
         }

         @Override
         public void getWalletData(int id, String name, String password, OnWalletDataListener onWalletDataListener) throws RemoteException {
             addWalletOperate(WalletOperate.TYPE_GET_WALLET_DATA, onWalletDataListener, id, name, password, null, null, null, null, null, 0, false, null);
         }

         @Override
         public void vote(String value, OnNormalListener onNormalListener) throws RemoteException {
             addWalletOperateDelegate(WalletOperate.TYPE_VOTE, onNormalListener, null, value, null, null, null);
         }

         @Override
         public void delayVote(String value, long voteTimestamp, OnNormalListener onNormalListener) throws RemoteException {
             addDelayVote(voteTimestamp, value, onNormalListener);
         }

         @Override
         public void delegateRegister(String delegate_name, String delegate_IP_address, String block_verifier_messages_public_key, OnNormalListener onNormalListener) throws RemoteException {
             addWalletOperateDelegate(WalletOperate.TYPE_DELEGATE_REGISTER, onNormalListener, null, null, delegate_name, delegate_IP_address, block_verifier_messages_public_key);
         }

         @Override
         public void delegateUpdate(String item, String value, OnNormalListener onNormalListener) throws RemoteException {
             addWalletOperateDelegate(WalletOperate.TYPE_DELEGATE_UPDATE, onNormalListener, item, value, null, null, null);
         }

         @Override
         public void delegateRemove(OnNormalListener onNormalListener) throws RemoteException {
             addWalletOperateDelegate(WalletOperate.TYPE_DELEGATE_REMOVE, onNormalListener, null, null, null, null, null);
         }

         @Override
         public void runService() throws RemoteException {
             if (operateType == OPERATETYPE_DESTROY) {//the onDestroy() on the same thread
                 return;
             }
             operateType = OPERATETYPE_RUN;
         }

         @Override
         public void stopService() throws RemoteException {
             if (operateType == OPERATETYPE_DESTROY) {
                 return;
             }
             operateType = OPERATETYPE_STOP;
         }

         @Override
         public void changeLanguage(String language) throws RemoteException {
             WalletService.this.language = language;
         }

         @Override
         public void registerListener(OnWalletRefreshListener onWalletRefreshListener) throws RemoteException {
             onWalletRefreshListenerList.register(onWalletRefreshListener);
         }

         @Override
         public void unRegisterListener(OnWalletRefreshListener onWalletRefreshListener) throws RemoteException {
             onWalletRefreshListenerList.unregister(onWalletRefreshListener);
         }

     }

     class CycleThreadRunnable implements Runnable {

         public void run() {
             try {
                 while (canRunCycleThread) {
                     try {
                         checkCycleTimestamp();
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                     try {
                         Thread.sleep(1000);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
             } finally {
                 cycleThreadStatusRunning = false;
             }
         }

         /**
          * Running in thread
          */
         private void checkCycleTimestamp() {
             synchronized (delayVotes) {
                 long timestamp = System.currentTimeMillis();
                 List<DelayVote> overTimeDelayVotes = new ArrayList<>();
                 for (int i = 0; i < delayVotes.size(); i++) {
                     DelayVote delayVote = delayVotes.get(i);
                     if (timestamp >= delayVote.getVoteTimestamp()) {
                         overTimeDelayVotes.add(delayVote);
                     }
                 }
                 for (int i = 0; i < overTimeDelayVotes.size(); i++) {
                     DelayVote delayVote = overTimeDelayVotes.get(i);
                     addWalletOperateDelegate(WalletOperate.TYPE_VOTE, delayVote.getOnNormalListener(), null, delayVote.getValue(), null, null, null);
                     delayVotes.remove(delayVote);
                 }
             }
         }
     }

     class WalletThreadRunnable implements Runnable {

         public void run() {
             try {
                 while (canRunWalletThread) {
                     try {
                         boolean needExit = doOperate();
                         if (needExit) {
                             return;
                         }
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                     try {
                         Thread.sleep(100);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
             } finally {
                 walletThreadStatusRunning = false;
             }
         }

         /**
          * Running in thread
          */
         private boolean doOperate() {
             boolean needExit = false;
             try {
                 switch (WalletService.this.operateType) {
                     case OPERATETYPE_DESTROY:
                         XManager.getInstance().getXWalletController().closeWallet();
                         closeRunningWallet();
                         needExit = true;
                         break;
                     case OPERATETYPE_STOP:

                         break;
                     case OPERATETYPE_RUN:
                         WalletOperate walletOperate = priorityBlockingQueueWalletOperate.poll();//don‘t use take(),because needed do OPERATETYPE_DESTROY...
                         if (walletOperate != null) {
                             if (!walletOperate.isCancel()) {
                                 operateRun(walletOperate);
                             }
                             removeWaitingWalletOperate(walletOperate.getKey());
                         }
                         break;
                     default:
                         break;
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
             return needExit;
         }

         /**
          * Running in thread
          */
         private void operateRun(final WalletOperate walletOperate) {
             try {
                 switch (walletOperate.getType()) {
                     case WalletOperate.TYPE_CREATE_WALLET:
                         createWallet(walletOperate);
                         break;
                     case WalletOperate.TYPE_IMPORT_WALLET_MNEMONIC:
                         importWalletMnemonic(walletOperate);
                         break;
                     case WalletOperate.TYPE_IMPORT_WALLET_KEYS:
                         importWalletKeys(walletOperate);
                         break;
                     case WalletOperate.TYPE_CHECK_WALLET_PASSWORD:
                         checkWalletPassword(walletOperate);
                         break;
                     case WalletOperate.TYPE_LOAD_REFRESH_WALLET:
                         loadRefreshWallet(walletOperate);
                         break;
                     case WalletOperate.TYPE_SET_DAEMON_WALLET:
                         setDaemon(walletOperate);
                         break;
                     case WalletOperate.TYPE_CREATE_TRANSACTION:
                         createTransaction(walletOperate);
                         break;
                     case WalletOperate.TYPE_SEND_TRANSACTION:
                         sendTransaction(walletOperate);
                         break;
                     case WalletOperate.TYPE_CLOSE_ACTIVE_WALLET:
                         closeActiveWallet(walletOperate);
                         break;
                     case WalletOperate.TYPE_CLOSE_WALLET:
                         closeWallet(walletOperate);
                         break;
                     case WalletOperate.TYPE_GET_WALLET_DATA:
                         getWalletData(walletOperate);
                         break;
                     case WalletOperate.TYPE_VOTE:
                         vote(walletOperate);
                         break;
                     case WalletOperate.TYPE_DELEGATE_REGISTER:
                         register(walletOperate);
                         break;
                     case WalletOperate.TYPE_DELEGATE_UPDATE:
                         update(walletOperate);
                         break;
                     case WalletOperate.TYPE_DELEGATE_REMOVE:
                         remove(walletOperate);
                         break;
                     default:
                         break;

                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }

         /**
          * Running in thread
          */
         private void createWallet(final WalletOperate walletOperate) throws Exception {
             OnWalletDataListener onWalletDataListener = walletOperate.getOnWalletDataListener();
             if (onWalletDataListener == null) {
                 return;
             }
             boolean result = false;
             Wallet dbWallet = null;
             String error = null;
             try {
                 dbWallet = XManager.getInstance().createWallet(walletOperate.getName(), walletOperate.getPassword());
                 error = dbWallet.getErrorString();
                 if (dbWallet != null && error == null) {
                     dbWallet.setPasswordPrompt(walletOperate.getPasswordPrompt());
                     XManager.getInstance().saveWallet(dbWallet);
                     result = true;
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
             try {
                 if (result) {
                     onWalletDataListener.onSuccess(convertByDBWallet(dbWallet));
                 } else {
                     onWalletDataListener.onError(error);
                 }
             } catch (RemoteException e) {
                 e.printStackTrace();
             }
         }

         /**
          * Running in thread
          */
         private void importWalletMnemonic(final WalletOperate walletOperate) throws Exception {
             OnWalletDataListener onWalletDataListener = walletOperate.getOnWalletDataListener();
             if (onWalletDataListener == null) {
                 return;
             }
             boolean result = false;
             Wallet dbWallet = null;
             String error = null;
             try {
                 dbWallet = XManager.getInstance().recoveryWallet(walletOperate.getName(), walletOperate.getPassword(), walletOperate.getMnemonic(), walletOperate.getRestoreHeight());
                 error = dbWallet.getErrorString();
                 if (dbWallet != null && error == null) {
                     dbWallet.setPasswordPrompt(walletOperate.getPasswordPrompt());
                     XManager.getInstance().saveWallet(dbWallet);
                     result = true;
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
             try {
                 if (result) {
                     onWalletDataListener.onSuccess(convertByDBWallet(dbWallet));
                 } else {
                     onWalletDataListener.onError(error);
                 }
             } catch (RemoteException e) {
                 e.printStackTrace();
             }
         }

         /**
          * Running in thread
          */
         private void importWalletKeys(final WalletOperate walletOperate) throws Exception {
             OnWalletDataListener onWalletDataListener = walletOperate.getOnWalletDataListener();
             if (onWalletDataListener == null) {
                 return;
             }
             boolean result = false;
             Wallet dbWallet = null;
             String error = null;
             try {
                 dbWallet = XManager.getInstance().createWalletWithKeys(walletOperate.getName(), walletOperate.getPassword(), walletOperate.getAddressKey(), walletOperate.getPrivateViewKey(), walletOperate.getPrivateSpendKey(), walletOperate.getRestoreHeight());
                 error = dbWallet.getErrorString();
                 if (dbWallet != null && error == null) {
                     dbWallet.setPasswordPrompt(walletOperate.getPasswordPrompt());
                     XManager.getInstance().saveWallet(dbWallet);
                     result = true;
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
             try {
                 if (result) {
                     onWalletDataListener.onSuccess(convertByDBWallet(dbWallet));
                 } else {
                     onWalletDataListener.onError(error);
                 }
             } catch (RemoteException e) {
                 e.printStackTrace();
             }
         }

         /**
          * Running in thread
          */
         private void checkWalletPassword(final WalletOperate walletOperate) throws Exception {
             OnNormalListener onNormalListener = walletOperate.getOnNormalListener();
             if (onNormalListener == null) {
                 return;
             }
             boolean result = false;
             try {
                 result = XManager.getInstance().verifyWalletPasswordOnly(walletOperate.getName(), walletOperate.getPassword());
             } catch (Exception e) {
                 e.printStackTrace();
             }
             try {
                 if (result) {
                     onNormalListener.onSuccess("");
                 } else {
                     onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.check_password_error_tips));
                 }
             } catch (RemoteException e) {
                 e.printStackTrace();
             }
         }

         /**
          * Running in thread
          */
         private void loadRefreshWallet(final WalletOperate walletOperate) throws Exception {
             final int walletId = walletOperate.getId();
             runningWalletId = walletId;
             boolean result = false;
             try {
                 final com.my.monero.model.Wallet openWallet = XManager.getInstance().openWallet(walletOperate.getName(), walletOperate.getPassword(), walletId);
                 beginLoadWallet(walletId);
                 resultWalletData(openWallet, walletOperate.getOnWalletDataListener());
                 if (openWallet != null) {
                     XManager.getInstance().insertNodes();
                     Node node = AppDatabase.getInstance().nodeDao().loadActiveNodeBySymbol(XManager.SYMBOL);
                     result = XManager.getInstance().startWallet(openWallet, node, walletOperate.getRestoreHeight(), new XWalletController.OnWalletListener() {
                         @Override
                         public void onWalletStarted() {

                         }

                         @Override
                         public void onWalletStartFailed(final String error) {

                         }

                         @Override
                         public void onRefreshed(long height) {
                             refresh(walletId, openWallet, walletOperate);
                         }

                         @Override
                         public void onMoneySpent(String txId, long amount) {
                             moneySpent(txId, amount, walletOperate);
                         }

                         @Override
                         public void onMoneyReceived(String txId, long amount) {
                             moneyReceive(txId, amount, walletOperate);
                         }

                         @Override
                         public void unconfirmedMoneyReceived(String txId, long amount) {
                             unconfirmedMoneyReceive(txId, amount, walletOperate);
                         }
                     });
                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 runningWalletId = -2;
             }
             resultStatus(walletId, result);
         }

         /**
          * Running in thread
          */
         private void setDaemon(final WalletOperate walletOperate) throws Exception {
             OnNormalListener onNormalListener = walletOperate.getOnNormalListener();
             if (onNormalListener == null) {
                 return;
             }
             try {
                 Node node = walletOperate.getNode();
                 if (node != null) {
                     WalletManager.getInstance().setDaemonAddress(node.getUrl());
                 }
                 onNormalListener.onSuccess("");
             } catch (Exception e) {
                 e.printStackTrace();
                 onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.set_daemon_error_tips));
             }
         }

         /**
          * Running in thread
          */
         private void createTransaction(final WalletOperate walletOperate) throws Exception {
             OnCreateTransactionListener onCreateTransactionListener = walletOperate.getOnCreateTransactionListener();
             if (onCreateTransactionListener == null) {
                 return;
             }
             try {
                 String walletAddress = walletOperate.getWalletAddress();
                 String amount = walletOperate.getAmount();
                 String ringSize = walletOperate.getRingSize();
                 String paymentId = walletOperate.getPaymentId();
                 String description = walletOperate.getDescription();
                 PendingTransaction.Priority pendingPriority = walletOperate.getPendingPriority();
                 boolean publicTransaction = walletOperate.isPublicTransaction();

                 XWalletController xWalletController = XManager.getInstance().getXWalletController();
                 PendingTransaction pendingTransaction = xWalletController.createTransaction(walletAddress, amount, ringSize, paymentId, description, publicTransaction, pendingPriority);
                 if (pendingTransaction != null) {
                     Transaction transaction = new Transaction();
                     transaction.setAmount(pendingTransaction.getAmount());
                     transaction.setDust(pendingTransaction.getDust());
                     transaction.setFee(pendingTransaction.getFee());
                     transaction.setFirstTxId(pendingTransaction.getFirstTxId());
                     transaction.setTxCount(pendingTransaction.getTxCount());
                     onCreateTransactionListener.onSuccess(transaction);
                 } else {
                     onCreateTransactionListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.create_transaction_error_tips));
                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 onCreateTransactionListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.create_transaction_error_tips));
             }
         }

         /**
          * Running in thread
          */
         private void sendTransaction(final WalletOperate walletOperate) throws Exception {
             OnNormalListener onNormalListener = walletOperate.getOnNormalListener();
             if (onNormalListener == null) {
                 return;
             }
             try {
                 XWalletController xWalletController = XManager.getInstance().getXWalletController();
                 String result = xWalletController.sendTransaction();
                 if (result == null || result.equals("")) {
                     onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.send_transaction_error_tips));
                 } else {
                     onNormalListener.onSuccess(result);
                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.send_transaction_error_tips));
             }
         }

         /**
          * Running in thread
          */
         private void closeActiveWallet(final WalletOperate walletOperate) throws Exception {
             OnNormalListener onNormalListener = walletOperate.getOnNormalListener();
             if (onNormalListener == null) {
                 return;
             }
             try {
                 com.my.monero.model.Wallet activeWallet = XManager.getInstance().getXWalletController().getActiveWallet();
                 if (activeWallet == null) {
                     onNormalListener.onSuccess(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.not_find_active_wallet_tips));
                 } else {
                     XManager.getInstance().getXWalletController().closeWallet();
                     closeRunningWallet();
                     onNormalListener.onSuccess("");
                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.close_active_wallet_error_tips));
             }
         }

         /**
          * Running in thread
          */
         private void closeWallet(final WalletOperate walletOperate) throws Exception {
             OnNormalListener onNormalListener = walletOperate.getOnNormalListener();
             if (onNormalListener == null) {
                 return;
             }
             try {
                 if (walletOperate.getId() == XManager.getInstance().getXWalletController().getActiveWalletId()) {
                     com.my.monero.model.Wallet activeWallet = XManager.getInstance().getXWalletController().getActiveWallet();
                     if (activeWallet == null) {
                         onNormalListener.onSuccess(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.not_find_active_wallet_tips));
                     } else {
                         XManager.getInstance().getXWalletController().closeWallet();
                         closeRunningWallet();
                         onNormalListener.onSuccess("");
                     }
                 } else {
                     onNormalListener.onSuccess(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.not_find_active_wallet_tips));
                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.close_active_wallet_error_tips));
             }
         }

         /**
          * Running in thread
          */
         private void getWalletData(final WalletOperate walletOperate) throws Exception {
             OnWalletDataListener onWalletDataListener = walletOperate.getOnWalletDataListener();
             if (onWalletDataListener == null) {
                 return;
             }
             try {
                 com.my.monero.model.Wallet activeWallet = XManager.getInstance().getXWalletController().getActiveWallet();
                 if (activeWallet != null && walletOperate.getId() == XManager.getInstance().getXWalletController().getActiveWalletId()) {
                     com.xcash.wallet.aidl.Wallet wallet = convertWallet(activeWallet);
                     onWalletDataListener.onSuccess(wallet);
                 } else {
                     File file = XManager.getInstance().getWalletData(walletOperate.getName());
                     if (file.exists()) {
                         com.my.monero.model.Wallet openWallet = WalletManager.getInstance().openWallet(file.getPath(), walletOperate.getPassword());
                         if (openWallet == null || openWallet.getStatus() != com.my.monero.model.Wallet.Status.Status_Ok) {
                             String error = null;
                             if (openWallet != null) {
                                 error = openWallet.getErrorString();
                                 openWallet.close();
                             }
                             onWalletDataListener.onError(error);
                         } else {
                             com.xcash.wallet.aidl.Wallet wallet = convertWallet(openWallet);
                             openWallet.close();
                             onWalletDataListener.onSuccess(wallet);
                         }
                     } else {
                         onWalletDataListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.get_wallet_data_error_tips));
                     }
                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 onWalletDataListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.get_wallet_data_error_tips));
             }
         }

         /**
          * Running in thread
          */
         private void vote(final WalletOperate walletOperate) throws Exception {
             OnNormalListener onNormalListener = walletOperate.getOnNormalListener();
             if (onNormalListener == null) {
                 return;
             }
             try {
                 com.my.monero.model.Wallet activeWallet = XManager.getInstance().getXWalletController().getActiveWallet();
                 if (activeWallet != null && walletOperate.getValue() != null) {
                     String result = activeWallet.vote(walletOperate.getValue());
                     if (result != null && result.equals(DPOPS_SUCCESS)) {
                         onNormalListener.onSuccess(result);
                     } else {
                         onNormalListener.onError(result);
                     }
                 } else {
                     onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.delegate_vote_error_tips));
                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.delegate_vote_error_tips));
             }
         }

         /**
          * Running in thread
          */
         private void register(final WalletOperate walletOperate) throws Exception {
             OnNormalListener onNormalListener = walletOperate.getOnNormalListener();
             if (onNormalListener == null) {
                 return;
             }
             try {
                 com.my.monero.model.Wallet activeWallet = XManager.getInstance().getXWalletController().getActiveWallet();
                 if (activeWallet != null && walletOperate.getDelegate_name() != null && walletOperate.getDelegate_IP_address() != null && walletOperate.getBlock_verifier_messages_public_key() != null) {
                     String result = activeWallet.delegateRegister(walletOperate.getDelegate_name(), walletOperate.getDelegate_IP_address(), walletOperate.getBlock_verifier_messages_public_key());
                     if (result != null && result.equals(DPOPS_SUCCESS)) {
                         onNormalListener.onSuccess(result);
                     } else {
                         onNormalListener.onError(result);
                     }
                 } else {
                     onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.delegate_register_error_tips));
                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.delegate_register_error_tips));
             }
         }

         /**
          * Running in thread
          */
         private void update(final WalletOperate walletOperate) throws Exception {
             OnNormalListener onNormalListener = walletOperate.getOnNormalListener();
             if (onNormalListener == null) {
                 return;
             }
             try {
                 com.my.monero.model.Wallet activeWallet = XManager.getInstance().getXWalletController().getActiveWallet();
                 if (activeWallet != null && walletOperate.getItem() != null && walletOperate.getValue() != null) {
                     String result = activeWallet.delegateUpdate(walletOperate.getItem(), walletOperate.getValue());
                     if (result != null && result.equals(DPOPS_SUCCESS)) {
                         onNormalListener.onSuccess(result);
                     } else {
                         onNormalListener.onError(result);
                     }
                 } else {
                     onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.delegate_update_error_tips));
                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.delegate_update_error_tips));
             }
         }

         /**
          * Running in thread
          */
         private void remove(final WalletOperate walletOperate) throws Exception {
             OnNormalListener onNormalListener = walletOperate.getOnNormalListener();
             if (onNormalListener == null) {
                 return;
             }
             try {
                 com.my.monero.model.Wallet activeWallet = XManager.getInstance().getXWalletController().getActiveWallet();
                 if (activeWallet != null) {
                     String result = "Registered delegate does not support remove.";
                     if (result != null && result.equals(DPOPS_SUCCESS)) {
                         onNormalListener.onSuccess(result);
                     } else {
                         onNormalListener.onError(result);
                     }
                 } else {
                     onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.delegate_remove_error_tips));
                 }
             } catch (Exception e) {
                 e.printStackTrace();
                 onNormalListener.onError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.delegate_remove_error_tips));
             }
         }

         /**
          * Running in thread
          */
         private void closeRunningWallet() {
             if (runningWalletId == -1) {
                 return;
             }
             WalletInfo walletInfo = new WalletInfo();
             walletInfo.setWalletId(runningWalletId);
             walletInfo.setType(WalletInfo.TYPE_CLOSE_ACTIVE_WALLET);
             callBack(walletInfo);
             runningWalletId = -1;
         }

         /**
          * Running in thread
          */
         private void beginLoadWallet(int walletId) {
             WalletInfo walletInfo = new WalletInfo();
             walletInfo.setWalletId(walletId);
             walletInfo.setType(WalletInfo.TYPE_BEGIN_LOAD_WALLET);
             callBack(walletInfo);
         }

         /**
          * Running in thread
          */
         private void resultStatus(int walletId, boolean result) {
             WalletInfo walletInfo = new WalletInfo();
             walletInfo.setWalletId(walletId);
             if (!result) {
                 runningWalletId = -2;
                 walletInfo.setType(WalletInfo.TYPE_SYNCHRONIZE_STATUS_ERROR);
                 walletInfo.setError(LanguageTool.getLocaleStringResource(WalletService.this.context, language, R.string.activity_wallet_running_SynchronizedError_tips));
             } else {
                 walletInfo.setType(WalletInfo.TYPE_SYNCHRONIZE_STATUS_SUCCESS);
             }
             callBack(walletInfo);
         }

         /**
          * Running in thread
          */
         private void resultWalletData(com.my.monero.model.Wallet openWallet, OnWalletDataListener onWalletDataListener) {
             if (onWalletDataListener == null) {
                 return;
             }
             try {
                 if (openWallet == null || openWallet.getStatus() != com.my.monero.model.Wallet.Status.Status_Ok) {
                     String error = null;
                     if (openWallet != null) {
                         error = openWallet.getErrorString();
                     }
                     onWalletDataListener.onError(error);
                 } else {
                     com.xcash.wallet.aidl.Wallet wallet = convertWallet(openWallet);
                     onWalletDataListener.onSuccess(wallet);
                 }
             } catch (RemoteException e) {
                 e.printStackTrace();
             }
         }

         /**
          * Running in thread
          */
         private com.xcash.wallet.aidl.Wallet convertWallet(com.my.monero.model.Wallet openWallet) {
             com.xcash.wallet.aidl.Wallet wallet = new com.xcash.wallet.aidl.Wallet();
             wallet.setSymbol(XManager.SYMBOL);
             wallet.setName(openWallet.getName());
             wallet.setAddress(openWallet.getAddress());
             wallet.setBalance(openWallet.getDisplayAmount(openWallet.getBalance()));
             wallet.setRestoreHeight(openWallet.getRestoreHeight());
             wallet.setSeed(openWallet.getSeed());
             wallet.setSecretViewKey(openWallet.getSecretViewKey());
             wallet.setSecretSpendKey(openWallet.getSecretSpendKey());
             wallet.setPublicViewKey(openWallet.getPublicViewKey());
             wallet.setPublicSpendKey(openWallet.getPublicSpendKey());
             return wallet;
         }

         /**
          * Running in thread
          */
         private com.xcash.wallet.aidl.Wallet convertByDBWallet(Wallet dbWallet) {
             com.xcash.wallet.aidl.Wallet wallet = new com.xcash.wallet.aidl.Wallet();
             wallet.setSymbol(XManager.SYMBOL);
             wallet.setId(dbWallet.getId());
             wallet.setName(dbWallet.getName());
             wallet.setAddress(dbWallet.getAddress());
             wallet.setBalance(dbWallet.getBalance());
             wallet.setRestoreHeight(dbWallet.getRestoreHeight());
             wallet.setSeed(dbWallet.getSeed());
             wallet.setSecretViewKey(dbWallet.getSecretViewKey());
             wallet.setSecretSpendKey(dbWallet.getSecretSpendKey());
             return wallet;
         }

         /**
          * Running in thread
          */
         private void refresh(int walletId, final com.my.monero.model.Wallet wallet, WalletOperate walletOperate) {
             try {
                 if (wallet == null || wallet.getStatus() != com.my.monero.model.Wallet.Status.Status_Ok) {
                     resultStatus(walletId, false);
                     return;
                 }
                 long blockChainHeight = wallet.getBlockChainHeight();
                 long daemonBlockChainHeight = wallet.getDaemonBlockChainHeight();
                 if (walletOperate.getMaxDaemonBlockChainHeight() > daemonBlockChainHeight || daemonBlockChainHeight == 0) {//when get a network error,while happen
                     resultStatus(walletId, false);
                     return;
                 }
                 walletOperate.setMaxDaemonBlockChainHeight(daemonBlockChainHeight);
                 WalletInfo walletInfo = new WalletInfo();
                 walletInfo.setWalletId(walletId);
                 walletInfo.setType(WalletInfo.TYPE_BLOCK_PROGRESS);
                 walletInfo.setBlockChainHeight(blockChainHeight);
                 walletInfo.setDaemonHeight(daemonBlockChainHeight);
                 int progress = 0;
                 if (blockChainHeight == daemonBlockChainHeight) {
                     progress = 100;
                 } else {
                     progress = (int) (100f * blockChainHeight / daemonBlockChainHeight);
                 }
                 walletOperate.setRestoreHeight(blockChainHeight);
                 LogTool.e(XManager.TAG, "blockChainHeight:" + blockChainHeight + ";" + "daemonHeight:" + daemonBlockChainHeight + ";" + "progress:" + progress);
                 walletInfo.setProgress(progress);
                 if (progress < 100) {
                     walletInfo.setResult(false);
                 } else {
                     walletOperate.setFullSynchronizeOnce(true);
                     walletInfo.setResult(true);
                     updateBalance(walletId, wallet);
                     updateHistory(walletId, wallet, walletOperate, blockChainHeight);
                 }
                 callBack(walletInfo);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }

         /**
          * Running in thread
          */
         private void updateBalance(int walletId, com.my.monero.model.Wallet wallet) {
             if (wallet == null) {
                 return;
             }
             final String balance = wallet.getDisplayAmount(wallet.getBalance());
             final String unlockedBalance = wallet.getDisplayAmount(wallet.getUnlockedBalance());

             Wallet theWallet = AppDatabase.getInstance().walletDao().loadWalletById(walletId);
             if (theWallet != null) {
                 theWallet.setBalance(balance);
                 AppDatabase.getInstance().walletDao().updateWallets(theWallet);
             }
             WalletInfo walletInfo = new WalletInfo();
             walletInfo.setWalletId(walletId);
             walletInfo.setType(WalletInfo.TYPE_REFRESH_BALANCE);
             walletInfo.setBalance(balance);
             walletInfo.setUnlockedBalance(unlockedBalance);
             callBack(walletInfo);
         }

         /**
          * Running in thread
          */
         private void updateHistory(int walletId, com.my.monero.model.Wallet wallet, WalletOperate walletOperate, long blockChainHeight) {
             if (wallet == null) {
                 return;
             }
             TransactionHistory transactionHistory = wallet.getHistory();
             if (transactionHistory == null) {
                 return;
             }
             transactionHistory.refresh();
             List<TransactionInfo> transactionInfos = getTransactionHistory(walletId, transactionHistory);
             if (transactionInfos == null) {
                 return;
             }
             if (transactionInfos.size() != walletOperate.getTransactionInfoSize() || (walletOperate.isHavePendingTransaction() && walletOperate.getUpdateTransactionHeight() != blockChainHeight)) {
                 walletOperate.setTransactionInfoSize(transactionInfos.size());
                 walletOperate.setHavePendingTransaction(havePendingTransaction(transactionInfos));
                 walletOperate.setUpdateTransactionHeight(blockChainHeight);

                 TransactionInfo[] transactionInfosArray = transactionInfos.toArray(new TransactionInfo[]{});
                 List<TransactionInfo> theTransactionInfos = AppDatabase.getInstance().transactionInfoDao().loadTransactionInfosByWalletId(XManager.SYMBOL, walletId);
                 if (theTransactionInfos != null) {
                     AppDatabase.getInstance().transactionInfoDao().deleteTransactionInfo(theTransactionInfos.toArray(new TransactionInfo[]{}));
                 }
                 AppDatabase.getInstance().transactionInfoDao().insertTransactionInfos(transactionInfosArray);
                 WalletInfo walletInfo = new WalletInfo();
                 walletInfo.setWalletId(walletId);
                 walletInfo.setType(WalletInfo.TYPE_REFRESH_TRANSACTION);
                 callBack(walletInfo);
             }
         }

         /**
          * Running in thread
          */
         private List<TransactionInfo> getTransactionHistory(int walletId, TransactionHistory transactionHistory) {
             List<TransactionInfo> transactionInfos = new ArrayList<>();
             if (transactionHistory == null) {
                 return transactionInfos;
             }
             List<com.my.monero.model.TransactionInfo> theTransactionInfos = transactionHistory.getAll();
             if (theTransactionInfos == null) {
                 return transactionInfos;
             }
             Collections.sort(theTransactionInfos);
             for (int i = 0; i < theTransactionInfos.size(); i++) {
                 com.my.monero.model.TransactionInfo theTransactionInfo = theTransactionInfos.get(i);
                 TransactionInfo transactionInfo = new TransactionInfo();
                 transactionInfo.setSymbol(XManager.SYMBOL);
                 transactionInfo.setWalletId(walletId);
                 transactionInfo.setDirection(theTransactionInfo.direction.getValue());
                 transactionInfo.setPending(theTransactionInfo.isPending);
                 transactionInfo.setFailed(theTransactionInfo.isFailed);
                 transactionInfo.setAmount(com.my.monero.model.Wallet.getDisplayAmount(theTransactionInfo.amount));
                 transactionInfo.setFee(com.my.monero.model.Wallet.getDisplayAmount(theTransactionInfo.fee));
                 transactionInfo.setBlockHeight(theTransactionInfo.blockheight);
                 transactionInfo.setConfirmations(theTransactionInfo.confirmations);
                 transactionInfo.setHash(theTransactionInfo.hash);
                 transactionInfo.setTimestamp(theTransactionInfo.timestamp * 1000);
                 transactionInfo.setPaymentId(theTransactionInfo.paymentId);
                 transactionInfo.setTxKey(theTransactionInfo.txKey);
                 transactionInfo.setAddress(theTransactionInfo.address);
                 transactionInfos.add(transactionInfo);
             }
             return transactionInfos;
         }

         /**
          * Running in thread
          */
         private boolean havePendingTransaction(List<TransactionInfo> transactionInfos) {
             if (transactionInfos == null) {
                 return false;
             }
             for (int i = 0; i < transactionInfos.size(); i++) {
                 TransactionInfo transactionInfo = transactionInfos.get(i);
                 if (transactionInfo.isPending() || transactionInfo.getConfirmations() < XManager.TRANSACTION_MIN_CONFIRMATION) {
                     return true;
                 }
             }
             return false;
         }

         /**
          * Running in thread
          */
         private void moneySpent(final String txId, final long amount, WalletOperate walletOperate) {
             if (txId == null) {
                 return;
             }
             WalletInfo walletInfo = new WalletInfo();
             walletInfo.setWalletId(walletOperate.getId());
             walletInfo.setType(WalletInfo.TYPE_MONEY_SPENT);
             walletInfo.setTxId(txId);
             walletInfo.setAmount(amount);
             walletInfo.setFullSynchronizeOnce(walletOperate.isFullSynchronizeOnce());
             callBack(walletInfo);
         }

         /**
          * Running in thread
          */
         private void moneyReceive(final String txId, final long amount, WalletOperate walletOperate) {
             if (txId == null) {
                 return;
             }
             WalletInfo walletInfo = new WalletInfo();
             walletInfo.setWalletId(walletOperate.getId());
             walletInfo.setType(WalletInfo.TYPE_MONEY_RECEIVE);
             walletInfo.setTxId(txId);
             walletInfo.setAmount(amount);
             walletInfo.setFullSynchronizeOnce(walletOperate.isFullSynchronizeOnce());
             callBack(walletInfo);
         }

         /**
          * Running in thread
          */
         private void unconfirmedMoneyReceive(final String txId, final long amount, WalletOperate walletOperate) {
             if (txId == null) {
                 return;
             }
             WalletInfo walletInfo = new WalletInfo();
             walletInfo.setWalletId(walletOperate.getId());
             walletInfo.setType(WalletInfo.TYPE_UNCONFIRMED_MONEY_RECEIVE);
             walletInfo.setTxId(txId);
             walletInfo.setAmount(amount);
             walletInfo.setFullSynchronizeOnce(walletOperate.isFullSynchronizeOnce());
             callBack(walletInfo);
         }

     }

 }