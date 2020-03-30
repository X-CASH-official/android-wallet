package com.xcash.utils.database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.xcash.utils.database.entity.AddressBook;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AddressBookDao_Impl implements AddressBookDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AddressBook> __insertionAdapterOfAddressBook;

  private final EntityDeletionOrUpdateAdapter<AddressBook> __deletionAdapterOfAddressBook;

  public AddressBookDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAddressBook = new EntityInsertionAdapter<AddressBook>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `address_books` (`_id`,`symbol`,`address`,`notes`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, AddressBook value) {
        stmt.bindLong(1, value.getId());
        if (value.getSymbol() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSymbol());
        }
        if (value.getAddress() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getAddress());
        }
        if (value.getNotes() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getNotes());
        }
      }
    };
    this.__deletionAdapterOfAddressBook = new EntityDeletionOrUpdateAdapter<AddressBook>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `address_books` WHERE `_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, AddressBook value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public void insertAddressBooks(final AddressBook... addressBook) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfAddressBook.insert(addressBook);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAddressBook(final AddressBook AddressBook) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfAddressBook.handle(AddressBook);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<AddressBook> loadAddressBooks() {
    final String _sql = "SELECT * FROM address_books ORDER BY _id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final List<AddressBook> _result = new ArrayList<AddressBook>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final AddressBook _item;
        _item = new AddressBook();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpSymbol;
        _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
        _item.setSymbol(_tmpSymbol);
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        _item.setAddress(_tmpAddress);
        final String _tmpNotes;
        _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        _item.setNotes(_tmpNotes);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
