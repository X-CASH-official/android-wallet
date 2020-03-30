/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.xcash.wallet.aidl;
public interface OnWalletRefreshListener extends android.os.IInterface
{
  /** Default implementation for OnWalletRefreshListener. */
  public static class Default implements com.xcash.wallet.aidl.OnWalletRefreshListener
  {
    @Override public void queueFullError(java.lang.String error) throws android.os.RemoteException
    {
    }
    @Override public void beginLoadWallet(int walletId) throws android.os.RemoteException
    {
    }
    @Override public void synchronizeStatusError(int walletId, java.lang.String error) throws android.os.RemoteException
    {
    }
    @Override public void synchronizeStatusSuccess(int walletId) throws android.os.RemoteException
    {
    }
    @Override public void refreshBalance(int walletId, java.lang.String balance, java.lang.String unlockedBalance) throws android.os.RemoteException
    {
    }
    @Override public void blockProgress(int walletId, boolean result, long blockChainHeight, long daemonHeight, int progress) throws android.os.RemoteException
    {
    }
    @Override public void refreshTransaction(int walletId) throws android.os.RemoteException
    {
    }
    @Override public void closeActiveWallet(int walletId) throws android.os.RemoteException
    {
    }
    @Override public void moneySpent(int walletId, java.lang.String txId, long amount, boolean fullSynchronizeOnce) throws android.os.RemoteException
    {
    }
    @Override public void moneyReceive(int walletId, java.lang.String txId, long amount, boolean fullSynchronizeOnce) throws android.os.RemoteException
    {
    }
    @Override public void unconfirmedMoneyReceive(int walletId, java.lang.String txId, long amount, boolean fullSynchronizeOnce) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.xcash.wallet.aidl.OnWalletRefreshListener
  {
    private static final java.lang.String DESCRIPTOR = "com.xcash.wallet.aidl.OnWalletRefreshListener";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.xcash.wallet.aidl.OnWalletRefreshListener interface,
     * generating a proxy if needed.
     */
    public static com.xcash.wallet.aidl.OnWalletRefreshListener asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.xcash.wallet.aidl.OnWalletRefreshListener))) {
        return ((com.xcash.wallet.aidl.OnWalletRefreshListener)iin);
      }
      return new com.xcash.wallet.aidl.OnWalletRefreshListener.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_queueFullError:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.queueFullError(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_beginLoadWallet:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          this.beginLoadWallet(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_synchronizeStatusError:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.synchronizeStatusError(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_synchronizeStatusSuccess:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          this.synchronizeStatusSuccess(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_refreshBalance:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          this.refreshBalance(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_blockProgress:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          long _arg2;
          _arg2 = data.readLong();
          long _arg3;
          _arg3 = data.readLong();
          int _arg4;
          _arg4 = data.readInt();
          this.blockProgress(_arg0, _arg1, _arg2, _arg3, _arg4);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_refreshTransaction:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          this.refreshTransaction(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_closeActiveWallet:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          this.closeActiveWallet(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_moneySpent:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          long _arg2;
          _arg2 = data.readLong();
          boolean _arg3;
          _arg3 = (0!=data.readInt());
          this.moneySpent(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_moneyReceive:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          long _arg2;
          _arg2 = data.readLong();
          boolean _arg3;
          _arg3 = (0!=data.readInt());
          this.moneyReceive(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_unconfirmedMoneyReceive:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          long _arg2;
          _arg2 = data.readLong();
          boolean _arg3;
          _arg3 = (0!=data.readInt());
          this.unconfirmedMoneyReceive(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.xcash.wallet.aidl.OnWalletRefreshListener
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public void queueFullError(java.lang.String error) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(error);
          boolean _status = mRemote.transact(Stub.TRANSACTION_queueFullError, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().queueFullError(error);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void beginLoadWallet(int walletId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(walletId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_beginLoadWallet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().beginLoadWallet(walletId);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void synchronizeStatusError(int walletId, java.lang.String error) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(walletId);
          _data.writeString(error);
          boolean _status = mRemote.transact(Stub.TRANSACTION_synchronizeStatusError, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().synchronizeStatusError(walletId, error);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void synchronizeStatusSuccess(int walletId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(walletId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_synchronizeStatusSuccess, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().synchronizeStatusSuccess(walletId);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void refreshBalance(int walletId, java.lang.String balance, java.lang.String unlockedBalance) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(walletId);
          _data.writeString(balance);
          _data.writeString(unlockedBalance);
          boolean _status = mRemote.transact(Stub.TRANSACTION_refreshBalance, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().refreshBalance(walletId, balance, unlockedBalance);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void blockProgress(int walletId, boolean result, long blockChainHeight, long daemonHeight, int progress) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(walletId);
          _data.writeInt(((result)?(1):(0)));
          _data.writeLong(blockChainHeight);
          _data.writeLong(daemonHeight);
          _data.writeInt(progress);
          boolean _status = mRemote.transact(Stub.TRANSACTION_blockProgress, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().blockProgress(walletId, result, blockChainHeight, daemonHeight, progress);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void refreshTransaction(int walletId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(walletId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_refreshTransaction, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().refreshTransaction(walletId);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void closeActiveWallet(int walletId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(walletId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_closeActiveWallet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().closeActiveWallet(walletId);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void moneySpent(int walletId, java.lang.String txId, long amount, boolean fullSynchronizeOnce) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(walletId);
          _data.writeString(txId);
          _data.writeLong(amount);
          _data.writeInt(((fullSynchronizeOnce)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_moneySpent, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().moneySpent(walletId, txId, amount, fullSynchronizeOnce);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void moneyReceive(int walletId, java.lang.String txId, long amount, boolean fullSynchronizeOnce) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(walletId);
          _data.writeString(txId);
          _data.writeLong(amount);
          _data.writeInt(((fullSynchronizeOnce)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_moneyReceive, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().moneyReceive(walletId, txId, amount, fullSynchronizeOnce);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unconfirmedMoneyReceive(int walletId, java.lang.String txId, long amount, boolean fullSynchronizeOnce) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(walletId);
          _data.writeString(txId);
          _data.writeLong(amount);
          _data.writeInt(((fullSynchronizeOnce)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_unconfirmedMoneyReceive, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().unconfirmedMoneyReceive(walletId, txId, amount, fullSynchronizeOnce);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static com.xcash.wallet.aidl.OnWalletRefreshListener sDefaultImpl;
    }
    static final int TRANSACTION_queueFullError = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_beginLoadWallet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_synchronizeStatusError = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_synchronizeStatusSuccess = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_refreshBalance = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_blockProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_refreshTransaction = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_closeActiveWallet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_moneySpent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_moneyReceive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_unconfirmedMoneyReceive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    public static boolean setDefaultImpl(com.xcash.wallet.aidl.OnWalletRefreshListener impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.xcash.wallet.aidl.OnWalletRefreshListener getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void queueFullError(java.lang.String error) throws android.os.RemoteException;
  public void beginLoadWallet(int walletId) throws android.os.RemoteException;
  public void synchronizeStatusError(int walletId, java.lang.String error) throws android.os.RemoteException;
  public void synchronizeStatusSuccess(int walletId) throws android.os.RemoteException;
  public void refreshBalance(int walletId, java.lang.String balance, java.lang.String unlockedBalance) throws android.os.RemoteException;
  public void blockProgress(int walletId, boolean result, long blockChainHeight, long daemonHeight, int progress) throws android.os.RemoteException;
  public void refreshTransaction(int walletId) throws android.os.RemoteException;
  public void closeActiveWallet(int walletId) throws android.os.RemoteException;
  public void moneySpent(int walletId, java.lang.String txId, long amount, boolean fullSynchronizeOnce) throws android.os.RemoteException;
  public void moneyReceive(int walletId, java.lang.String txId, long amount, boolean fullSynchronizeOnce) throws android.os.RemoteException;
  public void unconfirmedMoneyReceive(int walletId, java.lang.String txId, long amount, boolean fullSynchronizeOnce) throws android.os.RemoteException;
}
