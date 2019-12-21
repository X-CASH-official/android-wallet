/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet.aidl;


import android.os.Parcel;
import android.os.Parcelable;

public class Transaction implements Parcelable {

    private long amount;
    private long dust;
    private long fee;
    private String firstTxId;
    private long txCount;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getDust() {
        return dust;
    }

    public void setDust(long dust) {
        this.dust = dust;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public String getFirstTxId() {
        return firstTxId;
    }

    public void setFirstTxId(String firstTxId) {
        this.firstTxId = firstTxId;
    }

    public long getTxCount() {
        return txCount;
    }

    public void setTxCount(long txCount) {
        this.txCount = txCount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", dust=" + dust +
                ", fee=" + fee +
                ", firstTxId='" + firstTxId + '\'' +
                ", txCount=" + txCount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.amount);
        dest.writeLong(this.dust);
        dest.writeLong(this.fee);
        dest.writeString(this.firstTxId);
        dest.writeLong(this.txCount);
    }

    public Transaction() {
    }

    protected Transaction(Parcel in) {
        this.amount = in.readLong();
        this.dust = in.readLong();
        this.fee = in.readLong();
        this.firstTxId = in.readString();
        this.txCount = in.readLong();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}
