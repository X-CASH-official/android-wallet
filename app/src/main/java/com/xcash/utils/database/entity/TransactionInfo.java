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
package com.xcash.utils.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "transaction_infos")
public class TransactionInfo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;
    @ColumnInfo
    private String symbol;
    @ColumnInfo
    private int walletId;
    @ColumnInfo
    private int direction;
    @ColumnInfo
    private boolean isPending;
    @ColumnInfo
    private boolean isFailed;
    @ColumnInfo
    private String amount;
    @ColumnInfo
    private String fee;
    @ColumnInfo
    private long blockHeight;
    @ColumnInfo
    private long confirmations;
    @ColumnInfo
    private String hash;
    @ColumnInfo
    private long timestamp;
    @ColumnInfo
    private String paymentId;
    @ColumnInfo
    private String txKey;
    @ColumnInfo
    private String address;
    @ColumnInfo
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public void setFailed(boolean failed) {
        isFailed = failed;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public long getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(long confirmations) {
        this.confirmations = confirmations;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getTxKey() {
        return txKey;
    }

    public void setTxKey(String txKey) {
        this.txKey = txKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TransactionInfo{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", walletId=" + walletId +
                ", direction=" + direction +
                ", isPending=" + isPending +
                ", isFailed=" + isFailed +
                ", amount='" + amount + '\'' +
                ", fee='" + fee + '\'' +
                ", blockHeight=" + blockHeight +
                ", confirmations=" + confirmations +
                ", hash='" + hash + '\'' +
                ", timestamp=" + timestamp +
                ", paymentId='" + paymentId + '\'' +
                ", txKey='" + txKey + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
