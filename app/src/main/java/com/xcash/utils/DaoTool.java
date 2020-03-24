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
package com.xcash.utils;

import com.xcash.utils.database.entity.Wallet;

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
