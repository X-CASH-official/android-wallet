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
package com.xcash.testnetwallet.uihelp;

import android.view.View;
import android.widget.TextView;

import com.xcash.base.BaseActivity;
import com.xcash.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.xcash.testnetwallet.R;

public class BaseRecyclerViewFromFrameLayoutHelp {

    public static void setEmptyTransactionTips(BaseActivity baseActivity, BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout) {
        View view = baseRecyclerViewFromFrameLayout.getFrameLayoutEmpty();
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(baseActivity.getString(R.string.layout_transaction_item_empty_tips));
    }

}
