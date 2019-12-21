/**
 * Copyright (c) 2017-2018 m2049r
 *
 * Copyright (c) 2019 by snakeway
 *
 * All rights reserved.
 */

package com.my.monero.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransactionHistory {

    private static final String TAG = "TransactionHistory";

    static {
        System.loadLibrary("monerujo");
    }

    private long handle;

    int accountIndex;

    public void setAccountFor(Wallet wallet) {
        if (accountIndex != wallet.getAccountIndex()) {
            this.accountIndex = wallet.getAccountIndex();
            refreshWithNotes(wallet);
        }
    }

    public TransactionHistory(long handle, int accountIndex) {
        this.handle = handle;
        this.accountIndex = accountIndex;
    }

    public void loadNotes(Wallet wallet) {
        for (TransactionInfo info : transactions) {
            info.notes = wallet.getUserNote(info.hash);
        }
    }

    public native int getCount(); // over all accounts/subaddresses

    //private native long getTransactionByIndexJ(int i);

    //private native long getTransactionByIdJ(String id);

    public List<TransactionInfo> getAll() {
        return transactions;
    }

    private List<TransactionInfo> transactions = new ArrayList<>();

    public void refreshWithNotes(Wallet wallet) {
        refresh();
        loadNotes(wallet);
    }

//    public void refresh() {
//        transactions = refreshJ();
//    }

    public void refresh() {
        List<TransactionInfo> t = refreshJ();
        Log.d(TAG, "refreshed " + t.size());
        for (Iterator<TransactionInfo> iterator = t.iterator(); iterator.hasNext(); ) {
            TransactionInfo info = iterator.next();
            if (info.account != accountIndex) {
                iterator.remove();
                Log.d(TAG, "removed " + info.hash);
            } else {
                Log.d(TAG, "kept " + info.hash);
            }
        }
        transactions = t;
    }

    private native List<TransactionInfo> refreshJ();

}
