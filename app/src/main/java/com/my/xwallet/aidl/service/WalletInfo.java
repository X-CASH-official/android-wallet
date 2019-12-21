/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet.aidl.service;

public class WalletInfo {
    public static final int TYPE_QUEUE_FULL_ERROR = 1;
    public static final int TYPE_BEGIN_LOAD_WALLET = 2;
    public static final int TYPE_SYNCHRONIZE_STATUS_ERROR = 3;
    public static final int TYPE_SYNCHRONIZE_STATUS_SUCCESS = 4;
    public static final int TYPE_REFRESH_BALANCE = 5;
    public static final int TYPE_BLOCK_PROGRESS = 6;
    public static final int TYPE_REFRESH_TRANSACTION = 7;
    public static final int TYPE_CLOSE_ACTIVE_WALLET = 8;

    private int walletId;
    private int type;
    private String error;
    private String balance;
    private String unlockedBalance;
    private boolean result;
    private int progress;
    private long daemonHeight;
    private long blockChainHeight;

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUnlockedBalance() {
        return unlockedBalance;
    }

    public void setUnlockedBalance(String unlockedBalance) {
        this.unlockedBalance = unlockedBalance;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getDaemonHeight() {
        return daemonHeight;
    }

    public void setDaemonHeight(long daemonHeight) {
        this.daemonHeight = daemonHeight;
    }

    public long getBlockChainHeight() {
        return blockChainHeight;
    }

    public void setBlockChainHeight(long blockChainHeight) {
        this.blockChainHeight = blockChainHeight;
    }
}
