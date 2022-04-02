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
package com.xcash.testnetwallet.aidl.service;

public class WalletInfo {

    public static final int TYPE_QUEUE_FULL_ERROR = 1;
    public static final int TYPE_BEGIN_LOAD_WALLET = 2;
    public static final int TYPE_SYNCHRONIZE_STATUS_ERROR = 3;
    public static final int TYPE_SYNCHRONIZE_STATUS_SUCCESS = 4;
    public static final int TYPE_REFRESH_BALANCE = 5;
    public static final int TYPE_BLOCK_PROGRESS = 6;
    public static final int TYPE_REFRESH_TRANSACTION = 7;
    public static final int TYPE_CLOSE_ACTIVE_WALLET = 8;
    public static final int TYPE_MONEY_SPENT = 9;
    public static final int TYPE_MONEY_RECEIVE = 10;
    public static final int TYPE_UNCONFIRMED_MONEY_RECEIVE = 11;

    private int walletId;
    private int type;
    private String error;
    private String balance;
    private String unlockedBalance;
    private boolean result;
    private int progress;
    private long daemonHeight;
    private long blockChainHeight;
    private String txId;
    private long amount;
    private boolean fullSynchronizeOnce;

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

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public boolean isFullSynchronizeOnce() {
        return fullSynchronizeOnce;
    }

    public void setFullSynchronizeOnce(boolean fullSynchronizeOnce) {
        this.fullSynchronizeOnce = fullSynchronizeOnce;
    }
}
