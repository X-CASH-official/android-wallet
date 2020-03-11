/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.models.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xcash.models.Delegate;

import java.util.List;

public class GetDelegates_GsonModel {

    public static List<Delegate> getData(String response) {
        List<Delegate> data = null;
        if (response != null) {
            try {
                data = new Gson().fromJson(response, new TypeToken<List<Delegate>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

}
