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
package com.xcash.utils;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;

import com.xcash.base.BaseActivity;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.wallet.MainActivity;
import com.xcash.wallet.R;
import com.xcash.wallet.TheApplication;
import com.xcash.wallet.WalletRunningActivity;
import com.xcash.wallet.aidl.OnNormalListener;
import com.xcash.wallet.aidl.OnWalletDataListener;
import com.xcash.wallet.aidl.OnWalletRefreshListener;
import com.xcash.wallet.aidl.WalletOperateManager;
import com.xcash.wallet.aidl.manager.XManager;
import com.xcash.wallet.aidl.service.WalletService;
import com.xcash.wallet.uihelp.ProgressDialogHelp;

public class WalletServiceHelper {

    private Handler handler = new Handler(Looper.getMainLooper());
    private WalletOperateManager walletOperateManager;
    private OnWalletRefreshListener onWalletRefreshListener = new MyOnWalletRefreshListener();
    private Context context;
    private NotificationHelper notificationHelper;
    private int notificationId = 1;
    private CacheHelper cacheHelper = new CacheHelper<String>(2000);
    private Wallet wallet;
    private String password;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        /**
         * Running in Main thread
         */
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            walletOperateManager = WalletOperateManager.Stub.asInterface(iBinder);
            try {
                walletOperateManager.registerListener(onWalletRefreshListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /**
         * Running in Main thread
         */
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //BaseActivity.showLongToast(context,context.getString(R.string.retry_bind_service_tips));
                    TheApplication.cancelAllDialogFromActivityManager();
                    bindService();
                    resetOpenWallet(null);
                }
            }, 500);
        }

    };

    public WalletServiceHelper(Context context) {
        this.context = context.getApplicationContext();
        notificationHelper = new NotificationHelper(context, context.getString(R.string.app_name), context.getString(R.string.app_name));
    }

    private void loadRefreshWallet(Wallet wallet, String set_wallet_password, boolean needReset, final OnOpenWalletListener onOpenWalletListener) {
        if (wallet == null || set_wallet_password == null) {
            return;
        }
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        try {
            walletOperateManager.loadRefreshWallet(wallet.getId(), wallet.getName(), set_wallet_password, wallet.getRestoreHeight(), needReset, new OnWalletDataListener.Stub() {
                @Override
                public void onSuccess(final com.xcash.wallet.aidl.Wallet wallet) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (onOpenWalletListener != null) {
                                onOpenWalletListener.onSuccess(wallet);
                            }
                        }
                    });
                }

                @Override
                public void onError(final String error) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (onOpenWalletListener != null) {
                                onOpenWalletListener.onError(error);
                            }
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void bindService() {
        Intent intent = new Intent(context, WalletService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService() {
        if (walletOperateManager != null && walletOperateManager.asBinder().isBinderAlive()) {
            try {
                walletOperateManager.unRegisterListener(onWalletRefreshListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            walletOperateManager = null;
        }
        context.unbindService(serviceConnection);
    }

    public WalletOperateManager getWalletOperateManager() {
        if (walletOperateManager == null) {
            BaseActivity.showLongToast(TheApplication.getTheApplication(), TheApplication.getTheApplication().getString(R.string.service_uninitialized_tips));
        }
        return walletOperateManager;
    }

    public void resetOpenWallet(OnOpenWalletListener onOpenWalletListener) {
        openWallet(wallet, password, true, onOpenWalletListener);
    }

    public void openWallet(final Wallet wallet, final String password, final boolean needReset, final OnOpenWalletListener onOpenWalletListener) {
        if (wallet == null || password == null) {
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadRefreshWallet(wallet, password, needReset, onOpenWalletListener);
            }
        }, 500);
        this.wallet = wallet;
        this.password = password;
    }

    public void closeWallet(int walletId) {
        if (wallet != null && wallet.getId() == walletId) {
            wallet = null;
            password = null;
        }
    }

    public void closeActiveWallet() {
        wallet = null;
        password = null;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public String getPassword() {
        return password;
    }

    public static void verifyWalletPasswordOnly(final BaseActivity baseActivity, String name, String password, final OnVerifyWalletPasswordListener onVerifyWalletPasswordListener) {
        if (baseActivity.handler == null || name == null || password == null || onVerifyWalletPasswordListener == null) {
            return;
        }
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        Object[] objects = ProgressDialogHelp.unEnabledView(baseActivity, null);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        try {
            walletOperateManager.checkWalletPassword(name, password, new OnNormalListener.Stub() {
                @Override
                public void onSuccess(final String tips) throws RemoteException {
                    baseActivity.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onVerifyWalletPasswordListener.onSuccess(tips);
                            ProgressDialogHelp.enabledView(baseActivity, progressDialog, progressDialogKey, null);

                        }
                    });
                }

                @Override
                public void onError(final String error) throws RemoteException {
                    baseActivity.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onVerifyWalletPasswordListener.onError(error);
                            ProgressDialogHelp.enabledView(baseActivity, progressDialog, progressDialogKey, null);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            onVerifyWalletPasswordListener.onError(baseActivity.getString(R.string.wallet_service_not_running_tips));
            ProgressDialogHelp.enabledView(baseActivity, progressDialog, progressDialogKey, null);
        }
    }

    /**
     * All functions running in thread
     */
    class MyOnWalletRefreshListener extends OnWalletRefreshListener.Stub {
        @Override
        public void queueFullError(final String error) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    BaseActivity.showShortToast(context, error);
                }
            });
        }

        @Override
        public void beginLoadWallet(final int walletId) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.beginLoadWalletIfActivityExist(walletId);
                    WalletRunningActivity.beginLoadWalletIfActivityExist(walletId);
                }
            });
        }

        @Override
        public void synchronizeStatusError(final int walletId, final String error) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.showSynchronizeStatusErrorIfActivityExist(walletId, error);
                    WalletRunningActivity.showSynchronizeStatusErrorIfActivityExist(walletId, error);
                }
            });
        }

        @Override
        public void synchronizeStatusSuccess(final int walletId) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.synchronizeStatusSuccessIfActivityExist(walletId);
                    WalletRunningActivity.synchronizeStatusSuccessIfActivityExist(walletId);
                }
            });
        }

        @Override
        public void refreshBalance(final int walletId, final String balance, final String unlockedBalance) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.refreshBalanceIfActivityExist(walletId, balance, unlockedBalance);
                    WalletRunningActivity.refreshBalanceIfActivityExist(walletId, balance, unlockedBalance);
                }
            });
        }

        @Override
        public void blockProgress(final int walletId, final boolean result, final long blockChainHeight, final long daemonHeight, final int progress) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.showBlockProgressIfActivityExist(walletId, result, blockChainHeight, daemonHeight, progress);
                    WalletRunningActivity.showBlockProgressIfActivityExist(walletId, result, blockChainHeight, daemonHeight, progress);
                }
            });
        }

        @Override
        public void refreshTransaction(final int walletId) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.refreshTransactionIfActivityExist(walletId);
                    WalletRunningActivity.refreshTransactionIfActivityExist(walletId);
                }
            });
        }

        @Override
        public void closeActiveWallet(final int walletId) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MainActivity.closeWalletIfActivityExist(walletId);
                }
            });
        }

        @Override
        public void moneySpent(final int walletId, final String txId, final long amount, final boolean fullSynchronizeOnce) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

        @Override
        public void moneyReceive(final int walletId, final String txId, final long amount, final boolean fullSynchronizeOnce) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

        @Override
        public void unconfirmedMoneyReceive(final int walletId, final String txId, final long amount, final boolean fullSynchronizeOnce) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (txId != null && cacheHelper.getCache(txId) == null) {
                        cacheHelper.putCache(txId, txId);
                        receiveNotfication(walletId, txId, amount);
                    }
                }
            });
        }

        /**
         * Running in Main thread
         */
        private void receiveNotfication(int walletId, String txId, long amount) {
            try {
                Intent notificationIntent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
                String content = context.getString(R.string.start_receive_transaction_notfication_tips) + String.valueOf(amount / 1000000.0f) + " " + XManager.SYMBOL;
                notificationId = notificationId + 1;
                notificationHelper.sendNotification(notificationId, context.getString(R.string.app_name), content, pendingIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnVerifyWalletPasswordListener {

        void onSuccess(String tips);

        void onError(String error);

    }

    public interface OnOpenWalletListener {

        void onSuccess(com.xcash.wallet.aidl.Wallet wallet);

        void onError(String error);

    }
}
