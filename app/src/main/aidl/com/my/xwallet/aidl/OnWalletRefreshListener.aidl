package com.my.xwallet.aidl;
/**
 *Copyright (c) 2019 by snakeway
 *
 *All rights reserved.
 */
interface OnWalletRefreshListener {

      void queueFullError( String error);

      void beginLoadWallet(int walletId);

      void synchronizeStatusError(int walletId, String error);

      void synchronizeStatusSuccess(int walletId);

      void refreshBalance(int walletId,String balance,String unlockedBalance);

      void blockProgress(int walletId, boolean result, long blockChainHeight, long daemonHeight , int progress);

      void refreshTransaction(int walletId);

      void closeActiveWallet(int walletId);

}
