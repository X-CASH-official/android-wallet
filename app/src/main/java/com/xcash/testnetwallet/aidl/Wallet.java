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
package com.xcash.wallet.aidl;


import android.os.Parcel;
import android.os.Parcelable;

public class Wallet implements Parcelable {

    private int id;
    private String symbol;
    private String name;
    private String address;
    private String balance;
    private long restoreHeight;
    private String seed;
    private String secretViewKey;
    private String secretSpendKey;
    private String publicViewKey;
    private String publicSpendKey;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public long getRestoreHeight() {
        return restoreHeight;
    }

    public void setRestoreHeight(long restoreHeight) {
        this.restoreHeight = restoreHeight;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getSecretViewKey() {
        return secretViewKey;
    }

    public void setSecretViewKey(String secretViewKey) {
        this.secretViewKey = secretViewKey;
    }

    public String getSecretSpendKey() {
        return secretSpendKey;
    }

    public void setSecretSpendKey(String secretSpendKey) {
        this.secretSpendKey = secretSpendKey;
    }

    public String getPublicViewKey() {
        return publicViewKey;
    }

    public void setPublicViewKey(String publicViewKey) {
        this.publicViewKey = publicViewKey;
    }

    public String getPublicSpendKey() {
        return publicSpendKey;
    }

    public void setPublicSpendKey(String publicSpendKey) {
        this.publicSpendKey = publicSpendKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.symbol);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.balance);
        dest.writeLong(this.restoreHeight);
        dest.writeString(this.seed);
        dest.writeString(this.secretViewKey);
        dest.writeString(this.secretSpendKey);
        dest.writeString(this.publicViewKey);
        dest.writeString(this.publicSpendKey);
    }

    public Wallet() {

    }

    protected Wallet(Parcel in) {
        readFromParcel(in);
    }

    protected void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.symbol = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.balance = in.readString();
        this.restoreHeight = in.readLong();
        this.seed = in.readString();
        this.secretViewKey = in.readString();
        this.secretSpendKey = in.readString();
        this.publicViewKey = in.readString();
        this.publicSpendKey = in.readString();
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", balance='" + balance + '\'' +
                ", restoreHeight=" + restoreHeight +
                ", seed='" + seed + '\'' +
                ", secretViewKey='" + secretViewKey + '\'' +
                ", secretSpendKey='" + secretSpendKey + '\'' +
                ", publicViewKey='" + publicViewKey + '\'' +
                ", publicSpendKey='" + publicSpendKey + '\'' +
                '}';
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel source) {
            return new Wallet(source);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };

}
