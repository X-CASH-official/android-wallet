/**
 *Copyright (c) 2019 by snakeway
 *
 *All rights reserved.
 */
package com.xcash.wallet.aidl;

import com.xcash.wallet.aidl.Transaction;

interface OnCreateTransactionListener {

      void onSuccess(in Transaction transaction);

      void onError(String error);

}
