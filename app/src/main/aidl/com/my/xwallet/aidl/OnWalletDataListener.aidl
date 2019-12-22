/**
 *Copyright (c) 2019 by snakeway
 *
 *All rights reserved.
 */
package com.my.xwallet.aidl;

import com.my.xwallet.aidl.Wallet;

interface OnWalletDataListener {

      void onSuccess(in Wallet wallet);

      void onError(String error);

}
