package com.my.xwallet.aidl;

import com.my.xwallet.aidl.Wallet;
/**
 *Copyright (c) 2019 by snakeway
 *
 *All rights reserved.
 */
interface OnWalletDataListener {

      void onSuccess(in Wallet wallet);

      void onError(String error);

}
