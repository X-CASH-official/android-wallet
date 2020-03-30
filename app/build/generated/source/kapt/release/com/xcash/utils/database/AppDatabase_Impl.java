package com.xcash.utils.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.xcash.utils.database.dao.AddressBookDao;
import com.xcash.utils.database.dao.AddressBookDao_Impl;
import com.xcash.utils.database.dao.NodeDao;
import com.xcash.utils.database.dao.NodeDao_Impl;
import com.xcash.utils.database.dao.OperationHistoryDao;
import com.xcash.utils.database.dao.OperationHistoryDao_Impl;
import com.xcash.utils.database.dao.TransactionInfoDao;
import com.xcash.utils.database.dao.TransactionInfoDao_Impl;
import com.xcash.utils.database.dao.WalletDao;
import com.xcash.utils.database.dao.WalletDao_Impl;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AddressBookDao _addressBookDao;

  private volatile NodeDao _nodeDao;

  private volatile OperationHistoryDao _operationHistoryDao;

  private volatile TransactionInfoDao _transactionInfoDao;

  private volatile WalletDao _walletDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `address_books` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `symbol` TEXT, `address` TEXT, `notes` TEXT)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_address_books_symbol_address_notes` ON `address_books` (`symbol`, `address`, `notes`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `nodes` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `symbol` TEXT, `url` TEXT, `username` TEXT, `password` TEXT, `isActive` INTEGER NOT NULL)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_nodes_symbol_url` ON `nodes` (`symbol`, `url`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `operation_historys` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `walletId` INTEGER NOT NULL, `operation` TEXT, `status` TEXT, `description` TEXT, `timestamp` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `transaction_infos` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `symbol` TEXT, `walletId` INTEGER NOT NULL, `direction` INTEGER NOT NULL, `isPending` INTEGER NOT NULL, `isFailed` INTEGER NOT NULL, `amount` TEXT, `fee` TEXT, `blockHeight` INTEGER NOT NULL, `confirmations` INTEGER NOT NULL, `hash` TEXT, `timestamp` INTEGER NOT NULL, `paymentId` TEXT, `txKey` TEXT, `address` TEXT, `description` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `wallets` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `symbol` TEXT, `name` TEXT, `address` TEXT, `balance` TEXT, `unlockedBalance` TEXT, `passwordPrompt` TEXT, `restoreHeight` INTEGER NOT NULL, `isActive` INTEGER NOT NULL, `isReadOnly` INTEGER NOT NULL)");
        _db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_wallets_symbol_name` ON `wallets` (`symbol`, `name`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3cc9fe7c2d8946b1625dedc4c4654fbf')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `address_books`");
        _db.execSQL("DROP TABLE IF EXISTS `nodes`");
        _db.execSQL("DROP TABLE IF EXISTS `operation_historys`");
        _db.execSQL("DROP TABLE IF EXISTS `transaction_infos`");
        _db.execSQL("DROP TABLE IF EXISTS `wallets`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsAddressBooks = new HashMap<String, TableInfo.Column>(4);
        _columnsAddressBooks.put("_id", new TableInfo.Column("_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAddressBooks.put("symbol", new TableInfo.Column("symbol", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAddressBooks.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAddressBooks.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAddressBooks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAddressBooks = new HashSet<TableInfo.Index>(1);
        _indicesAddressBooks.add(new TableInfo.Index("index_address_books_symbol_address_notes", true, Arrays.asList("symbol","address","notes")));
        final TableInfo _infoAddressBooks = new TableInfo("address_books", _columnsAddressBooks, _foreignKeysAddressBooks, _indicesAddressBooks);
        final TableInfo _existingAddressBooks = TableInfo.read(_db, "address_books");
        if (! _infoAddressBooks.equals(_existingAddressBooks)) {
          return new RoomOpenHelper.ValidationResult(false, "address_books(com.xcash.utils.database.entity.AddressBook).\n"
                  + " Expected:\n" + _infoAddressBooks + "\n"
                  + " Found:\n" + _existingAddressBooks);
        }
        final HashMap<String, TableInfo.Column> _columnsNodes = new HashMap<String, TableInfo.Column>(6);
        _columnsNodes.put("_id", new TableInfo.Column("_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNodes.put("symbol", new TableInfo.Column("symbol", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNodes.put("url", new TableInfo.Column("url", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNodes.put("username", new TableInfo.Column("username", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNodes.put("password", new TableInfo.Column("password", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNodes.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNodes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNodes = new HashSet<TableInfo.Index>(1);
        _indicesNodes.add(new TableInfo.Index("index_nodes_symbol_url", true, Arrays.asList("symbol","url")));
        final TableInfo _infoNodes = new TableInfo("nodes", _columnsNodes, _foreignKeysNodes, _indicesNodes);
        final TableInfo _existingNodes = TableInfo.read(_db, "nodes");
        if (! _infoNodes.equals(_existingNodes)) {
          return new RoomOpenHelper.ValidationResult(false, "nodes(com.xcash.utils.database.entity.Node).\n"
                  + " Expected:\n" + _infoNodes + "\n"
                  + " Found:\n" + _existingNodes);
        }
        final HashMap<String, TableInfo.Column> _columnsOperationHistorys = new HashMap<String, TableInfo.Column>(6);
        _columnsOperationHistorys.put("_id", new TableInfo.Column("_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOperationHistorys.put("walletId", new TableInfo.Column("walletId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOperationHistorys.put("operation", new TableInfo.Column("operation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOperationHistorys.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOperationHistorys.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOperationHistorys.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOperationHistorys = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesOperationHistorys = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoOperationHistorys = new TableInfo("operation_historys", _columnsOperationHistorys, _foreignKeysOperationHistorys, _indicesOperationHistorys);
        final TableInfo _existingOperationHistorys = TableInfo.read(_db, "operation_historys");
        if (! _infoOperationHistorys.equals(_existingOperationHistorys)) {
          return new RoomOpenHelper.ValidationResult(false, "operation_historys(com.xcash.utils.database.entity.OperationHistory).\n"
                  + " Expected:\n" + _infoOperationHistorys + "\n"
                  + " Found:\n" + _existingOperationHistorys);
        }
        final HashMap<String, TableInfo.Column> _columnsTransactionInfos = new HashMap<String, TableInfo.Column>(16);
        _columnsTransactionInfos.put("_id", new TableInfo.Column("_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("symbol", new TableInfo.Column("symbol", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("walletId", new TableInfo.Column("walletId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("direction", new TableInfo.Column("direction", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("isPending", new TableInfo.Column("isPending", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("isFailed", new TableInfo.Column("isFailed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("amount", new TableInfo.Column("amount", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("fee", new TableInfo.Column("fee", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("blockHeight", new TableInfo.Column("blockHeight", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("confirmations", new TableInfo.Column("confirmations", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("hash", new TableInfo.Column("hash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("paymentId", new TableInfo.Column("paymentId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("txKey", new TableInfo.Column("txKey", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactionInfos.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransactionInfos = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTransactionInfos = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTransactionInfos = new TableInfo("transaction_infos", _columnsTransactionInfos, _foreignKeysTransactionInfos, _indicesTransactionInfos);
        final TableInfo _existingTransactionInfos = TableInfo.read(_db, "transaction_infos");
        if (! _infoTransactionInfos.equals(_existingTransactionInfos)) {
          return new RoomOpenHelper.ValidationResult(false, "transaction_infos(com.xcash.utils.database.entity.TransactionInfo).\n"
                  + " Expected:\n" + _infoTransactionInfos + "\n"
                  + " Found:\n" + _existingTransactionInfos);
        }
        final HashMap<String, TableInfo.Column> _columnsWallets = new HashMap<String, TableInfo.Column>(10);
        _columnsWallets.put("_id", new TableInfo.Column("_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWallets.put("symbol", new TableInfo.Column("symbol", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWallets.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWallets.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWallets.put("balance", new TableInfo.Column("balance", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWallets.put("unlockedBalance", new TableInfo.Column("unlockedBalance", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWallets.put("passwordPrompt", new TableInfo.Column("passwordPrompt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWallets.put("restoreHeight", new TableInfo.Column("restoreHeight", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWallets.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWallets.put("isReadOnly", new TableInfo.Column("isReadOnly", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWallets = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWallets = new HashSet<TableInfo.Index>(1);
        _indicesWallets.add(new TableInfo.Index("index_wallets_symbol_name", true, Arrays.asList("symbol","name")));
        final TableInfo _infoWallets = new TableInfo("wallets", _columnsWallets, _foreignKeysWallets, _indicesWallets);
        final TableInfo _existingWallets = TableInfo.read(_db, "wallets");
        if (! _infoWallets.equals(_existingWallets)) {
          return new RoomOpenHelper.ValidationResult(false, "wallets(com.xcash.utils.database.entity.Wallet).\n"
                  + " Expected:\n" + _infoWallets + "\n"
                  + " Found:\n" + _existingWallets);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3cc9fe7c2d8946b1625dedc4c4654fbf", "864024c952acf98e507078bcf0cb5e3f");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "address_books","nodes","operation_historys","transaction_infos","wallets");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `address_books`");
      _db.execSQL("DELETE FROM `nodes`");
      _db.execSQL("DELETE FROM `operation_historys`");
      _db.execSQL("DELETE FROM `transaction_infos`");
      _db.execSQL("DELETE FROM `wallets`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public AddressBookDao addressBookDao() {
    if (_addressBookDao != null) {
      return _addressBookDao;
    } else {
      synchronized(this) {
        if(_addressBookDao == null) {
          _addressBookDao = new AddressBookDao_Impl(this);
        }
        return _addressBookDao;
      }
    }
  }

  @Override
  public NodeDao nodeDao() {
    if (_nodeDao != null) {
      return _nodeDao;
    } else {
      synchronized(this) {
        if(_nodeDao == null) {
          _nodeDao = new NodeDao_Impl(this);
        }
        return _nodeDao;
      }
    }
  }

  @Override
  public OperationHistoryDao operationHistoryDao() {
    if (_operationHistoryDao != null) {
      return _operationHistoryDao;
    } else {
      synchronized(this) {
        if(_operationHistoryDao == null) {
          _operationHistoryDao = new OperationHistoryDao_Impl(this);
        }
        return _operationHistoryDao;
      }
    }
  }

  @Override
  public TransactionInfoDao transactionInfoDao() {
    if (_transactionInfoDao != null) {
      return _transactionInfoDao;
    } else {
      synchronized(this) {
        if(_transactionInfoDao == null) {
          _transactionInfoDao = new TransactionInfoDao_Impl(this);
        }
        return _transactionInfoDao;
      }
    }
  }

  @Override
  public WalletDao walletDao() {
    if (_walletDao != null) {
      return _walletDao;
    } else {
      synchronized(this) {
        if(_walletDao == null) {
          _walletDao = new WalletDao_Impl(this);
        }
        return _walletDao;
      }
    }
  }
}
