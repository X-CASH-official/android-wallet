/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.utils.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.xcash.utils.database.entity.AddressBook;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface AddressBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAddressBooks(@NotNull AddressBook... addressBook);

    @Query("SELECT * FROM address_books ORDER BY _id DESC")
    List<AddressBook> loadAddressBooks();

    @Delete
    void deleteAddressBook(@NotNull AddressBook AddressBook);

}