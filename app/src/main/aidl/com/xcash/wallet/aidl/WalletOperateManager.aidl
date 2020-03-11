/**
 *Copyright (c) 2019 by snakeway
 *
 *All rights reserved.
 */
package com.xcash.wallet.aidl;

import com.xcash.wallet.aidl.OnWalletRefreshListener;
import com.xcash.wallet.aidl.OnWalletDataListener;
import com.xcash.wallet.aidl.OnNormalListener;
import com.xcash.wallet.aidl.OnCreateTransactionListener;

interface WalletOperateManager {

    void setDaemon(String url,String username,String password,OnNormalListener onNormalListener);

    void createWallet(String name,String password,String passwordPrompt,OnWalletDataListener onWalletDataListener);

    void importWalletMnemonic(String name,String password,String passwordPrompt,String mnemonic,long restoreHeight,OnWalletDataListener onWalletDataListener);

    void importWalletKeys(String name,String password,String passwordPrompt,String addressKey,String privateViewKey,String privateSpendKey,long restoreHeight,OnWalletDataListener onWalletDataListener);

    void checkWalletPassword(String name,String password,OnNormalListener onNormalListener);

    void loadRefreshWallet(int id,String name,String password, long restoreHeight,boolean needReset,OnWalletDataListener onWalletDataListener);

    void createTransaction(String walletAddress,String amount,String ringSize,String paymentId,String description,int priority,boolean publicTransaction,OnCreateTransactionListener onCreateTransactionListener);

    void sendTransaction(OnNormalListener onNormalListener);

    void closeActiveWallet(OnNormalListener onNormalListener);

    void closeWallet(int id,OnNormalListener onNormalListener);

    void getWalletData(int id,String name,String password,OnWalletDataListener onWalletDataListener);

    void vote(String value,OnNormalListener onNormalListener);

    void delegateRegister(String delegate_name,String delegate_IP_address,String block_verifier_messages_public_key,OnNormalListener onNormalListener);

    void delegateUpdate(String item,String value,OnNormalListener onNormalListener);

    void delegateRemove(OnNormalListener onNormalListener);

    void runService();

    void stopService();

    void changeLanguage(String language);

    void registerListener(OnWalletRefreshListener onWalletRefreshListener);

    void unRegisterListener(OnWalletRefreshListener onWalletRefreshListener);

}

