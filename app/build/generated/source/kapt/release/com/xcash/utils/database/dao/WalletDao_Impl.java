package com.xcash.utils.database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.xcash.utils.database.entity.Wallet;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class WalletDao_Impl implements WalletDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Wallet> __insertionAdapterOfWallet;

  private final EntityDeletionOrUpdateAdapter<Wallet> __deletionAdapterOfWallet;

  private final EntityDeletionOrUpdateAdapter<Wallet> __updateAdapterOfWallet;

  public WalletDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWallet = new EntityInsertionAdapter<Wallet>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `wallets` (`_id`,`symbol`,`name`,`address`,`balance`,`unlockedBalance`,`passwordPrompt`,`restoreHeight`,`isActive`,`isReadOnly`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Wallet value) {
        stmt.bindLong(1, value.getId());
        if (value.getSymbol() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSymbol());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        if (value.getAddress() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getAddress());
        }
        if (value.getBalance() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getBalance());
        }
        if (value.getUnlockedBalance() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getUnlockedBalance());
        }
        if (value.getPasswordPrompt() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getPasswordPrompt());
        }
        stmt.bindLong(8, value.getRestoreHeight());
        final int _tmp;
        _tmp = value.isActive() ? 1 : 0;
        stmt.bindLong(9, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isReadOnly() ? 1 : 0;
        stmt.bindLong(10, _tmp_1);
      }
    };
    this.__deletionAdapterOfWallet = new EntityDeletionOrUpdateAdapter<Wallet>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `wallets` WHERE `_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Wallet value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfWallet = new EntityDeletionOrUpdateAdapter<Wallet>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `wallets` SET `_id` = ?,`symbol` = ?,`name` = ?,`address` = ?,`balance` = ?,`unlockedBalance` = ?,`passwordPrompt` = ?,`restoreHeight` = ?,`isActive` = ?,`isReadOnly` = ? WHERE `_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Wallet value) {
        stmt.bindLong(1, value.getId());
        if (value.getSymbol() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSymbol());
        }
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        if (value.getAddress() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getAddress());
        }
        if (value.getBalance() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getBalance());
        }
        if (value.getUnlockedBalance() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getUnlockedBalance());
        }
        if (value.getPasswordPrompt() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getPasswordPrompt());
        }
        stmt.bindLong(8, value.getRestoreHeight());
        final int _tmp;
        _tmp = value.isActive() ? 1 : 0;
        stmt.bindLong(9, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isReadOnly() ? 1 : 0;
        stmt.bindLong(10, _tmp_1);
        stmt.bindLong(11, value.getId());
      }
    };
  }

  @Override
  public void insertWallet(final Wallet wallet) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfWallet.insert(wallet);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteWallets(final Wallet... wallets) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfWallet.handleMultiple(wallets);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateWallets(final Wallet... wallets) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfWallet.handleMultiple(wallets);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Wallet loadWalletById(final int id) {
    final String _sql = "SELECT * FROM wallets WHERE _id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
      final int _cursorIndexOfUnlockedBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "unlockedBalance");
      final int _cursorIndexOfPasswordPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordPrompt");
      final int _cursorIndexOfRestoreHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "restoreHeight");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfIsReadOnly = CursorUtil.getColumnIndexOrThrow(_cursor, "isReadOnly");
      final Wallet _result;
      if(_cursor.moveToFirst()) {
        _result = new Wallet();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpSymbol;
        _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
        _result.setSymbol(_tmpSymbol);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _result.setName(_tmpName);
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        _result.setAddress(_tmpAddress);
        final String _tmpBalance;
        _tmpBalance = _cursor.getString(_cursorIndexOfBalance);
        _result.setBalance(_tmpBalance);
        final String _tmpUnlockedBalance;
        _tmpUnlockedBalance = _cursor.getString(_cursorIndexOfUnlockedBalance);
        _result.setUnlockedBalance(_tmpUnlockedBalance);
        final String _tmpPasswordPrompt;
        _tmpPasswordPrompt = _cursor.getString(_cursorIndexOfPasswordPrompt);
        _result.setPasswordPrompt(_tmpPasswordPrompt);
        final long _tmpRestoreHeight;
        _tmpRestoreHeight = _cursor.getLong(_cursorIndexOfRestoreHeight);
        _result.setRestoreHeight(_tmpRestoreHeight);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _result.setActive(_tmpIsActive);
        final boolean _tmpIsReadOnly;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsReadOnly);
        _tmpIsReadOnly = _tmp_1 != 0;
        _result.setReadOnly(_tmpIsReadOnly);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Wallet loadActiveWalletBySymbol(final String symbol) {
    final String _sql = "SELECT * FROM wallets WHERE symbol = ? AND isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (symbol == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, symbol);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
      final int _cursorIndexOfUnlockedBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "unlockedBalance");
      final int _cursorIndexOfPasswordPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordPrompt");
      final int _cursorIndexOfRestoreHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "restoreHeight");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfIsReadOnly = CursorUtil.getColumnIndexOrThrow(_cursor, "isReadOnly");
      final Wallet _result;
      if(_cursor.moveToFirst()) {
        _result = new Wallet();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpSymbol;
        _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
        _result.setSymbol(_tmpSymbol);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _result.setName(_tmpName);
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        _result.setAddress(_tmpAddress);
        final String _tmpBalance;
        _tmpBalance = _cursor.getString(_cursorIndexOfBalance);
        _result.setBalance(_tmpBalance);
        final String _tmpUnlockedBalance;
        _tmpUnlockedBalance = _cursor.getString(_cursorIndexOfUnlockedBalance);
        _result.setUnlockedBalance(_tmpUnlockedBalance);
        final String _tmpPasswordPrompt;
        _tmpPasswordPrompt = _cursor.getString(_cursorIndexOfPasswordPrompt);
        _result.setPasswordPrompt(_tmpPasswordPrompt);
        final long _tmpRestoreHeight;
        _tmpRestoreHeight = _cursor.getLong(_cursorIndexOfRestoreHeight);
        _result.setRestoreHeight(_tmpRestoreHeight);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _result.setActive(_tmpIsActive);
        final boolean _tmpIsReadOnly;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsReadOnly);
        _tmpIsReadOnly = _tmp_1 != 0;
        _result.setReadOnly(_tmpIsReadOnly);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Wallet loadActiveWallet() {
    final String _sql = "SELECT * FROM wallets WHERE isActive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
      final int _cursorIndexOfUnlockedBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "unlockedBalance");
      final int _cursorIndexOfPasswordPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordPrompt");
      final int _cursorIndexOfRestoreHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "restoreHeight");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfIsReadOnly = CursorUtil.getColumnIndexOrThrow(_cursor, "isReadOnly");
      final Wallet _result;
      if(_cursor.moveToFirst()) {
        _result = new Wallet();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpSymbol;
        _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
        _result.setSymbol(_tmpSymbol);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _result.setName(_tmpName);
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        _result.setAddress(_tmpAddress);
        final String _tmpBalance;
        _tmpBalance = _cursor.getString(_cursorIndexOfBalance);
        _result.setBalance(_tmpBalance);
        final String _tmpUnlockedBalance;
        _tmpUnlockedBalance = _cursor.getString(_cursorIndexOfUnlockedBalance);
        _result.setUnlockedBalance(_tmpUnlockedBalance);
        final String _tmpPasswordPrompt;
        _tmpPasswordPrompt = _cursor.getString(_cursorIndexOfPasswordPrompt);
        _result.setPasswordPrompt(_tmpPasswordPrompt);
        final long _tmpRestoreHeight;
        _tmpRestoreHeight = _cursor.getLong(_cursorIndexOfRestoreHeight);
        _result.setRestoreHeight(_tmpRestoreHeight);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _result.setActive(_tmpIsActive);
        final boolean _tmpIsReadOnly;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsReadOnly);
        _tmpIsReadOnly = _tmp_1 != 0;
        _result.setReadOnly(_tmpIsReadOnly);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Wallet> loadWalletsBySymbol(final String symbol) {
    final String _sql = "SELECT * FROM wallets WHERE symbol = ? ORDER BY _id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (symbol == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, symbol);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
      final int _cursorIndexOfUnlockedBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "unlockedBalance");
      final int _cursorIndexOfPasswordPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordPrompt");
      final int _cursorIndexOfRestoreHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "restoreHeight");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfIsReadOnly = CursorUtil.getColumnIndexOrThrow(_cursor, "isReadOnly");
      final List<Wallet> _result = new ArrayList<Wallet>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Wallet _item;
        _item = new Wallet();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpSymbol;
        _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
        _item.setSymbol(_tmpSymbol);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        _item.setAddress(_tmpAddress);
        final String _tmpBalance;
        _tmpBalance = _cursor.getString(_cursorIndexOfBalance);
        _item.setBalance(_tmpBalance);
        final String _tmpUnlockedBalance;
        _tmpUnlockedBalance = _cursor.getString(_cursorIndexOfUnlockedBalance);
        _item.setUnlockedBalance(_tmpUnlockedBalance);
        final String _tmpPasswordPrompt;
        _tmpPasswordPrompt = _cursor.getString(_cursorIndexOfPasswordPrompt);
        _item.setPasswordPrompt(_tmpPasswordPrompt);
        final long _tmpRestoreHeight;
        _tmpRestoreHeight = _cursor.getLong(_cursorIndexOfRestoreHeight);
        _item.setRestoreHeight(_tmpRestoreHeight);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _item.setActive(_tmpIsActive);
        final boolean _tmpIsReadOnly;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsReadOnly);
        _tmpIsReadOnly = _tmp_1 != 0;
        _item.setReadOnly(_tmpIsReadOnly);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Wallet> loadWallets() {
    final String _sql = "SELECT * FROM wallets ORDER BY _id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
      final int _cursorIndexOfUnlockedBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "unlockedBalance");
      final int _cursorIndexOfPasswordPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordPrompt");
      final int _cursorIndexOfRestoreHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "restoreHeight");
      final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
      final int _cursorIndexOfIsReadOnly = CursorUtil.getColumnIndexOrThrow(_cursor, "isReadOnly");
      final List<Wallet> _result = new ArrayList<Wallet>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Wallet _item;
        _item = new Wallet();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpSymbol;
        _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
        _item.setSymbol(_tmpSymbol);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        _item.setAddress(_tmpAddress);
        final String _tmpBalance;
        _tmpBalance = _cursor.getString(_cursorIndexOfBalance);
        _item.setBalance(_tmpBalance);
        final String _tmpUnlockedBalance;
        _tmpUnlockedBalance = _cursor.getString(_cursorIndexOfUnlockedBalance);
        _item.setUnlockedBalance(_tmpUnlockedBalance);
        final String _tmpPasswordPrompt;
        _tmpPasswordPrompt = _cursor.getString(_cursorIndexOfPasswordPrompt);
        _item.setPasswordPrompt(_tmpPasswordPrompt);
        final long _tmpRestoreHeight;
        _tmpRestoreHeight = _cursor.getLong(_cursorIndexOfRestoreHeight);
        _item.setRestoreHeight(_tmpRestoreHeight);
        final boolean _tmpIsActive;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsActive);
        _tmpIsActive = _tmp != 0;
        _item.setActive(_tmpIsActive);
        final boolean _tmpIsReadOnly;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsReadOnly);
        _tmpIsReadOnly = _tmp_1 != 0;
        _item.setReadOnly(_tmpIsReadOnly);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
