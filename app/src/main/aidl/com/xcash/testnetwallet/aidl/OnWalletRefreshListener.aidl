/*
 * Copyright (c) 2019-2020 The X-Cash Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xcash.testnetwallet.aidl;

interface OnWalletRefreshListener {

      void queueFullError( String error);

      void beginLoadWallet(int walletId);

      void synchronizeStatusError(int walletId, String error);

      void synchronizeStatusSuccess(int walletId);

      void refreshBalance(int walletId,String balance,String unlockedBalance);

      void blockProgress(int walletId, boolean result, long blockChainHeight, long daemonHeight , int progress);

      void refreshTransaction(int walletId);

      void closeActiveWallet(int walletId);

      void moneySpent(int walletId,String txId, long amount, boolean fullSynchronizeOnce);

      void moneyReceive(int walletId,String txId, long amount, boolean fullSynchronizeOnce);

      void unconfirmedMoneyReceive(int walletId,String txId, long amount, boolean fullSynchronizeOnce);

}
