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
package com.xcash.base.recyclerviewlibrary.models;

public class ViewLayerItem extends ViewItem {

    public static final int LOAD_COMPLETED = -1;
    public static final int LOAD_MORE = 0;
    public static final int LAYER_ROOT = 1;

    private int layer;
    private Object rootModel;

    public ViewLayerItem(int viewType, Object model, int layer, Object rootModel) {
        super(viewType, model);
        this.layer = layer;
        this.rootModel = rootModel;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public Object getRootModel() {
        return rootModel;
    }

    public void setRootModel(Object rootModel) {
        this.rootModel = rootModel;
    }

    @Override
    public String toString() {
        return "ViewLayerItem{" +
                "layer=" + layer +
                ", rootModel=" + rootModel +
                '}';
    }

}