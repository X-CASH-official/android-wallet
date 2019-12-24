/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.utils.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.my.utils.database.entity.TransactionInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface TransactionInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTransactionInfo(@NotNull TransactionInfo... transactionInfo);

    @Query("SELECT * FROM transactionInfos WHERE symbol = :symbol AND walletId = :walletId")
    List<TransactionInfo> loadTransactionInfoByWalletId(@NotNull String symbol, int walletId);

    @Query("SELECT * FROM transactionInfos WHERE symbol = :symbol AND walletId = :walletId  AND direction = :direction")
    List<TransactionInfo> loadTransactionInfoByWalletIdAndDirection(@NotNull String symbol, int walletId, int direction);

    @Query("SELECT * FROM transactionInfos")
    List<TransactionInfo> loadAllTransactionInfo();

    @Delete
    void deleteTransactionInfo(@NotNull TransactionInfo... transactionInfo);

}