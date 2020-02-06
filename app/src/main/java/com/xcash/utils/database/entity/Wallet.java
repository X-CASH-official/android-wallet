/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.utils.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "wallets", indices = {@Index(value = {"symbol", "name"},unique = true)})
public class Wallet implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;
    @ColumnInfo
    private String symbol;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String address;
    @ColumnInfo
    private String balance;
    @ColumnInfo
    private String unlockedBalance;
    @ColumnInfo
    private String passwordPrompt;
    @ColumnInfo
    private long restoreHeight;
    @ColumnInfo
    private boolean isActive;
    @ColumnInfo
    private boolean isReadOnly;
    @Ignore
    @ColumnInfo
    private String seed;
    @Ignore
    @ColumnInfo
    private String secretViewKey;
    @Ignore
    @ColumnInfo
    private String secretSpendKey;
    @Ignore
    @ColumnInfo
    private String errorString;

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

    public String getUnlockedBalance() {
        return unlockedBalance;
    }

    public void setUnlockedBalance(String unlockedBalance) {
        this.unlockedBalance = unlockedBalance;
    }

    public String getPasswordPrompt() {
        return passwordPrompt;
    }

    public void setPasswordPrompt(String passwordPrompt) {
        this.passwordPrompt = passwordPrompt;
    }

    public long getRestoreHeight() {
        return restoreHeight;
    }

    public void setRestoreHeight(long restoreHeight) {
        this.restoreHeight = restoreHeight;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
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

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", balance='" + balance + '\'' +
                ", unlockedBalance='" + unlockedBalance + '\'' +
                ", passwordPrompt='" + passwordPrompt + '\'' +
                ", restoreHeight=" + restoreHeight +
                ", isActive=" + isActive +
                ", isReadOnly=" + isReadOnly +
                ", seed='" + seed + '\'' +
                ", secretViewKey='" + secretViewKey + '\'' +
                ", secretSpendKey='" + secretSpendKey + '\'' +
                ", errorString='" + errorString + '\'' +
                '}';
    }

}
