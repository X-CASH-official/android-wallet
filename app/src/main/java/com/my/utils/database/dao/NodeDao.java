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

import com.my.utils.database.entity.Node;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface NodeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNodes(@NotNull Node... nodes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNodesReplace(@NotNull Node... nodes);

    @Query("SELECT * FROM nodes WHERE symbol = :symbol AND isActive = 1")
    Node loadActiveNodeBySymbol(@NotNull String symbol);

    @Query("SELECT * FROM nodes WHERE symbol = :symbol ORDER BY _id")
    List<Node> loadNodesBySymbol(@NotNull String symbol);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNodes(@NotNull Node... nodes);

    @Delete
    void deleteNode(@NotNull Node node);

}