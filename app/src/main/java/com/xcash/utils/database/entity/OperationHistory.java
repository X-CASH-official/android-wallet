/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.utils.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "operation_historys")
public class OperationHistory implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;
    @ColumnInfo
    private int walletId;
    @ColumnInfo
    private String operation;
    @ColumnInfo
    private String status;
    @ColumnInfo
    private String description;
    @ColumnInfo
    private long timestamp;

    public OperationHistory() {

    }

    @Ignore
    public OperationHistory(int walletId, String operation, String status, String description, long timestamp) {
        this.walletId = walletId;
        this.operation = operation;
        this.status = status;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OperationHistory{" +
                "id=" + id +
                ", walletId=" + walletId +
                ", operation='" + operation + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
