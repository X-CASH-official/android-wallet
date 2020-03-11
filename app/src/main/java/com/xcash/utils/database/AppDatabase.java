/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.utils.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.xcash.utils.database.dao.AddressBookDao;
import com.xcash.utils.database.dao.NodeDao;
import com.xcash.utils.database.dao.OperationHistoryDao;
import com.xcash.utils.database.dao.TransactionInfoDao;
import com.xcash.utils.database.dao.WalletDao;
import com.xcash.utils.database.entity.AddressBook;
import com.xcash.utils.database.entity.Node;
import com.xcash.utils.database.entity.OperationHistory;
import com.xcash.utils.database.entity.TransactionInfo;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.wallet.TheApplication;

import org.jetbrains.annotations.NotNull;

@Database(entities = {AddressBook.class, Node.class, OperationHistory.class, TransactionInfo.class, Wallet.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "wallet.db";

    private static AppDatabase appDatabase;

    @NotNull
    public abstract AddressBookDao addressBookDao();

    @NotNull
    public abstract NodeDao nodeDao();

    @NotNull
    public abstract OperationHistoryDao operationHistoryDao();

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

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

}
