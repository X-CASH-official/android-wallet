package com.xcash.utils.database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.xcash.utils.database.entity.TransactionInfo;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TransactionInfoDao_Impl implements TransactionInfoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TransactionInfo> __insertionAdapterOfTransactionInfo;

  private final EntityDeletionOrUpdateAdapter<TransactionInfo> __deletionAdapterOfTransactionInfo;

  public TransactionInfoDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransactionInfo = new EntityInsertionAdapter<TransactionInfo>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `transaction_infos` (`_id`,`symbol`,`walletId`,`direction`,`isPending`,`isFailed`,`amount`,`fee`,`blockHeight`,`confirmations`,`hash`,`timestamp`,`paymentId`,`txKey`,`address`,`description`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TransactionInfo value) {
        stmt.bindLong(1, value.getId());
        if (value.getSymbol() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSymbol());
        }
        stmt.bindLong(3, value.getWalletId());
        stmt.bindLong(4, value.getDirection());
        final int _tmp;
        _tmp = value.isPending() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isFailed() ? 1 : 0;
        stmt.bindLong(6, _tmp_1);
        if (value.getAmount() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getAmount());
        }
        if (value.getFee() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getFee());
        }
        stmt.bindLong(9, value.getBlockHeight());
        stmt.bindLong(10, value.getConfirmations());
        if (value.getHash() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getHash());
        }
        stmt.bindLong(12, value.getTimestamp());
        if (value.getPaymentId() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getPaymentId());
        }
        if (value.getTxKey() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getTxKey());
        }
        if (value.getAddress() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getAddress());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getDescription());
        }
      }
    };
    this.__deletionAdapterOfTransactionInfo = new EntityDeletionOrUpdateAdapter<TransactionInfo>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `transaction_infos` WHERE `_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TransactionInfo value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public void insertTransactionInfos(final TransactionInfo... transactionInfo) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTransactionInfo.insert(transactionInfo);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteTransactionInfo(final TransactionInfo... transactionInfo) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTransactionInfo.handleMultiple(transactionInfo);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<TransactionInfo> loadTransactionInfosByWalletId(final String symbol,
      final int walletId) {
    final String _sql = "SELECT * FROM transaction_infos WHERE symbol = ? AND walletId = ? ORDER BY _id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (symbol == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, symbol);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, walletId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
      final int _cursorIndexOfWalletId = CursorUtil.getColumnIndexOrThrow(_cursor, "walletId");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfIsPending = CursorUtil.getColumnIndexOrThrow(_cursor, "isPending");
      final int _cursorIndexOfIsFailed = CursorUtil.getColumnIndexOrThrow(_cursor, "isFailed");
      final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
      final int _cursorIndexOfFee = CursorUtil.getColumnIndexOrThrow(_cursor, "fee");
      final int _cursorIndexOfBlockHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "blockHeight");
      final int _cursorIndexOfConfirmations = CursorUtil.getColumnIndexOrThrow(_cursor, "confirmations");
      final int _cursorIndexOfHash = CursorUtil.getColumnIndexOrThrow(_cursor, "hash");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentId");
      final int _cursorIndexOfTxKey = CursorUtil.getColumnIndexOrThrow(_cursor, "txKey");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final List<TransactionInfo> _result = new ArrayList<TransactionInfo>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TransactionInfo _item;
        _item = new TransactionInfo();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpSymbol;
        _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
        _item.setSymbol(_tmpSymbol);
        final int _tmpWalletId;
        _tmpWalletId = _cursor.getInt(_cursorIndexOfWalletId);
        _item.setWalletId(_tmpWalletId);
        final int _tmpDirection;
        _tmpDirection = _cursor.getInt(_cursorIndexOfDirection);
        _item.setDirection(_tmpDirection);
        final boolean _tmpIsPending;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsPending);
        _tmpIsPending = _tmp != 0;
        _item.setPending(_tmpIsPending);
        final boolean _tmpIsFailed;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsFailed);
        _tmpIsFailed = _tmp_1 != 0;
        _item.setFailed(_tmpIsFailed);
        final String _tmpAmount;
        _tmpAmount = _cursor.getString(_cursorIndexOfAmount);
        _item.setAmount(_tmpAmount);
        final String _tmpFee;
        _tmpFee = _cursor.getString(_cursorIndexOfFee);
        _item.setFee(_tmpFee);
        final long _tmpBlockHeight;
        _tmpBlockHeight = _cursor.getLong(_cursorIndexOfBlockHeight);
        _item.setBlockHeight(_tmpBlockHeight);
        final long _tmpConfirmations;
        _tmpConfirmations = _cursor.getLong(_cursorIndexOfConfirmations);
        _item.setConfirmations(_tmpConfirmations);
        final String _tmpHash;
        _tmpHash = _cursor.getString(_cursorIndexOfHash);
        _item.setHash(_tmpHash);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        final String _tmpPaymentId;
        _tmpPaymentId = _cursor.getString(_cursorIndexOfPaymentId);
        _item.setPaymentId(_tmpPaymentId);
        final String _tmpTxKey;
        _tmpTxKey = _cursor.getString(_cursorIndexOfTxKey);
        _item.setTxKey(_tmpTxKey);
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        _item.setAddress(_tmpAddress);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        _item.setDescription(_tmpDescription);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TransactionInfo> loadTransactionInfosByWalletIdAndDirection(final String symbol,
      final int walletId, final int direction) {
    final String _sql = "SELECT * FROM transaction_infos WHERE symbol = ? AND walletId = ? AND direction = ? ORDER BY _id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (symbol == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, symbol);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, walletId);
    _argIndex = 3;
    _statement.bindLong(_argIndex, direction);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
      final int _cursorIndexOfWalletId = CursorUtil.getColumnIndexOrThrow(_cursor, "walletId");
      final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
      final int _cursorIndexOfIsPending = CursorUtil.getColumnIndexOrThrow(_cursor, "isPending");
      final int _cursorIndexOfIsFailed = CursorUtil.getColumnIndexOrThrow(_cursor, "isFailed");
      final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
      final int _cursorIndexOfFee = CursorUtil.getColumnIndexOrThrow(_cursor, "fee");
      final int _cursorIndexOfBlockHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "blockHeight");
      final int _cursorIndexOfConfirmations = CursorUtil.getColumnIndexOrThrow(_cursor, "confirmations");
      final int _cursorIndexOfHash = CursorUtil.getColumnIndexOrThrow(_cursor, "hash");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentId");
      final int _cursorIndexOfTxKey = CursorUtil.getColumnIndexOrThrow(_cursor, "txKey");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final List<TransactionInfo> _result = new ArrayList<TransactionInfo>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TransactionInfo _item;
        _item = new TransactionInfo();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpSymbol;
        _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
        _item.setSymbol(_tmpSymbol);
        final int _tmpWalletId;
        _tmpWalletId = _cursor.getInt(_cursorIndexOfWalletId);
        _item.setWalletId(_tmpWalletId);
        final int _tmpDirection;
        _tmpDirection = _cursor.getInt(_cursorIndexOfDirection);
        _item.setDirection(_tmpDirection);
        final boolean _tmpIsPending;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsPending);
        _tmpIsPending = _tmp != 0;
        _item.setPending(_tmpIsPending);
        final boolean _tmpIsFailed;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfIsFailed);
        _tmpIsFailed = _tmp_1 != 0;
        _item.setFailed(_tmpIsFailed);
        final String _tmpAmount;
        _tmpAmount = _cursor.getString(_cursorIndexOfAmount);
        _item.setAmount(_tmpAmount);
        final String _tmpFee;
        _tmpFee = _cursor.getString(_cursorIndexOfFee);
        _item.setFee(_tmpFee);
        final long _tmpBlockHeight;
        _tmpBlockHeight = _cursor.getLong(_cursorIndexOfBlockHeight);
        _item.setBlockHeight(_tmpBlockHeight);
        final long _tmpConfirmations;
        _tmpConfirmations = _cursor.getLong(_cursorIndexOfConfirmations);
        _item.setConfirmations(_tmpConfirmations);
        final String _tmpHash;
        _tmpHash = _cursor.getString(_cursorIndexOfHash);
        _item.setHash(_tmpHash);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        final String _tmpPaymentId;
        _tmpPaymentId = _cursor.getString(_cursorIndexOfPaymentId);
        _item.setPaymentId(_tmpPaymentId);
        final String _tmpTxKey;
        _tmpTxKey = _cursor.getString(_cursorIndexOfTxKey);
        _item.setTxKey(_tmpTxKey);
        final String _tmpAddress;
        _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        _item.setAddress(_tmpAddress);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        _item.setDescription(_tmpDescription);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
