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
package com.xcash.utils.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xcash.utils.database.entity.TransactionInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface TransactionInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTransactionInfos(@NotNull TransactionInfo... transactionInfo);

    @Query("SELECT * FROM transaction_infos WHERE symbol = :symbol AND walletId = :walletId ORDER BY timestamp DESC")
    List<TransactionInfo> loadTransactionInfosByWalletId(@NotNull String symbol, int walletId);

    @Query("SELECT * FROM transaction_infos WHERE symbol = :symbol AND walletId = :walletId AND direction = :direction ORDER BY timestamp DESC")
    List<TransactionInfo> loadTransactionInfosByWalletIdAndDirection(@NotNull String symbol, int walletId, int direction);

    @Delete
    void deleteTransactionInfo(@NotNull TransactionInfo... transactionInfo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTransactionInfos(@NotNull TransactionInfo... transactionInfo);

}