/**
 * Copyright (c) 2017-2018 m2049r
 *
 * Copyright (c) 2019 by snakeway
 *
 * All rights reserved.
 */

package com.my.monero.model;


import android.util.Log;

import com.my.monero.data.Node;
import com.my.monero.ledger.Ledger;
import com.my.monero.util.RestoreHeight;

import java.io.File;
import java.util.Date;

public class WalletManager {

    private static final String TAG = "WalletManager";

    static {
        System.loadLibrary("monerujo");
    }

    private static WalletManager walletManager;


    private String daemonUsername = "";

    private String daemonPassword = "";

    private boolean activeDaemon;

    private String daemonAddress = null;

    private final NetworkType networkType = NetworkType.NetworkType_Mainnet;

    public static synchronized WalletManager getInstance() {
        if (walletManager == null) {
            walletManager = new WalletManager();
        }
        return walletManager;
    }


    public Wallet createWallet(File aFile, String password, String language) {
        long walletHandle = createWalletJ(aFile.getAbsolutePath(), password, language, getNetworkType().getValue());
        Wallet wallet = new Wallet(walletHandle);
        if (wallet.getStatus() == Wallet.Status.Status_Ok) {
            long oldHeight = wallet.getRestoreHeight();
            wallet.setRestoreHeight(RestoreHeight.getInstance().getHeight(new Date()));
            Log.d(TAG, "Changed Restore Height from " + oldHeight + " to " + wallet.getRestoreHeight());
            wallet.setPassword(password); // this rewrites the keys file (which contains the restore height)
        }
        return wallet;
    }


    public Wallet openAccount(String path, int accountIndex, String password) {
        long walletHandle = openWalletJ(path, password, getNetworkType().getValue());
        Wallet wallet = new Wallet(walletHandle, accountIndex);
        return wallet;
    }

    public Wallet openWallet(String path, String password) {
        long walletHandle = openWalletJ(path, password, getNetworkType().getValue());
        Wallet wallet = new Wallet(walletHandle);
        return wallet;
    }


    public Wallet recoveryWallet(File aFile, String password, String mnemonic) {
        return recoveryWallet(aFile, password, mnemonic, 0);
    }

    public Wallet recoveryWallet(File aFile, String password, String mnemonic, long restoreHeight) {
        long walletHandle = recoveryWalletJ(aFile.getAbsolutePath(), password, mnemonic,
                getNetworkType().getValue(), restoreHeight);
        Wallet wallet = new Wallet(walletHandle);
        return wallet;
    }


    public Wallet createWalletWithKeys(File aFile, String password, String language, long restoreHeight,
                                       String addressString, String viewKeyString, String spendKeyString) {
        long walletHandle = createWalletFromKeysJ(aFile.getAbsolutePath(), password,
                language, getNetworkType().getValue(), restoreHeight,
                addressString, viewKeyString, spendKeyString);
        Wallet wallet = new Wallet(walletHandle);
        return wallet;
    }

    public Wallet createWalletFromDevice(File aFile, String password, long restoreHeight,
                                         String deviceName) {
        long walletHandle = createWalletFromDeviceJ(aFile.getAbsolutePath(), password,
                getNetworkType().getValue(), deviceName, restoreHeight,
                Ledger.SUBADDRESS_LOOKAHEAD);
        Wallet wallet = new Wallet(walletHandle);
        return wallet;
    }

    public boolean close(Wallet wallet) {
        wallet.pauseRefresh();
        wallet.setListener(null);
        return closeJ(wallet);
    }

    public boolean walletExists(File aFile) {
        return walletExists(aFile.getAbsolutePath());
    }


    public boolean verifyWalletPasswordOnly(String keys_file_name, String password) {
        return queryWalletDeviceJ(keys_file_name, password) >= 0;
    }

    public Wallet.Device queryWalletDevice(String keys_file_name, String password) {
        int device = queryWalletDeviceJ(keys_file_name, password);
        return Wallet.Device.values()[device + 1];
    }


    public void setDaemon(Node node) {
        if (node != null) {
            this.daemonAddress = node.getAddress();
            if (networkType != node.getNetworkType())
                throw new IllegalArgumentException("network type does not match");
            this.daemonUsername = node.getUsername();
            this.daemonPassword = node.getPassword();
            setDaemonAddressJ(daemonAddress);
        } else {
            this.daemonAddress = null;
            this.daemonUsername = "";
            this.daemonPassword = "";
            setDaemonAddressJ("");
        }
        activeDaemon = true;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public String addressPrefix(NetworkType networkType) {
        switch (networkType) {
            case NetworkType_Testnet:
                return "TXCA-";
            case NetworkType_Stagenet:
                return "-";
            case NetworkType_Mainnet:
            default:
                return "XCA-";
        }
    }


    public String getDaemonAddress() {
        return this.daemonAddress;
    }

    public String getDaemonUsername() {
        return daemonUsername;
    }

    public String getDaemonPassword() {
        return daemonPassword;
    }

    public boolean isActiveDaemon() {
        return activeDaemon;
    }


    public native long createWalletJ(String path, String password, String language, int networkType);

    public native int queryWalletDeviceJ(String keys_file_name, String password);


    public native long openWalletJ(String path, String password, int networkType);


    public native long recoveryWalletJ(String path, String password, String mnemonic,
                                       int networkType, long restoreHeight);

    public native boolean verifyWalletPassword(String keys_file_name, String password, boolean watch_only);

    public native long createWalletFromKeysJ(String path, String password,
                                             String language,
                                             int networkType,
                                             long restoreHeight,
                                             String addressString,
                                             String viewKeyString,
                                             String spendKeyString);

    public native long createWalletFromDeviceJ(String path, String password,
                                               int networkType,
                                               String deviceName,
                                               long restoreHeight,
                                               String subaddressLookahead);

    public native boolean closeJ(Wallet wallet);

    public native boolean walletExists(String path);

    public native void setDaemonAddressJ(String address);

    public native int getDaemonVersion();

    public native long getBlockchainHeight();

    public native long getBlockchainTargetHeight();

    public native long getNetworkDifficulty();

    public native double getMiningHashRate();

    public native long getBlockTarget();

    public native boolean isMining();

    public native boolean startMining(String address, boolean background_mining, boolean ignore_battery);

    public native boolean stopMining();

    public native String resolveOpenAlias(String address, boolean dnssec_valid);

    static public native void initLogger(String argv0, String defaultLogBaseName);

    static public int LOGLEVEL_SILENT = -1;

    static public int LOGLEVEL_WARN = 0;

    static public int LOGLEVEL_INFO = 1;

    static public int LOGLEVEL_DEBUG = 2;

    static public int LOGLEVEL_TRACE = 3;

    static public int LOGLEVEL_MAX = 4;

    static public native void setLogLevel(int level);

    static public native void logDebug(String category, String message);

    static public native void logInfo(String category, String message);

    static public native void logWarning(String category, String message);

    static public native void logError(String category, String message);

}