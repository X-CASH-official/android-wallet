/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.utils.database.models;

import java.io.Serializable;

public class SubAddress implements Serializable {

    private int id;
    private String address;
    private String label;

    public SubAddress() {

    }

    public SubAddress(int id, String address, String label) {
        this.id = id;
        this.address = address;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
