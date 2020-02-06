/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.utils.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.xcash.utils.database.entity.TransactionInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface TransactionInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTransactionInfos(@NotNull TransactionInfo... transactionInfo);

    @Query("SELECT * FROM transactionInfos WHERE symbol = :symbol AND walletId = :walletId ORDER BY _id")
    List<TransactionInfo> loadTransactionInfosByWalletId(@NotNull String symbol, int walletId);

    @Query("SELECT * FROM transactionInfos WHERE symbol = :symbol AND walletId = :walletId AND direction = :direction ORDER BY _id")
    List<TransactionInfo> loadTransactionInfosByWalletIdAndDirection(@NotNull String symbol, int walletId, int direction);

    @Delete
    void deleteTransactionInfo(@NotNull TransactionInfo... transactionInfo);

}