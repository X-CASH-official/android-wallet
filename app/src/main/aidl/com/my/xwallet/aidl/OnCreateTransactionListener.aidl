package com.my.xwallet.aidl;

import com.my.xwallet.aidl.Transaction;
/**
 *Copyright (c) 2019 by snakeway
 *
 *All rights reserved.
 */
interface OnCreateTransactionListener {

      void onSuccess(in Transaction transaction);

      void onError(String error);

}
