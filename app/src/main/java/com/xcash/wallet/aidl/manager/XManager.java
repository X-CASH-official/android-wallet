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
package com.xcash.wallet.aidl.manager;

import com.xcash.base.utils.FileTool;
import com.xcash.base.utils.LogTool;
import com.xcash.utils.database.AppDatabase;
import com.xcash.utils.database.entity.Node;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.wallet.TheApplication;

import java.io.File;
import java.util.List;

/**
 * All the operate need in service,static variables are not shared
 */
public class XManager {

    public static final int TRANSACTION_MIN_CONFIRMATION = 10;
    public static final String SYMBOL = "XCASH";
    public static final String TAG = "XManager";
    public static final String WALLET = "wallet";

    private static XManager xManager;
    private final XWalletController xWalletController;

    private XManager() {
        xWalletController = new XWalletController();
    }

    public static synchronized XManager getInstance() {
        if (xManager == null) {
            xManager = new XManager();
        }
        return xManager;
    }

    public File getWalletDir(String name) {
        String dirPath = TheApplication.getFilesDirRootPath() + File.separator + WALLET + File.separator + name;
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getWalletFile(String name) {
        return new File(getWalletDir(name), name);
    }

    public File getKeysFile(String name) {
        return new File(getWalletDir(name), name + ".keys");
    }

    public File getAddressFile(String name) {
        return new File(getWalletDir(name), name + ".address.txt");
    }

    public Wallet createWallet(String walletName, String password) {
        return xWalletController.createWallet(generateXMRFile(walletName), password);
    }

    public Wallet recoveryWallet(String walletName, String password, String mnemonic, long restoreHeight) {
        return xWalletController.recoveryWallet(generateXMRFile(walletName), password, mnemonic, restoreHeight);
    }

    public Wallet createWalletWithKeys(String walletName, String password, String address, String viewKey, String spendKey, long restoreHeight) {
        return xWalletController.createWalletWithKeys(generateXMRFile(walletName), password, restoreHeight, address, viewKey, spendKey);
    }

    public boolean verifyWalletPasswordOnly(String name, String password) {
        if (name == null || password == null) {
            return false;
        }
        File file = getKeysFile(name);
        if (!file.exists()) {
            return false;
        }
        return xWalletController.verifyWalletPasswordOnly(file.getPath(), password);
    }

    public com.my.monero.model.Wallet openWallet(String name, String password, int walletId) {
        if (name == null || password == null) {
            return null;
        }
        File file = getWalletFile(name);
        if (!file.exists()) {
            return null;
        }
        return xWalletController.openWallet(file.getPath(), password, walletId);
    }

    public boolean startWallet(com.my.monero.model.Wallet wallet, Node node, long restoreHeight, XWalletController.OnWalletListener onWalletListener) {
        if (wallet == null) {
            return false;
        }
        return xWalletController.startWallet(wallet, node, restoreHeight, onWalletListener);
    }

    public XWalletController getXWalletController() {
        return xWalletController;
    }

    public void saveWallet(Wallet wallet) {
        if (wallet == null) {
            return;
        }
        AppDatabase appDatabase = AppDatabase.getInstance();
        List<Wallet> wallets = appDatabase.walletDao().loadWallets();
        if (wallets != null) {
            Wallet[] walletsArray = wallets.toArray(new Wallet[]{});
            for (int i = 0; i < walletsArray.length; i++) {
                Wallet theWallet = walletsArray[i];
                theWallet.setActive(false);
            }
            appDatabase.walletDao().updateWallets(walletsArray);
        }
        wallet.setActive(true);
        appDatabase.walletDao().insertWallet(wallet);
    }

    private File generateXMRFile(String name) {
        File walletFile = getWalletFile(name);
        File keysFile = getKeysFile(name + ".keys");
        File addressFile = getAddressFile(name + ".address.txt");
        if (walletFile.exists() || keysFile.exists() || addressFile.exists()) {
            LogTool.e(TAG, "Some wallet files already exist for " + name);
        }
        return walletFile;
    }

    public static void insertNodes() {
        List<Node> nodes = AppDatabase.getInstance().nodeDao().loadNodesBySymbol(SYMBOL);
        if (nodes != null && nodes.size() > 0) {
            return;
        }
        Node[] defaultNodesArray = new Node[10];
        defaultNodesArray[0] = new Node(SYMBOL, "asiaseed2.x-cash.org:18281", "", "", true);
        defaultNodesArray[1] = new Node(SYMBOL, "asiaseed1.x-cash.org:18281", "", "", false);
        defaultNodesArray[2] = new Node(SYMBOL, "usseed3.x-cash.org:18281", "", "", false);
        defaultNodesArray[3] = new Node(SYMBOL, "usseed2.x-cash.org:18281", "", "", false);
        defaultNodesArray[4] = new Node(SYMBOL, "usseed1.x-cash.org:18281", "", "", false);
        defaultNodesArray[5] = new Node(SYMBOL, "euseed3.x-cash.org:18281", "", "", false);
        defaultNodesArray[6] = new Node(SYMBOL, "euseed2.x-cash.org:18281", "", "", false);
        defaultNodesArray[7] = new Node(SYMBOL, "euseed1.x-cash.org:18281", "", "", false);
        defaultNodesArray[8] = new Node(SYMBOL, "xcash-china2.oiwm.com:18281", "", "", false);
        defaultNodesArray[9] = new Node(SYMBOL, "xcash-china1.oiwm.com:18281", "snakeway", "snake123456", false);
        AppDatabase.getInstance().nodeDao().insertNodes(defaultNodesArray);
    }

    public static void deleteWallet(String name) {
        String dirPath = TheApplication.getFilesDirRootPath() + File.separator + WALLET + File.separator + name;
        FileTool.deleteFolder(dirPath);
    }

}
