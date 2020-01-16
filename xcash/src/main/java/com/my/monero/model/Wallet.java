/**
 * Copyright (c) 2017-2018 m2049r
 * <p>
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */

package com.my.monero.model;


import android.util.Log;

import com.my.monero.data.TxData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Wallet {

    final static public long SWEEP_ALL = Long.MAX_VALUE;

    static {
        System.loadLibrary("monerujo");
    }

    private int accountIndex = 0;

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

    private long handle = 0;
    private long listenerHandle = 0;

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

//TODO virtual void hardForkInfo(uint8_t &version, uint64_t &earliest_height) const = 0;
//TODO virtual bool useForkRules(uint8_t version, int64_t early_blocks) const = 0;

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

    //    virtual std::string keysFilename() const = 0;
    public boolean init(String daemon_address, long upper_transaction_size_limit, String daemon_username, String daemon_password) {
        return initJ(daemon_address, upper_transaction_size_limit, daemon_username, daemon_password);
    }

    private native boolean initJ(String daemon_address, long upper_transaction_size_limit,
                                 String daemon_username, String daemon_password);

//    virtual bool createWatchOnly(const std::string &path, const std::string &password, const std::string &language) const = 0;
//    virtual void setRefreshFromBlockHeight(uint64_t refresh_from_block_height) = 0;

    public native void setRestoreHeight(long height);

    public native long getRestoreHeight();

    //    virtual void setRecoveringFromSeed(bool recoveringFromSeed) = 0;
//    virtual bool connectToDaemon() = 0;

    public ConnectionStatus getConnectionStatus() {
        int s = getConnectionStatusJ();
        return ConnectionStatus.values()[s];
    }

    private native int getConnectionStatusJ();

//TODO virtual void setTrustedDaemon(bool arg) = 0;
//TODO virtual bool trustedDaemon() const = 0;

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

//TODO virtual void setAutoRefreshInterval(int millis) = 0;
//TODO virtual int autoRefreshInterval() const = 0;


    private PendingTransaction pendingTransaction = null;

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

//virtual UnsignedTransaction * loadUnsignedTx(const std::string &unsigned_filename) = 0;
//virtual bool submitTransaction(const std::string &fileName) = 0;

    public native void disposeTransaction(PendingTransaction pendingTransaction);

//virtual bool exportKeyImages(const std::string &filename) = 0;
//virtual bool importKeyImages(const std::string &filename) = 0;


//virtual TransactionHistory * history() const = 0;

    private TransactionHistory history = null;

    public TransactionHistory getHistory() {
        if (history == null) {
            history = new TransactionHistory(getHistoryJ(), accountIndex);
        }
        return history;
    }

    private native long getHistoryJ();

//virtual AddressBook * addressBook() const = 0;
//virtual void setListener(WalletListener *) = 0;

    private native long setListenerJ(WalletListener listener);

    public void setListener(WalletListener listener) {
        this.listenerHandle = setListenerJ(listener);
    }

    public native int getDefaultMixin();

    public native void setDefaultMixin(int mixin);

    public native boolean setUserNote(String txid, String note);

    public native String getUserNote(String txid);

    public native String getTxKey(String txid);

//virtual std::string signMessage(const std::string &message) = 0;
//virtual bool verifySignedMessage(const std::string &message, const std::string &addres, const std::string &signature) const = 0;

//virtual bool parse_uri(const std::string &uri, std::string &address, std::string &payment_id, uint64_t &tvAmount, std::string &tx_description, std::string &recipient_name, std::vector<std::string> &unknown_parameters, std::string &error) = 0;
//virtual bool rescanSpent() = 0;

    private static final String NEW_ACCOUNT_NAME = "Untitled account"; // src/wallet/wallet2.cpp:941

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

}
