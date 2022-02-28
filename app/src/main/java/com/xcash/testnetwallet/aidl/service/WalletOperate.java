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


import com.my.monero.model.PendingTransaction;
import com.xcash.utils.database.entity.Node;
import com.xcash.wallet.aidl.OnCreateTransactionListener;
import com.xcash.wallet.aidl.OnNormalListener;
import com.xcash.wallet.aidl.OnWalletDataListener;

public class WalletOperate implements Comparable<WalletOperate> {

    public static final int TYPE_CREATE_WALLET = 1;
    public static final int TYPE_IMPORT_WALLET_MNEMONIC = 2;
    public static final int TYPE_IMPORT_WALLET_KEYS = 3;
    public static final int TYPE_CHECK_WALLET_PASSWORD = 4;
    public static final int TYPE_LOAD_REFRESH_WALLET = 5;
    public static final int TYPE_SET_DAEMON_WALLET = 6;
    public static final int TYPE_CREATE_TRANSACTION = 7;
    public static final int TYPE_SEND_TRANSACTION = 8;
    public static final int TYPE_CLOSE_ACTIVE_WALLET = 9;
    public static final int TYPE_CLOSE_WALLET = 10;
    public static final int TYPE_GET_WALLET_DATA = 11;
    public static final int TYPE_VOTE = 12;
    public static final int TYPE_DELEGATE_REGISTER = 13;
    public static final int TYPE_DELEGATE_UPDATE = 14;
    public static final int TYPE_DELEGATE_REMOVE = 15;

    private boolean cancel;
    private String key;
    private String tag;
    private int type;
    private int id;
    private String name;
    private String password;
    private String passwordPrompt;
    private String mnemonic;
    private String addressKey;
    private String privateViewKey;
    private String privateSpendKey;
    private long restoreHeight;
    private Node node;
    private String walletAddress;
    private String amount;
    private String ringSize;
    private String paymentId;
    private String description;
    private PendingTransaction.Priority pendingPriority;
    private boolean publicTransaction;
    private String item;
    private String value;
    private String delegate_name;
    private String delegate_IP_address;
    private String block_verifier_messages_public_key;
    private int priority;

    private OnWalletDataListener onWalletDataListener;

    private OnNormalListener onNormalListener;

    private OnCreateTransactionListener onCreateTransactionListener;

    private int transactionInfoSize;

    private boolean havePendingTransaction;

    private long updateTransactionHeight;

    private long maxDaemonBlockChainHeight;

    private boolean isFullSynchronizeOnce;

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordPrompt() {
        return passwordPrompt;
    }

    public void setPasswordPrompt(String passwordPrompt) {
        this.passwordPrompt = passwordPrompt;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getAddressKey() {
        return addressKey;
    }

    public void setAddressKey(String addressKey) {
        this.addressKey = addressKey;
    }

    public String getPrivateViewKey() {
        return privateViewKey;
    }

    public void setPrivateViewKey(String privateViewKey) {
        this.privateViewKey = privateViewKey;
    }

    public String getPrivateSpendKey() {
        return privateSpendKey;
    }

    public void setPrivateSpendKey(String privateSpendKey) {
        this.privateSpendKey = privateSpendKey;
    }

    public long getRestoreHeight() {
        return restoreHeight;
    }

    public void setRestoreHeight(long restoreHeight) {
        this.restoreHeight = restoreHeight;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRingSize() {
        return ringSize;
    }

    public void setRingSize(String ringSize) {
        this.ringSize = ringSize;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PendingTransaction.Priority getPendingPriority() {
        return pendingPriority;
    }

    public void setPendingPriority(PendingTransaction.Priority pendingPriority) {
        this.pendingPriority = pendingPriority;
    }

    public boolean isPublicTransaction() {
        return publicTransaction;
    }

    public void setPublicTransaction(boolean publicTransaction) {
        this.publicTransaction = publicTransaction;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDelegate_name() {
        return delegate_name;
    }

    public void setDelegate_name(String delegate_name) {
        this.delegate_name = delegate_name;
    }

    public String getDelegate_IP_address() {
        return delegate_IP_address;
    }

    public void setDelegate_IP_address(String delegate_IP_address) {
        this.delegate_IP_address = delegate_IP_address;
    }

    public String getBlock_verifier_messages_public_key() {
        return block_verifier_messages_public_key;
    }

    public void setBlock_verifier_messages_public_key(String block_verifier_messages_public_key) {
        this.block_verifier_messages_public_key = block_verifier_messages_public_key;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public OnWalletDataListener getOnWalletDataListener() {
        return onWalletDataListener;
    }

    public void setOnWalletDataListener(OnWalletDataListener onWalletDataListener) {
        this.onWalletDataListener = onWalletDataListener;
    }

    public OnNormalListener getOnNormalListener() {
        return onNormalListener;
    }

    public void setOnNormalListener(OnNormalListener onNormalListener) {
        this.onNormalListener = onNormalListener;
    }

    public OnCreateTransactionListener getOnCreateTransactionListener() {
        return onCreateTransactionListener;
    }

    public void setOnCreateTransactionListener(OnCreateTransactionListener onCreateTransactionListener) {
        this.onCreateTransactionListener = onCreateTransactionListener;
    }

    public int getTransactionInfoSize() {
        return transactionInfoSize;
    }

    public void setTransactionInfoSize(int transactionInfoSize) {
        this.transactionInfoSize = transactionInfoSize;
    }

    public boolean isHavePendingTransaction() {
        return havePendingTransaction;
    }

    public void setHavePendingTransaction(boolean havePendingTransaction) {
        this.havePendingTransaction = havePendingTransaction;
    }

    public long getUpdateTransactionHeight() {
        return updateTransactionHeight;
    }

    public void setUpdateTransactionHeight(long updateTransactionHeight) {
        this.updateTransactionHeight = updateTransactionHeight;
    }

    public long getMaxDaemonBlockChainHeight() {
        return maxDaemonBlockChainHeight;
    }

    public void setMaxDaemonBlockChainHeight(long maxDaemonBlockChainHeight) {
        this.maxDaemonBlockChainHeight = maxDaemonBlockChainHeight;
    }

    public boolean isFullSynchronizeOnce() {
        return isFullSynchronizeOnce;
    }

    public void setFullSynchronizeOnce(boolean fullSynchronizeOnce) {
        isFullSynchronizeOnce = fullSynchronizeOnce;
    }

    @Override
    public int compareTo(WalletOperate walletOperate) {
        int result = 0;
        if (walletOperate == null) {
            return result;
        }
        return this.priority > walletOperate.priority ? -1 : 1;
    }

    @Override
    public String toString() {
        return "WalletOperate{" +
                "cancel=" + cancel +
                ", key='" + key + '\'' +
                ", tag='" + tag + '\'' +
                ", type=" + type +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", passwordPrompt='" + passwordPrompt + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                ", addressKey='" + addressKey + '\'' +
                ", privateViewKey='" + privateViewKey + '\'' +
                ", privateSpendKey='" + privateSpendKey + '\'' +
                ", restoreHeight=" + restoreHeight +
                ", node=" + node +
                ", walletAddress='" + walletAddress + '\'' +
                ", amount='" + amount + '\'' +
                ", ringSize='" + ringSize + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", description='" + description + '\'' +
                ", pendingPriority=" + pendingPriority +
                ", publicTransaction=" + publicTransaction +
                ", item='" + item + '\'' +
                ", value='" + value + '\'' +
                ", delegate_name='" + delegate_name + '\'' +
                ", delegate_IP_address='" + delegate_IP_address + '\'' +
                ", block_verifier_messages_public_key='" + block_verifier_messages_public_key + '\'' +
                ", priority=" + priority +
                ", onWalletDataListener=" + onWalletDataListener +
                ", onNormalListener=" + onNormalListener +
                ", onCreateTransactionListener=" + onCreateTransactionListener +
                ", transactionInfoSize=" + transactionInfoSize +
                ", havePendingTransaction=" + havePendingTransaction +
                ", updateTransactionHeight=" + updateTransactionHeight +
                ", maxDaemonBlockChainHeight=" + maxDaemonBlockChainHeight +
                ", isFullSynchronizeOnce=" + isFullSynchronizeOnce +
                '}';
    }
}