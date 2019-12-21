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

import com.my.utils.database.entity.AddressBook;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface AddressBookDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAddressBook(@NotNull AddressBook AddressBook);


    @Query("SELECT * FROM address_books ORDER BY _id DESC")
    List<AddressBook> loadAddressBooks();


    @Query("SELECT * FROM address_books WHERE symbol=:symbol ORDER BY _id DESC")
    List<AddressBook> loadAddressBooksBySymbol(@NotNull String symbol);

    @Delete
    void deleteAddressBook(@NotNull AddressBook AddressBook);
}