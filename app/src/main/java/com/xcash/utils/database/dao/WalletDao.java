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

import com.xcash.utils.database.entity.Wallet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertWallet(@NotNull Wallet wallet);

    @Query("SELECT * FROM wallets WHERE _id = :id")
    Wallet loadWalletById(int id);

    @Query("SELECT * FROM wallets WHERE symbol = :symbol AND isActive = 1")
    Wallet loadActiveWalletBySymbol(@NotNull String symbol);

    @Query("SELECT * FROM wallets WHERE isActive = 1")
    Wallet loadActiveWallet();

    @Query("SELECT * FROM wallets WHERE symbol = :symbol ORDER BY _id")
    List<Wallet> loadWalletsBySymbol(@NotNull String symbol);

    @Query("SELECT * FROM wallets ORDER BY _id")
    List<Wallet> loadWallets();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWallets(@NotNull Wallet... wallets);

    @Delete
    void deleteWallets(@NotNull Wallet... wallets);

}