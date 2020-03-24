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

import com.xcash.utils.database.entity.Node;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface NodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNodes(@NotNull Node... nodes);

    @Query("SELECT * FROM nodes WHERE symbol = :symbol AND isActive = 1")
    Node loadActiveNodeBySymbol(@NotNull String symbol);

    @Query("SELECT * FROM nodes WHERE symbol = :symbol ORDER BY _id")
    List<Node> loadNodesBySymbol(@NotNull String symbol);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNodes(@NotNull Node... nodes);

    @Delete
    void deleteNode(@NotNull Node node);

}