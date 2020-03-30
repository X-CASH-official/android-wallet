package com.xcash.utils.database.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.xcash.utils.database.entity.OperationHistory;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class OperationHistoryDao_Impl implements OperationHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<OperationHistory> __insertionAdapterOfOperationHistory;

  public OperationHistoryDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOperationHistory = new EntityInsertionAdapter<OperationHistory>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `operation_historys` (`_id`,`walletId`,`operation`,`status`,`description`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, OperationHistory value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getWalletId());
        if (value.getOperation() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getOperation());
        }
        if (value.getStatus() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getStatus());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDescription());
        }
        stmt.bindLong(6, value.getTimestamp());
      }
    };
  }

  @Override
  public void insertOperationHistorys(final OperationHistory... operationHistorys) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfOperationHistory.insert(operationHistorys);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<OperationHistory> loadOperationHistorysByWalletId(final int walletId) {
    final String _sql = "SELECT * FROM operation_historys WHERE walletId = ? ORDER BY _id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, walletId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "_id");
      final int _cursorIndexOfWalletId = CursorUtil.getColumnIndexOrThrow(_cursor, "walletId");
      final int _cursorIndexOfOperation = CursorUtil.getColumnIndexOrThrow(_cursor, "operation");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<OperationHistory> _result = new ArrayList<OperationHistory>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final OperationHistory _item;
        _item = new OperationHistory();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpWalletId;
        _tmpWalletId = _cursor.getInt(_cursorIndexOfWalletId);
        _item.setWalletId(_tmpWalletId);
        final String _tmpOperation;
        _tmpOperation = _cursor.getString(_cursorIndexOfOperation);
        _item.setOperation(_tmpOperation);
        final String _tmpStatus;
        _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        _item.setStatus(_tmpStatus);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        _item.setDescription(_tmpDescription);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
