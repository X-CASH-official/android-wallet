/**
 * Copyright (c) 2017-2018 m2049r
 * <p>
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */

package com.my.monero.model;


import com.my.monero.data.TxData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Wallet {

    static {
        System.loadLibrary("monerujo");
    }

    private final String NEW_ACCOUNT_NAME = "Untitled account";
    private int accountIndex;
    private long handle;
    private long listenerHandle;
    private PendingTransaction pendingTransaction;

    public int getAccountIndex() {
        return accountIndex;
    }

    public void setAccountIndex(int accountIndex) {
        this.accountIndex = accountIndex;
        getHistory().setAccountFor(this);
    }

    public String getName() {
        return new File(getPath()).getName();
    }

    Wallet(long handle) {
        this.handle = handle;
    }

    Wallet(long handle, int accountIndex) {
        this.handle = handle;
        this.accountIndex = accountIndex;
    }

    public enum Device {
        Device_Undefined,
        Device_Software,
        Device_Ledger
    }

    public enum Status {
        Status_Ok,
        Status_Error,
        Status_Critical
    }

    public enum ConnectionStatus {
        ConnectionStatus_Disconnected,
        ConnectionStatus_Connected,
        ConnectionStatus_WrongVersion
    }

    public native String getSeed();

    public native String getSeedLanguage();

    public native void setSeedLanguage(String language);

    public Status getStatus() {
        return Status.values()[getStatusJ()];
    }

    private native int getStatusJ();

    public native String getErrorString();

    public native boolean setPassword(String password);

    public String getAddress() {
        return getAddress(accountIndex);
    }

    public String getAddress(int accountIndex) {
        return getAddressJ(accountIndex, 0);
    }

    public String getSubaddress(int addressIndex) {
        return getAddressJ(accountIndex, addressIndex);
    }

    public String getSubaddress(int accountIndex, int addressIndex) {
        return getAddressJ(accountIndex, addressIndex);
    }

    private native String getAddressJ(int accountIndex, int addressIndex);

    public native String getPath();

    public NetworkType getNetworkType() {
        return NetworkType.fromInteger(nettype());
    }

    public native int nettype();

    public native String getIntegratedAddress(String payment_id);

    public native String getSecretViewKey();

    public native String getSecretSpendKey();

    public native String getPublicViewKey();

    public native String getPublicSpendKey();

    public boolean store() {
        final boolean ok = store("");
        return ok;
    }

    public native boolean store(String path);

    public boolean close() {
        disposePendingTransaction();
        return WalletManager.getInstance().close(this);
    }

    public native String getFilename();

    public boolean init(String daemon_address, long upper_transaction_size_limit, String daemon_username, String daemon_password) {
        return initJ(daemon_address, upper_transaction_size_limit, daemon_username, daemon_password);
    }

    private native boolean initJ(String daemon_address, long upper_transaction_size_limit,
                                 String daemon_username, String daemon_password);

    public native void setRestoreHeight(long height);

    public native long getRestoreHeight();

    public ConnectionStatus getConnectionStatus() {
        int s = getConnectionStatusJ();
        return ConnectionStatus.values()[s];
    }

    private native int getConnectionStatusJ();

    public long getBalance() {
        return getBalance(accountIndex);
    }

    public native long getBalance(int accountIndex);

    public native long getBalanceAll();

    public long getUnlockedBalance() {
        return getUnlockedBalance(accountIndex);
    }

    public native long getUnlockedBalanceAll();

    public native long getUnlockedBalance(int accountIndex);

    public native boolean isWatchOnly();

    public native long getBlockChainHeight();

    public native long getApproximateBlockChainHeight();

    public native long getDaemonBlockChainHeight();

    public native long getDaemonBlockChainTargetHeight();

    public native boolean isSynchronized();

    public static native String getDisplayAmount(long amount);

    public static native long getAmountFromString(String amount);

    public static native long getAmountFromDouble(double amount);

    public static native String generatePaymentId();

    public static native boolean isPaymentIdValid(String payment_id);

    public static boolean isAddressValid(String address) {
        return isAddressValid(address, WalletManager.getInstance().getNetworkType().getValue());
    }

    public static native boolean isAddressValid(String address, int networkType);

    public static native String getPaymentIdFromAddress(String address, int networkType);

    public static native long getMaximumAllowedAmount();

    public native void startRefresh();

    public native void pauseRefresh();

    public native boolean refresh();

    public native void refreshAsync();

    public PendingTransaction getPendingTransaction() {
        return pendingTransaction;
    }

    public void disposePendingTransaction() {
        if (pendingTransaction != null) {
            disposeTransaction(pendingTransaction);
            pendingTransaction = null;
        }
    }

    public PendingTransaction createTransaction(TxData txData) throws Exception {
        int privacy_settings = 0;
        if (!txData.isPublicTransaction()) {
            privacy_settings = 1;
        }
        return createTransaction(
                txData.getDstAddr(),
                txData.getPaymentId(),
                txData.getAmount(),
                txData.getMixin(),
                txData.getPriority(),
                privacy_settings
        );
    }

    public PendingTransaction createTransaction(String dst_addr, String payment_id,
                                                long amount, int mixin_count,
                                                PendingTransaction.Priority priority, int privacy_settings) throws Exception {
        disposePendingTransaction();
        int _priority = priority.getValue();
        long txHandle;
        if (amount == -1) {
            txHandle = createSweepTransaction(dst_addr, payment_id, mixin_count, _priority,
                    accountIndex, privacy_settings);
        } else {
            txHandle = createTransactionJ(dst_addr, payment_id, amount, mixin_count, _priority,
                    accountIndex, privacy_settings);
        }
        checkTxHandle(txHandle);
        pendingTransaction = new PendingTransaction(txHandle);
        return pendingTransaction;
    }

    private void checkTxHandle(long txHandle) {
        if (txHandle == 0) {
            throw new IllegalStateException("createTransaction failed");
        }
    }

    private native long createTransactionJ(String dst_addr, String payment_id,
                                           long amount, int mixin_count,
                                           int priority, int accountIndex, int tx_privacy_settings);

    private native long createSweepTransaction(String dst_addr, String payment_id,
                                               int mixin_count,
                                               int priority, int accountIndex, int tx_privacy_settings);

    public PendingTransaction createSweepUnmixableTransaction() throws Exception {
        disposePendingTransaction();
        long txHandle = createSweepUnmixableTransactionJ();
        checkTxHandle(txHandle);
        pendingTransaction = new PendingTransaction(txHandle);
        return pendingTransaction;
    }

    private native long createSweepUnmixableTransactionJ();

    public native void disposeTransaction(PendingTransaction pendingTransaction);

    private TransactionHistory history = null;

    public TransactionHistory getHistory() {
        if (history == null) {
            history = new TransactionHistory(getHistoryJ(), accountIndex);
        }
        return history;
    }

    private native long getHistoryJ();

    private native long setListenerJ(WalletListener listener);

    public void setListener(WalletListener listener) {
        this.listenerHandle = setListenerJ(listener);
    }

    public native int getDefaultMixin();

    public native void setDefaultMixin(int mixin);

    public native boolean setUserNote(String txid, String note);

    public native String getUserNote(String txid);

    public native String getTxKey(String txid);

    public void addAccount() {
        addAccount(NEW_ACCOUNT_NAME);
    }

    public native void addAccount(String label);

    public String getAccountLabel() {
        return getAccountLabel(accountIndex);
    }

    public String getAccountLabel(int accountIndex) {
        String label = getSubaddressLabel(accountIndex, 0);
        if (label.equals(NEW_ACCOUNT_NAME)) {
            String address = getAddress(accountIndex);
            int len = address.length();
            return address.substring(0, 6) +
                    "\u2026" + address.substring(len - 6, len);
        } else return label;
    }

    public String getSubaddressLabel(int addressIndex) {
        return getSubaddressLabel(accountIndex, addressIndex);
    }

    public native String getSubaddressLabel(int accountIndex, int addressIndex);

    public void setAccountLabel(String label) {
        setAccountLabel(accountIndex, label);
    }

    public void setAccountLabel(int accountIndex, String label) {
        setSubaddressLabel(accountIndex, 0, label);
    }

    public native void setSubaddressLabel(int accountIndex, int addressIndex, String label);

    public native int getNumAccounts();

    public int getNumSubaddresses() {
        return getNumSubaddresses(accountIndex);
    }

    public native int getNumSubaddresses(int accountIndex);

    public String getNewSubaddress() {
        return getNewSubaddress(accountIndex);
    }

    public String getNewSubaddress(int accountIndex) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.US).format(new Date());
        addSubaddress(accountIndex, timeStamp);
        String subaddress = getLastSubaddress(accountIndex);
        return subaddress;
    }

    public void addSubaddress(String label) {
        addSubaddress(accountIndex, label);
    }

    public List<SubaddressRow> getSubaddresses() {
        return getSubaddresses(accountIndex);
    }

    public native void addSubaddress(int accountIndex, String label);

    public native List<SubaddressRow> getSubaddresses(int accountIndex);

    public String getLastSubaddress(int accountIndex) {
        return getSubaddress(accountIndex, getNumSubaddresses(accountIndex) - 1);
    }

    public Device getDeviceType() {
        int device = getDeviceTypeJ();
        return Device.values()[device + 1]; // mapping is monero+1=android
    }

    private native int getDeviceTypeJ();

    public native String lightWalletLogin(boolean isNewWallet);

    public native String delegateRegister(String delegate_name, String delegate_IP_address, String block_verifier_messages_public_key);

    public native String delegateUpdate(String item, String value);

    public native String vote(String value);

}
