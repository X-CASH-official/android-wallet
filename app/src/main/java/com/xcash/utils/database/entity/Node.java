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
package com.xcash.utils.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "nodes", indices = {@Index(value = {"symbol", "url"},unique = true)})
public class Node implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private int id;
    @ColumnInfo
    private String symbol;
    @ColumnInfo
    private String url;
    @ColumnInfo
    private String username;
    @ColumnInfo
    private String password;
    @ColumnInfo
    private boolean isActive;

    public Node() {

    }

    @Ignore
    public Node(String symbol, String url, String username, String password, boolean isActive) {
        this.symbol = symbol;
        this.url = url;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                '}';
    }

}
