/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet.aidl.manager;

import android.util.Log;

import com.my.monero.data.Node;
import com.my.monero.data.TxData;
import com.my.monero.model.PendingTransaction;
import com.my.monero.model.SubaddressRow;
import com.my.monero.model.TransactionHistory;
import com.my.monero.model.WalletListener;
import com.my.monero.model.WalletManager;
import com.my.monero.util.RestoreHeight;
import com.my.utils.database.entity.TransactionInfo;
import com.my.utils.database.entity.Wallet;
import com.my.utils.database.models.SubAddress;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XWalletController {

    public static final String MNEMONIC_LANGUAGE = "English";

    private final String TAG = "XWalletController";

    private int activeWalletId = -1;

    private com.my.monero.model.Wallet activeWallet = null;

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
        } else {
            wallet.setErrorString(newWallet.getErrorString());
        }
        newWallet.close();
        return wallet;
    }


    public void setNode(String host, int port) {
        if (host == null) {
            return;
        }
        try {
            Node node = new Node();
            node.setHost(host);
            node.setRpcPort(port);
            WalletManager.getInstance().setDaemon(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            Log.e(TAG, "wallet opened failed");
            return null;
        }
        if (wallet.getStatus() != com.my.monero.model.Wallet.Status.Status_Ok) {
            Log.e(TAG, wallet.getErrorString());
        } else {
            activeWallet = wallet;
            activeWalletId = walletId;
        }
        return wallet;
    }

    public boolean startWallet(com.my.monero.model.Wallet wallet, long restoreHeight, OnWalletListener onWalletListener) {
        if (wallet == null || onWalletListener == null) {
            return false;
        }
        if (!startRefresh(wallet, restoreHeight, onWalletListener)) {
            return false;
        }
        onWalletListener.onWalletStarted();
        return true;
    }

    public boolean startRefresh(final com.my.monero.model.Wallet wallet, long restoreHeight, final OnWalletListener onWalletListener) {
        if (wallet == null || onWalletListener == null) {
            return false;
        }
        wallet.init(0);
        wallet.setRestoreHeight(restoreHeight);
        Log.d(TAG, wallet.getRestoreHeight() + "Using daemon" + WalletManager.getInstance().getDaemonAddress());

        if (wallet.getConnectionStatus() != com.my.monero.model.Wallet.ConnectionStatus.ConnectionStatus_Connected) {
            Log.d(TAG, "Connection error");
            onWalletListener.onWalletStartFailed(wallet.getErrorString());
            return false;
        }

        wallet.setListener(new WalletListener() {

            private long lastBlockTime;
            private boolean synced;


            @Override
            public void moneySpent(String txId, long amount) {
                Log.d(TAG, "moneySpent txId: " + txId + "+amount:" + amount);
            }

            @Override
            public void moneyReceived(String txId, long amount) {
                Log.d(TAG, "moneyReceived txId: " + txId + "+amount:" + amount);
            }

            @Override
            public void unconfirmedMoneyReceived(String txId, long amount) {
                Log.d(TAG, "unconfirmedMoneyReceived txId: " + txId + "+amount:" + amount);
            }

            @Override
            public void newBlock(long height) {
                //Log.d(TAG, "newBlock height: " + height);
                if (lastBlockTime < System.currentTimeMillis() - 2000) {
                    lastBlockTime = System.currentTimeMillis();
                    onWalletListener.onRefreshed(height);
                    wallet.store();
                }
            }

            @Override
            public void updated() {
                Log.d(TAG, "updated");
            }

            @Override
            public void refreshed() {
                Log.d(TAG, "refreshed");
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
        Log.d(TAG, "closeWallet");
        if (wallet != null) {
            wallet.close();
            activeWallet = null;
            activeWalletId = -1;
        } else {
            Log.e(TAG, "activeWallet is null");
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

    /**
     * return NotNull
     */
    public List<TransactionInfo> getTransactionHistory() {
        List<TransactionInfo> transactionInfos = new ArrayList<>();
        com.my.monero.model.Wallet wallet = getActiveWallet();
        if (wallet == null) {
            return transactionInfos;
        }
        TransactionHistory transactionHistory = wallet.getHistory();
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
            if (theTransactionInfo != null) {
                TransactionInfo transactionInfo = new TransactionInfo();
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
        }
        return transactionInfos;
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
            txData.setAmount(com.my.monero.model.Wallet.getAmountFromString(amount));
            txData.setMixin(Integer.valueOf(ringSize) - 1);
            txData.setPriority(priority);
            txData.setPublicTransaction(publicTransaction);
            wallet.disposePendingTransaction();
            PendingTransaction pendingTransaction = wallet.createTransaction(txData);
            PendingTransaction.Status status = pendingTransaction.getStatus();

            if (status != PendingTransaction.Status.Status_Ok) {
                wallet.disposePendingTransaction();
                Log.e(TAG, pendingTransaction.getErrorString());
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
            Log.e(TAG, "getPendingTransaction failed");
            return "";
        }
        PendingTransaction.Status status = pendingTransaction.getStatus();
        if (status != PendingTransaction.Status.Status_Ok) {
            wallet.disposePendingTransaction();
            Log.e(TAG, pendingTransaction.getErrorString());
            return "";
        }

        String firstTxId = pendingTransaction.getFirstTxId();
        boolean success = pendingTransaction.commit("", true);
        if (success) {
            wallet.disposePendingTransaction();
            if (!wallet.store()) {
                Log.e(TAG, wallet.getErrorString());
            }
        } else {
            wallet.disposePendingTransaction();
            Log.e(TAG, pendingTransaction.getErrorString());
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
            Log.e(TAG, "getPendingTransaction failed");
            return "";
        }
        PendingTransaction.Status status = pendingTransaction.getStatus();
        if (status != PendingTransaction.Status.Status_Ok) {
            wallet.disposePendingTransaction();
            Log.e(TAG, pendingTransaction.getErrorString());
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
            Log.e(TAG, "getPendingTransaction failed");
            return "";
        }
        PendingTransaction.Status status = pendingTransaction.getStatus();
        if (status != PendingTransaction.Status.Status_Ok) {
            wallet.disposePendingTransaction();
            Log.e(TAG, pendingTransaction.getErrorString());
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
            com.my.monero.model.SubaddressRow subaddressRow = subaddressRows.get(i);
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

    }

}
