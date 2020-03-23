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
 */you may not use this file except in compliance with the License.
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
package com.xcash.base.recyclerviewlibrary.models;

import java.io.Serializable;

public class ViewItem implements Serializable {

    public static final int VIEW_TYPE_ITEM_LOAD_COMPLETED = -1;
    public static final int VIEW_TYPE_ITEM_LOAD_MORE = 0;
    public static final int VIEW_TYPE_ITEM_VIEWPAGER = 1;
    public static final int VIEW_TYPE_NORMAL_ITEM_TYPE1 = 2;
    public static final int VIEW_TYPE_NORMAL_ITEM_TYPE2 = 3;
    public static final int VIEW_TYPE_NORMAL_ITEM_TYPE3 = 4;
    public static final int VIEW_TYPE_NORMAL_ITEM_TYPE4 = 5;
    public static final int VIEW_TYPE_NORMAL_ITEM_TYPE5 = 6;

    protected int viewType;
    protected Object model;

    public ViewItem(int viewType, Object model) {
        this.viewType = viewType;
        this.model = model;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "ViewItem{" +
                "viewType=" + viewType +
                ", model=" + model +
                '}';
    }

}