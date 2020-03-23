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
