/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.xcash.wallet.aidl;
public interface WalletOperateManager extends android.os.IInterface
{
  /** Default implementation for WalletOperateManager. */
  public static class Default implements com.xcash.wallet.aidl.WalletOperateManager
  {
    @Override public void setDaemon(java.lang.String url, java.lang.String username, java.lang.String password, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
    {
    }
    @Override public void createWallet(java.lang.String name, java.lang.String password, java.lang.String passwordPrompt, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException
    {
    }
    @Override public void importWalletMnemonic(java.lang.String name, java.lang.String password, java.lang.String passwordPrompt, java.lang.String mnemonic, long restoreHeight, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException
    {
    }
    @Override public void importWalletKeys(java.lang.String name, java.lang.String password, java.lang.String passwordPrompt, java.lang.String addressKey, java.lang.String privateViewKey, java.lang.String privateSpendKey, long restoreHeight, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException
    {
    }
    @Override public void checkWalletPassword(java.lang.String name, java.lang.String password, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
    {
    }
    @Override public void loadRefreshWallet(int id, java.lang.String name, java.lang.String password, long restoreHeight, boolean needReset, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException
    {
    }
    @Override public void createTransaction(java.lang.String walletAddress, java.lang.String amount, java.lang.String ringSize, java.lang.String paymentId, java.lang.String description, int priority, boolean publicTransaction, com.xcash.wallet.aidl.OnCreateTransactionListener onCreateTransactionListener) throws android.os.RemoteException
    {
    }
    @Override public void sendTransaction(com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
    {
    }
    @Override public void closeActiveWallet(com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
    {
    }
    @Override public void closeWallet(int id, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
    {
    }
    @Override public void getWalletData(int id, java.lang.String name, java.lang.String password, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException
    {
    }
    @Override public void vote(java.lang.String value, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
    {
    }
    @Override public void delegateRegister(java.lang.String delegate_name, java.lang.String delegate_IP_address, java.lang.String block_verifier_messages_public_key, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
    {
    }
    @Override public void delegateUpdate(java.lang.String item, java.lang.String value, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
    {
    }
    @Override public void delegateRemove(com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
    {
    }
    @Override public void runService() throws android.os.RemoteException
    {
    }
    @Override public void stopService() throws android.os.RemoteException
    {
    }
    @Override public void changeLanguage(java.lang.String language) throws android.os.RemoteException
    {
    }
    @Override public void registerListener(com.xcash.wallet.aidl.OnWalletRefreshListener onWalletRefreshListener) throws android.os.RemoteException
    {
    }
    @Override public void unRegisterListener(com.xcash.wallet.aidl.OnWalletRefreshListener onWalletRefreshListener) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.xcash.wallet.aidl.WalletOperateManager
  {
    private static final java.lang.String DESCRIPTOR = "com.xcash.wallet.aidl.WalletOperateManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.xcash.wallet.aidl.WalletOperateManager interface,
     * generating a proxy if needed.
     */
    public static com.xcash.wallet.aidl.WalletOperateManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.xcash.wallet.aidl.WalletOperateManager))) {
        return ((com.xcash.wallet.aidl.WalletOperateManager)iin);
      }
      return new com.xcash.wallet.aidl.WalletOperateManager.Stub.Proxy(obj);
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
        case TRANSACTION_setDaemon:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          com.xcash.wallet.aidl.OnNormalListener _arg3;
          _arg3 = com.xcash.wallet.aidl.OnNormalListener.Stub.asInterface(data.readStrongBinder());
          this.setDaemon(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_createWallet:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          com.xcash.wallet.aidl.OnWalletDataListener _arg3;
          _arg3 = com.xcash.wallet.aidl.OnWalletDataListener.Stub.asInterface(data.readStrongBinder());
          this.createWallet(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_importWalletMnemonic:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          java.lang.String _arg3;
          _arg3 = data.readString();
          long _arg4;
          _arg4 = data.readLong();
          com.xcash.wallet.aidl.OnWalletDataListener _arg5;
          _arg5 = com.xcash.wallet.aidl.OnWalletDataListener.Stub.asInterface(data.readStrongBinder());
          this.importWalletMnemonic(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_importWalletKeys:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          java.lang.String _arg3;
          _arg3 = data.readString();
          java.lang.String _arg4;
          _arg4 = data.readString();
          java.lang.String _arg5;
          _arg5 = data.readString();
          long _arg6;
          _arg6 = data.readLong();
          com.xcash.wallet.aidl.OnWalletDataListener _arg7;
          _arg7 = com.xcash.wallet.aidl.OnWalletDataListener.Stub.asInterface(data.readStrongBinder());
          this.importWalletKeys(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_checkWalletPassword:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          com.xcash.wallet.aidl.OnNormalListener _arg2;
          _arg2 = com.xcash.wallet.aidl.OnNormalListener.Stub.asInterface(data.readStrongBinder());
          this.checkWalletPassword(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_loadRefreshWallet:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          long _arg3;
          _arg3 = data.readLong();
          boolean _arg4;
          _arg4 = (0!=data.readInt());
          com.xcash.wallet.aidl.OnWalletDataListener _arg5;
          _arg5 = com.xcash.wallet.aidl.OnWalletDataListener.Stub.asInterface(data.readStrongBinder());
          this.loadRefreshWallet(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_createTransaction:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          java.lang.String _arg3;
          _arg3 = data.readString();
          java.lang.String _arg4;
          _arg4 = data.readString();
          int _arg5;
          _arg5 = data.readInt();
          boolean _arg6;
          _arg6 = (0!=data.readInt());
          com.xcash.wallet.aidl.OnCreateTransactionListener _arg7;
          _arg7 = com.xcash.wallet.aidl.OnCreateTransactionListener.Stub.asInterface(data.readStrongBinder());
          this.createTransaction(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_sendTransaction:
        {
          data.enforceInterface(descriptor);
          com.xcash.wallet.aidl.OnNormalListener _arg0;
          _arg0 = com.xcash.wallet.aidl.OnNormalListener.Stub.asInterface(data.readStrongBinder());
          this.sendTransaction(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_closeActiveWallet:
        {
          data.enforceInterface(descriptor);
          com.xcash.wallet.aidl.OnNormalListener _arg0;
          _arg0 = com.xcash.wallet.aidl.OnNormalListener.Stub.asInterface(data.readStrongBinder());
          this.closeActiveWallet(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_closeWallet:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          com.xcash.wallet.aidl.OnNormalListener _arg1;
          _arg1 = com.xcash.wallet.aidl.OnNormalListener.Stub.asInterface(data.readStrongBinder());
          this.closeWallet(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_getWalletData:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          com.xcash.wallet.aidl.OnWalletDataListener _arg3;
          _arg3 = com.xcash.wallet.aidl.OnWalletDataListener.Stub.asInterface(data.readStrongBinder());
          this.getWalletData(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_vote:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          com.xcash.wallet.aidl.OnNormalListener _arg1;
          _arg1 = com.xcash.wallet.aidl.OnNormalListener.Stub.asInterface(data.readStrongBinder());
          this.vote(_arg0, _arg1);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_delegateRegister:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          com.xcash.wallet.aidl.OnNormalListener _arg3;
          _arg3 = com.xcash.wallet.aidl.OnNormalListener.Stub.asInterface(data.readStrongBinder());
          this.delegateRegister(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_delegateUpdate:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          com.xcash.wallet.aidl.OnNormalListener _arg2;
          _arg2 = com.xcash.wallet.aidl.OnNormalListener.Stub.asInterface(data.readStrongBinder());
          this.delegateUpdate(_arg0, _arg1, _arg2);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_delegateRemove:
        {
          data.enforceInterface(descriptor);
          com.xcash.wallet.aidl.OnNormalListener _arg0;
          _arg0 = com.xcash.wallet.aidl.OnNormalListener.Stub.asInterface(data.readStrongBinder());
          this.delegateRemove(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_runService:
        {
          data.enforceInterface(descriptor);
          this.runService();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_stopService:
        {
          data.enforceInterface(descriptor);
          this.stopService();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_changeLanguage:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.changeLanguage(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_registerListener:
        {
          data.enforceInterface(descriptor);
          com.xcash.wallet.aidl.OnWalletRefreshListener _arg0;
          _arg0 = com.xcash.wallet.aidl.OnWalletRefreshListener.Stub.asInterface(data.readStrongBinder());
          this.registerListener(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_unRegisterListener:
        {
          data.enforceInterface(descriptor);
          com.xcash.wallet.aidl.OnWalletRefreshListener _arg0;
          _arg0 = com.xcash.wallet.aidl.OnWalletRefreshListener.Stub.asInterface(data.readStrongBinder());
          this.unRegisterListener(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.xcash.wallet.aidl.WalletOperateManager
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
      @Override public void setDaemon(java.lang.String url, java.lang.String username, java.lang.String password, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(url);
          _data.writeString(username);
          _data.writeString(password);
          _data.writeStrongBinder((((onNormalListener!=null))?(onNormalListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setDaemon, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().setDaemon(url, username, password, onNormalListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void createWallet(java.lang.String name, java.lang.String password, java.lang.String passwordPrompt, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          _data.writeString(password);
          _data.writeString(passwordPrompt);
          _data.writeStrongBinder((((onWalletDataListener!=null))?(onWalletDataListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_createWallet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().createWallet(name, password, passwordPrompt, onWalletDataListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void importWalletMnemonic(java.lang.String name, java.lang.String password, java.lang.String passwordPrompt, java.lang.String mnemonic, long restoreHeight, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          _data.writeString(password);
          _data.writeString(passwordPrompt);
          _data.writeString(mnemonic);
          _data.writeLong(restoreHeight);
          _data.writeStrongBinder((((onWalletDataListener!=null))?(onWalletDataListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_importWalletMnemonic, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().importWalletMnemonic(name, password, passwordPrompt, mnemonic, restoreHeight, onWalletDataListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void importWalletKeys(java.lang.String name, java.lang.String password, java.lang.String passwordPrompt, java.lang.String addressKey, java.lang.String privateViewKey, java.lang.String privateSpendKey, long restoreHeight, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          _data.writeString(password);
          _data.writeString(passwordPrompt);
          _data.writeString(addressKey);
          _data.writeString(privateViewKey);
          _data.writeString(privateSpendKey);
          _data.writeLong(restoreHeight);
          _data.writeStrongBinder((((onWalletDataListener!=null))?(onWalletDataListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_importWalletKeys, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().importWalletKeys(name, password, passwordPrompt, addressKey, privateViewKey, privateSpendKey, restoreHeight, onWalletDataListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void checkWalletPassword(java.lang.String name, java.lang.String password, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          _data.writeString(password);
          _data.writeStrongBinder((((onNormalListener!=null))?(onNormalListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_checkWalletPassword, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().checkWalletPassword(name, password, onNormalListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void loadRefreshWallet(int id, java.lang.String name, java.lang.String password, long restoreHeight, boolean needReset, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(id);
          _data.writeString(name);
          _data.writeString(password);
          _data.writeLong(restoreHeight);
          _data.writeInt(((needReset)?(1):(0)));
          _data.writeStrongBinder((((onWalletDataListener!=null))?(onWalletDataListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_loadRefreshWallet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().loadRefreshWallet(id, name, password, restoreHeight, needReset, onWalletDataListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void createTransaction(java.lang.String walletAddress, java.lang.String amount, java.lang.String ringSize, java.lang.String paymentId, java.lang.String description, int priority, boolean publicTransaction, com.xcash.wallet.aidl.OnCreateTransactionListener onCreateTransactionListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(walletAddress);
          _data.writeString(amount);
          _data.writeString(ringSize);
          _data.writeString(paymentId);
          _data.writeString(description);
          _data.writeInt(priority);
          _data.writeInt(((publicTransaction)?(1):(0)));
          _data.writeStrongBinder((((onCreateTransactionListener!=null))?(onCreateTransactionListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_createTransaction, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().createTransaction(walletAddress, amount, ringSize, paymentId, description, priority, publicTransaction, onCreateTransactionListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void sendTransaction(com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((onNormalListener!=null))?(onNormalListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_sendTransaction, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().sendTransaction(onNormalListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void closeActiveWallet(com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((onNormalListener!=null))?(onNormalListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_closeActiveWallet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().closeActiveWallet(onNormalListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void closeWallet(int id, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(id);
          _data.writeStrongBinder((((onNormalListener!=null))?(onNormalListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_closeWallet, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().closeWallet(id, onNormalListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void getWalletData(int id, java.lang.String name, java.lang.String password, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(id);
          _data.writeString(name);
          _data.writeString(password);
          _data.writeStrongBinder((((onWalletDataListener!=null))?(onWalletDataListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getWalletData, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().getWalletData(id, name, password, onWalletDataListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void vote(java.lang.String value, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(value);
          _data.writeStrongBinder((((onNormalListener!=null))?(onNormalListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_vote, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().vote(value, onNormalListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void delegateRegister(java.lang.String delegate_name, java.lang.String delegate_IP_address, java.lang.String block_verifier_messages_public_key, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(delegate_name);
          _data.writeString(delegate_IP_address);
          _data.writeString(block_verifier_messages_public_key);
          _data.writeStrongBinder((((onNormalListener!=null))?(onNormalListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_delegateRegister, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().delegateRegister(delegate_name, delegate_IP_address, block_verifier_messages_public_key, onNormalListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void delegateUpdate(java.lang.String item, java.lang.String value, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(item);
          _data.writeString(value);
          _data.writeStrongBinder((((onNormalListener!=null))?(onNormalListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_delegateUpdate, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().delegateUpdate(item, value, onNormalListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void delegateRemove(com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((onNormalListener!=null))?(onNormalListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_delegateRemove, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().delegateRemove(onNormalListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void runService() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_runService, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().runService();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void stopService() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_stopService, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().stopService();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void changeLanguage(java.lang.String language) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(language);
          boolean _status = mRemote.transact(Stub.TRANSACTION_changeLanguage, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().changeLanguage(language);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void registerListener(com.xcash.wallet.aidl.OnWalletRefreshListener onWalletRefreshListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((onWalletRefreshListener!=null))?(onWalletRefreshListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerListener, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().registerListener(onWalletRefreshListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unRegisterListener(com.xcash.wallet.aidl.OnWalletRefreshListener onWalletRefreshListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((onWalletRefreshListener!=null))?(onWalletRefreshListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_unRegisterListener, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().unRegisterListener(onWalletRefreshListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static com.xcash.wallet.aidl.WalletOperateManager sDefaultImpl;
    }
    static final int TRANSACTION_setDaemon = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_createWallet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_importWalletMnemonic = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_importWalletKeys = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_checkWalletPassword = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_loadRefreshWallet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_createTransaction = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_sendTransaction = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_closeActiveWallet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_closeWallet = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_getWalletData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_vote = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_delegateRegister = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_delegateUpdate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_delegateRemove = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_runService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_stopService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_changeLanguage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_registerListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_unRegisterListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    public static boolean setDefaultImpl(com.xcash.wallet.aidl.WalletOperateManager impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.xcash.wallet.aidl.WalletOperateManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void setDaemon(java.lang.String url, java.lang.String username, java.lang.String password, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException;
  public void createWallet(java.lang.String name, java.lang.String password, java.lang.String passwordPrompt, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException;
  public void importWalletMnemonic(java.lang.String name, java.lang.String password, java.lang.String passwordPrompt, java.lang.String mnemonic, long restoreHeight, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException;
  public void importWalletKeys(java.lang.String name, java.lang.String password, java.lang.String passwordPrompt, java.lang.String addressKey, java.lang.String privateViewKey, java.lang.String privateSpendKey, long restoreHeight, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException;
  public void checkWalletPassword(java.lang.String name, java.lang.String password, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException;
  public void loadRefreshWallet(int id, java.lang.String name, java.lang.String password, long restoreHeight, boolean needReset, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException;
  public void createTransaction(java.lang.String walletAddress, java.lang.String amount, java.lang.String ringSize, java.lang.String paymentId, java.lang.String description, int priority, boolean publicTransaction, com.xcash.wallet.aidl.OnCreateTransactionListener onCreateTransactionListener) throws android.os.RemoteException;
  public void sendTransaction(com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException;
  public void closeActiveWallet(com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException;
  public void closeWallet(int id, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException;
  public void getWalletData(int id, java.lang.String name, java.lang.String password, com.xcash.wallet.aidl.OnWalletDataListener onWalletDataListener) throws android.os.RemoteException;
  public void vote(java.lang.String value, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException;
  public void delegateRegister(java.lang.String delegate_name, java.lang.String delegate_IP_address, java.lang.String block_verifier_messages_public_key, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException;
  public void delegateUpdate(java.lang.String item, java.lang.String value, com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException;
  public void delegateRemove(com.xcash.wallet.aidl.OnNormalListener onNormalListener) throws android.os.RemoteException;
  public void runService() throws android.os.RemoteException;
  public void stopService() throws android.os.RemoteException;
  public void changeLanguage(java.lang.String language) throws android.os.RemoteException;
  public void registerListener(com.xcash.wallet.aidl.OnWalletRefreshListener onWalletRefreshListener) throws android.os.RemoteException;
  public void unRegisterListener(com.xcash.wallet.aidl.OnWalletRefreshListener onWalletRefreshListener) throws android.os.RemoteException;
}
