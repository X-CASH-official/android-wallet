/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.utils;

import com.my.utils.database.entity.Wallet;

import java.util.List;

public class DaoTool {

    public static void removeWalletById(List<Wallet> wallets, int id) {
        if (wallets == null) {
            return;
        }
        for (int i = 0; i < wallets.size(); i++) {
            Wallet wallet = wallets.get(i);
            if (id == wallet.getId()) {
                wallets.remove(i);
                break;
            }
        }

    }

}
