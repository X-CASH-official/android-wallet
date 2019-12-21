/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.utils.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.my.utils.database.dao.AddressBookDao;
import com.my.utils.database.dao.NodeDao;
import com.my.utils.database.dao.TransactionInfoDao;
import com.my.utils.database.dao.WalletDao;
import com.my.utils.database.entity.AddressBook;
import com.my.utils.database.entity.Node;
import com.my.utils.database.entity.TransactionInfo;
import com.my.utils.database.entity.Wallet;
import com.my.xwallet.TheApplication;

import org.jetbrains.annotations.NotNull;

@Database(entities = {AddressBook.class, Node.class, TransactionInfo.class, Wallet.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "wallet.db";

    private static AppDatabase appDatabase;

    @NotNull
    public abstract AddressBookDao addressBookDao();

    @NotNull
    public abstract NodeDao nodeDao();

    @NotNull
    public abstract TransactionInfoDao transactionInfoDao();

    @NotNull
    public abstract WalletDao walletDao();


    public static synchronized AppDatabase getInstance() {
        if (appDatabase == null) {
            appDatabase = create(TheApplication.getTheApplication());
        }
        return appDatabase;
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `transactionInfo` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `symbol` TEXT NOT NULL, `walletId` INTEGER NOT NULL, `direction` INTEGER NOT NULL, `isPending` INTEGER NOT NULL, `isFailed` INTEGER NOT NULL, `amount` TEXT, `fee` TEXT, `blockHeight` INTEGER NOT NULL, `confirmations` INTEGER NOT NULL, `hash` TEXT, `timestamp` INTEGER NOT NULL, `paymentId` TEXT, `txKey` TEXT, `address` TEXT)");
        }
    };


    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME).
                addMigrations(new Migration[]{(Migration) AppDatabase.MIGRATION_1_2}).build();
    }

}
