/*
 * Copyright (c) 2019-2020 The X-Cash Foundation
 * Copyright (c) 2017 m2049r
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

package com.my.monero.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.my.monero.model.PendingTransaction;
import com.my.monero.util.UserNotes;


// https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
public class TxData implements Parcelable {

    private String dstAddr;
    private String paymentId;
    private long amount;
    private int mixin;
    private PendingTransaction.Priority priority;
    private boolean publicTransaction;

    private UserNotes userNotes;

    public TxData() {

    }

    public TxData(TxData txData) {
        this.dstAddr = txData.dstAddr;
        this.paymentId = txData.paymentId;
        this.amount = txData.amount;
        this.mixin = txData.mixin;
        this.priority = txData.priority;
        this.publicTransaction = txData.publicTransaction;
    }

    public TxData(String dstAddr,
                  String paymentId,
                  long amount,
                  int mixin,
                  PendingTransaction.Priority priority, boolean publicTransaction) {
        this.dstAddr = dstAddr;
        this.paymentId = paymentId;
        this.amount = amount;
        this.mixin = mixin;
        this.priority = priority;
        this.publicTransaction = publicTransaction;
    }

    public String getDstAddr() {
        return dstAddr;
    }

    public void setDstAddr(String dstAddr) {
        this.dstAddr = dstAddr;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getMixin() {
        return mixin;
    }

    public void setMixin(int mixin) {
        this.mixin = mixin;
    }

    public PendingTransaction.Priority getPriority() {
        return priority;
    }

    public void setPriority(PendingTransaction.Priority priority) {
        this.priority = priority;
    }

    public boolean isPublicTransaction() {
        return publicTransaction;
    }

    public void setPublicTransaction(boolean publicTransaction) {
        this.publicTransaction = publicTransaction;
    }

    public UserNotes getUserNotes() {
        return userNotes;
    }

    public void setUserNotes(UserNotes userNotes) {
        this.userNotes = userNotes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dstAddr);
        dest.writeString(this.paymentId);
        dest.writeLong(this.amount);
        dest.writeInt(this.mixin);
        dest.writeInt(this.priority == null ? -1 : this.priority.ordinal());
        dest.writeByte(this.publicTransaction ? (byte) 1 : (byte) 0);
    }

    protected TxData(Parcel in) {
        this.dstAddr = in.readString();
        this.paymentId = in.readString();
        this.amount = in.readLong();
        this.mixin = in.readInt();
        int tmpPriority = in.readInt();
        this.priority = tmpPriority == -1 ? null : PendingTransaction.Priority.values()[tmpPriority];
        this.publicTransaction = in.readByte() != 0;
    }

    @Override
    public String toString() {
        return "TxData{" +
                "dstAddr='" + dstAddr + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", amount=" + amount +
                ", mixin=" + mixin +
                ", priority=" + priority +
                ", publicTransaction=" + publicTransaction +
                ", userNotes=" + userNotes +
                '}';
    }

    public static final Creator<TxData> CREATOR = new Creator<TxData>() {
        @Override
        public TxData createFromParcel(Parcel source) {
            return new TxData(source);
        }

        @Override
        public TxData[] newArray(int size) {
            return new TxData[size];
        }
    };

}
