/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.models.local;

import java.util.List;

public class Setting {

    private String language;
    private List<String> searchHistorys;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getSearchHistorys() {
        return searchHistorys;
    }

    public void setSearchHistorys(List<String> searchHistorys) {
        this.searchHistorys = searchHistorys;
    }

}
