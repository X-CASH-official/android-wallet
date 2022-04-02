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
package com.xcash.models.local;

import java.util.List;

public class Setting {

    private String language;
    private List<String> searchHistorys;
    private boolean closeServerWhenExit;

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

    public boolean isCloseServerWhenExit() {
        return closeServerWhenExit;
    }

    public void setCloseServerWhenExit(boolean closeServerWhenExit) {
        this.closeServerWhenExit = closeServerWhenExit;
    }
}
