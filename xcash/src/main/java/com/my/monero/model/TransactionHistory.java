/**
 * Copyright (c) 2017-2018 m2049r
 * <p>
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */

package com.my.monero.model;

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
        for (Iterator<TransactionInfo> iterator = t.iterator(); iterator.hasNext(); ) {
            TransactionInfo info = iterator.next();
            if (info.account != accountIndex || info.amount == 0) {
                iterator.remove();
            }
        }
        transactions = t;
    }

    private native List<TransactionInfo> refreshJ();

}
