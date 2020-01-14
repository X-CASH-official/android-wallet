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

@Database(entities = {AddressBook.class, Node.class, TransactionInfo.class, Wallet.class}, version = 3, exportSchema = false)
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

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            try {
                database.execSQL("ALTER TABLE nodes ADD COLUMN username TEXT");
                database.execSQL("ALTER TABLE nodes ADD COLUMN password TEXT");
                database.execSQL("DELETE FROM nodes");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private static AppDatabase create(final Context context) {
//        return Room.databaseBuilder(
//                context,
//                AppDatabase.class,
//                DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME).allowMainThreadQueries().
                addMigrations(new Migration[]{AppDatabase.MIGRATION_2_3}).build();
    }

}
