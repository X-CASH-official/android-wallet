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