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
 */you may not use this file except in compliance with the License.
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

@Database(entities = {AddressBook.class, Node.class, OperationHistory.class, TransactionInfo.class, Wallet.class}, version = 1, exportSchema = false)
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
