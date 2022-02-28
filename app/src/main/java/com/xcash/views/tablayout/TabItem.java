 /*
 * Copyright (C) 2019-2020 The X-Cash Foundation
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xcash.views.tablayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.TintTypedArray;

import com.xcash.testnetwallet.R;

public final class TabItem extends View {

    final CharSequence mText;
    final Drawable mIcon;
    final int mCustomLayout;

    public TabItem(Context context) {
        this(context, null);
    }

    @SuppressLint("RestrictedApi")
    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.TabItem);
        mText = a.getText(R.styleable.TabItem_android_text);
        mIcon = a.getDrawable(R.styleable.TabItem_android_icon);
        mCustomLayout = a.getResourceId(R.styleable.TabItem_android_layout, 0);
        a.recycle();
    }

}