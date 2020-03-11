/**
 *Copyright (c) 2019 by snakeway
 *
 *All rights reserved.
 */
package com.xcash.wallet.aidl;

import com.xcash.wallet.aidl.Wallet;

interface OnWalletDataListener {

      void onSuccess(in Wallet wallet);

      void onError(String error);

}
