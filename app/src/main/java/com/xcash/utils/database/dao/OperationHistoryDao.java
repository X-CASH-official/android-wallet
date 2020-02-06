/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.utils.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.xcash.utils.database.entity.OperationHistory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface OperationHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOperationHistorys(@NotNull OperationHistory... operationHistorys);

    @Query("SELECT * FROM operation_historys WHERE walletId = :walletId ORDER BY _id DESC")
    List<OperationHistory> loadOperationHistorysByWalletId(int walletId);

}