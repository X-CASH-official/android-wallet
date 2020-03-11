/**
 * Copyright (c) 2017-2018 m2049r
 * <p>
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */

package com.my.monero.model;

public enum NetworkType {

    NetworkType_Mainnet(0),
    NetworkType_Testnet(1),
    NetworkType_Stagenet(2);

    public static NetworkType fromInteger(int n) {
        switch (n) {
            case 0:
                return NetworkType_Mainnet;
            case 1:
                return NetworkType_Testnet;
            case 2:
                return NetworkType_Stagenet;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    private int value;

    NetworkType(int value) {
        this.value = value;
    }

}
