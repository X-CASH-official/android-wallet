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
import androidx.room.Update;

import com.my.utils.database.entity.Wallet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertWallet(@NotNull Wallet wallet);

    @Query("SELECT COUNT(*) FROM wallets")
    int walletsCount();

    @Query("SELECT COUNT(*) FROM wallets WHERE symbol = :symbol")
    int walletsCountBySymbol(@NotNull String symbol);

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