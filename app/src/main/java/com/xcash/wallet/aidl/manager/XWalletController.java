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
package com.xcash.wallet.aidl.manager;

 import com.my.monero.data.TxData;
 import com.my.monero.model.PendingTransaction;
 import com.my.monero.model.SubaddressRow;
 import com.my.monero.model.WalletListener;
 import com.my.monero.model.WalletManager;
 import com.my.monero.util.RestoreHeight;
 import com.xcash.base.utils.LogTool;
 import com.xcash.utils.database.entity.Node;
 import com.xcash.utils.database.entity.Wallet;
 import com.xcash.utils.database.models.SubAddress;

 import java.io.File;
 import java.util.ArrayList;
 import java.util.List;

public class XWalletController {

    public static final String MNEMONIC_LANGUAGE = "English";
    public static final String TAG = "XWalletController";

    private int activeWalletId = -1;

    private com.my.monero.model.Wallet activeWallet;

    public int getActiveWalletId() {
        return activeWalletId;
    }

    public com.my.monero.model.Wallet getActiveWallet() {
        return activeWallet;
    }

    public Wallet createWallet(File file, String password) {
        if (file == null || password == null) {
            return null;
        }
        com.my.monero.model.Wallet newWallet = WalletManager.getInstance().createWallet(file, password, MNEMONIC_LANGUAGE);
        return close(newWallet);
    }

    public Wallet recoveryWallet(File file, String password, String mnemonic, long restoreHeight) {
        if (file == null || password == null || mnemonic == null) {
            return null;
        }
        com.my.monero.model.Wallet newWallet = WalletManager.getInstance().recoveryWallet(file, password, mnemonic, restoreHeight);
        return close(newWallet);
    }

    public Wallet createWalletWithKeys(File file, String password, long restoreHeight, String address, String viewKey, String spendKey) {
        if (file == null || password == null || address == null || viewKey == null || spendKey == null) {
            return null;
        }
        com.my.monero.model.Wallet newWallet = WalletManager.getInstance().createWalletWithKeys(file, password, MNEMONIC_LANGUAGE, restoreHeight, address, viewKey, spendKey);
        return close(newWallet);
    }

    private Wallet close(com.my.monero.model.Wallet newWallet) {
        Wallet wallet = new Wallet();
        boolean success = newWallet.getStatus() == com.my.monero.model.Wallet.Status.Status_Ok;
        if (success) {
            wallet.setSymbol(XManager.SYMBOL);
            wallet.setName(newWallet.getName());
            wallet.setAddress(newWallet.getAddress());
            wallet.setRestoreHeight(newWallet.getRestoreHeight());
            wallet.setSeed(newWallet.getSeed());
            wallet.setSecretViewKey(newWallet.getSecretViewKey());
            wallet.setSecretSpendKey(newWallet.getSecretSpendKey());
            if (newWallet.getSecretSpendKey()==null||newWallet.getSecretSpendKey().equals("")){
                wallet.setReadOnly(true);
            }else {
                wallet.setReadOnly(false);
            }
        } else {
            wallet.setErrorString(newWallet.getErrorString());
        }
        newWallet.close();
        return wallet;
    }

    public boolean verifyWalletPasswordOnly(String keyPath, String password) {
        return WalletManager.getInstance().verifyWalletPasswordOnly(keyPath, password);
    }

    public String getSeed() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return "";
        }
        wallet.setSeedLanguage(MNEMONIC_LANGUAGE);
        return wallet.getSeed();
    }

    public String getPublicViewKey() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return "";
        }
        return wallet.getPublicViewKey();
    }

    public String getPublicSpendKey() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return "";
        }
        return wallet.getPublicSpendKey();
    }

    public String getSecretViewKey() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return "";
        }
        return wallet.getSecretViewKey();
    }

    public String getSecretSpendKey() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return "";
        }
        return wallet.getSecretSpendKey();
    }

    public com.my.monero.model.Wallet openWallet(String path, String password, int walletId) {
        closeWallet();
        com.my.monero.model.Wallet wallet = WalletManager.getInstance().openWallet(path, password);
        if (wallet == null) {
            LogTool.e(TAG, "wallet opened failed");
            return null;
        }
        if (wallet.getStatus() != com.my.monero.model.Wallet.Status.Status_Ok) {
            LogTool.e(TAG, wallet.getErrorString());
        } else {
            activeWallet = wallet;
            activeWalletId = walletId;
        }
        return wallet;
    }

    public boolean startWallet(com.my.monero.model.Wallet wallet, Node node, long restoreHeight, OnWalletListener onWalletListener) {
        if (wallet == null || onWalletListener == null) {
            return false;
        }
        if (!startRefresh(wallet, node, restoreHeight, onWalletListener)) {
            return false;
        }
        onWalletListener.onWalletStarted();
        return true;
    }

    public boolean startRefresh(final com.my.monero.model.Wallet wallet, Node node, long restoreHeight, final OnWalletListener onWalletListener) {
        if (wallet == null || node == null || onWalletListener == null) {
            return false;
        }
        WalletManager.getInstance().setDaemonAddress(node.getUrl());
        wallet.init(node.getUrl(), 0, node.getUsername(), node.getPassword());
        wallet.setRestoreHeight(restoreHeight);
        if (wallet.getConnectionStatus() != com.my.monero.model.Wallet.ConnectionStatus.ConnectionStatus_Connected) {
            LogTool.d(TAG, "Connection error");
            onWalletListener.onWalletStartFailed(wallet.getErrorString());
            return false;
        }
        wallet.setListener(new WalletListener() {

            private long lastBlockTime;
            private boolean synced;

            @Override
            public void moneySpent(String txId, long amount) {
                LogTool.d(TAG, "moneySpent txId: " + txId + "+amount:" + amount);
                onWalletListener.onMoneySpent(txId, amount);
                wallet.store();
            }

            @Override
            public void moneyReceived(String txId, long amount) {
                LogTool.d(TAG, "moneyReceived txId: " + txId + "+amount:" + amount);
                onWalletListener.onMoneyReceived(txId, amount);
                wallet.store();
            }

            @Override
            public void unconfirmedMoneyReceived(String txId, long amount) {
                LogTool.d(TAG, "unconfirmedMoneyReceived txId: " + txId + "+amount:" + amount);
                onWalletListener.unconfirmedMoneyReceived(txId, amount);
                wallet.store();
            }

            @Override
            public void newBlock(long height) {
                if (lastBlockTime < System.currentTimeMillis() - 2000) {
                    lastBlockTime = System.currentTimeMillis();
                    LogTool.d(TAG, "newBlock height: " + height);
                    onWalletListener.onRefreshed(height);
                    wallet.store();
                }
            }

            @Override
            public void updated() {
                LogTool.d(TAG, "updated");
            }

            @Override
            public void refreshed() {
                LogTool.d(TAG, "refreshed");
                if (wallet.isSynchronized() && !synced) {
                    if (wallet.store()) {
                        synced = true;
                    }
                }
                onWalletListener.onRefreshed(-1);
            }
        });
        wallet.startRefresh();
        return true;
    }

    public void refreshWallet() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet != null) {
            wallet.refresh();
        }
    }

    public void stopRefresh() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet != null) {
            wallet.pauseRefresh();
            wallet.setListener(null);
        }
    }

    public void closeWallet() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        LogTool.d(TAG, "closeWallet");
        if (wallet != null) {
            wallet.close();
            activeWallet = null;
            activeWalletId = -1;
        } else {
            LogTool.e(TAG, "activeWallet is null");
        }
    }

    public boolean isRunning() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return false;
        }
        return wallet.getStatus() == com.my.monero.model.Wallet.Status.Status_Ok;
    }

    public boolean isConnecting() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return false;
        }
        return wallet.getStatus() == com.my.monero.model.Wallet.Status.Status_Ok && wallet.getConnectionStatus() == com.my.monero.model.Wallet.ConnectionStatus.ConnectionStatus_Connected;
    }

    public long getDaemonBlockChainHeight() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return 0;
        }
        return wallet.getDaemonBlockChainHeight();
    }

    public long getDaemonBlockChainTargetHeight() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return 0;
        }
        return wallet.getDaemonBlockChainTargetHeight();
    }

    public long getBlockChainHeight() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return 0;
        }
        return wallet.getBlockChainHeight();
    }


    public long getBalance() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return 0;
        }
        return wallet.getBalance();
    }

    public boolean isSynchronized() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return false;
        }
        return wallet.isSynchronized();
    }

    public String getDisplayAmount(long amount) {
        String displayAmount = com.my.monero.model.Wallet.getDisplayAmount(amount);
        if (displayAmount == null) {
            return "";
        }
        return displayAmount;
    }

    public long getAmountFromString(String amount) {
        if (amount == null) {
            return 0;
        }
        return com.my.monero.model.Wallet.getAmountFromString(amount);
    }

    public String getIntegratedAddress(String id) {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return "";
        }
        return wallet.getIntegratedAddress(id);
    }

    public String generatePaymentId() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return "";
        }
        return wallet.generatePaymentId();
    }

    public boolean isAddressValid(String address) {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return false;
        }
        return com.my.monero.model.Wallet.isAddressValid(address);
    }

    public boolean isPaymentIdValid(String paymentId) {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return false;
        }
        return com.my.monero.model.Wallet.isPaymentIdValid(paymentId);
    }

    public PendingTransaction createTransaction(String walletAddress, String amount, String ringSize, String paymentId, String description, boolean publicTransaction, PendingTransaction.Priority priority) {
        if (walletAddress == null || amount == null) {
            return null;
        }
        if (priority == null) {
            priority = PendingTransaction.Priority.Priority_Default;
        }
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return null;
        }
        if (ringSize == null || ringSize.equals("")) {
            ringSize = "21";
        }
        try {
            TxData txData = new TxData();
            txData.setDstAddr(walletAddress);
            txData.setPaymentId(paymentId);
            if (amount.equals("-1")) {
                txData.setAmount(-1);
            } else {
                txData.setAmount(com.my.monero.model.Wallet.getAmountFromString(amount));
            }
            txData.setMixin(Integer.valueOf(ringSize) - 1);
            txData.setPriority(priority);
            txData.setPublicTransaction(publicTransaction);
            wallet.disposePendingTransaction();
            PendingTransaction pendingTransaction = wallet.createTransaction(txData);
            PendingTransaction.Status status = pendingTransaction.getStatus();

            if (status != PendingTransaction.Status.Status_Ok) {
                wallet.disposePendingTransaction();
                LogTool.e(TAG, pendingTransaction.getErrorString());
                return null;
            }
            return pendingTransaction;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String sendTransaction() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return "";
        }
        PendingTransaction pendingTransaction = wallet.getPendingTransaction();
        if (pendingTransaction == null) {
            LogTool.e(TAG, "getPendingTransaction failed");
            return "";
        }
        PendingTransaction.Status status = pendingTransaction.getStatus();
        if (status != PendingTransaction.Status.Status_Ok) {
            wallet.disposePendingTransaction();
            LogTool.e(TAG, pendingTransaction.getErrorString());
            return "";
        }

        String firstTxId = pendingTransaction.getFirstTxId();
        boolean success = pendingTransaction.commit("", true);
        if (success) {
            wallet.disposePendingTransaction();
            if (!wallet.store()) {
                LogTool.e(TAG, wallet.getErrorString());
            }
        } else {
            wallet.disposePendingTransaction();
            LogTool.e(TAG, pendingTransaction.getErrorString());
            return "";
        }
        return firstTxId;
    }

    public String getTxAmount() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return "";
        }
        PendingTransaction pendingTransaction = wallet.getPendingTransaction();
        if (pendingTransaction == null) {
            LogTool.e(TAG, "getPendingTransaction failed");
            return "";
        }
        PendingTransaction.Status status = pendingTransaction.getStatus();
        if (status != PendingTransaction.Status.Status_Ok) {
            wallet.disposePendingTransaction();
            LogTool.e(TAG, pendingTransaction.getErrorString());
            return "";
        }
        return getDisplayAmount(pendingTransaction.getAmount());
    }

    public String getTxFee() {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return "";
        }
        PendingTransaction pendingTransaction = wallet.getPendingTransaction();
        if (pendingTransaction == null) {
            LogTool.e(TAG, "getPendingTransaction failed");
            return "";
        }
        PendingTransaction.Status status = pendingTransaction.getStatus();
        if (status != PendingTransaction.Status.Status_Ok) {
            wallet.disposePendingTransaction();
            LogTool.e(TAG, pendingTransaction.getErrorString());
            return "";
        }
        return getDisplayAmount(pendingTransaction.getFee());
    }

    public long getBlockHeight(String value) {
        return RestoreHeight.getInstance().getHeight(value);
    }

    public void addSubAddress(String label) {
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return;
        }
        wallet.addSubaddress(label);
        wallet.store();
    }

    public List<SubAddress> getSubAddresses() {
        List<SubAddress> subAddresses = new ArrayList<>();
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return subAddresses;
        }
        List<SubaddressRow> subaddressRows = wallet.getSubaddresses();
        if (subaddressRows == null) {
            return subAddresses;
        }
        for (int i = 0; i < subaddressRows.size(); i++) {
            SubaddressRow subaddressRow = subaddressRows.get(i);
            if (subaddressRow != null) {
                subAddresses.add(new SubAddress(subaddressRow.rowId, subaddressRow.address, subaddressRow.label));
            }
        }
        return subAddresses;
    }

    public interface OnWalletListener {

        void onWalletStarted();

        void onWalletStartFailed(String error);

        void onRefreshed(long height);

        void onMoneySpent(String txId, long amount);

        void onMoneyReceived(String txId, long amount);

        void unconfirmedMoneyReceived(String txId, long amount);

    }

}
