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
package com.xcash.wallet.uihelp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xcash.adapters.listviewadapter.Menu_ListViewAdapter;
import com.xcash.adapters.listviewadapter.Normal_ListViewAdapter;
import com.xcash.adapters.listviewadapter.Search_History_ListViewAdapter;
import com.xcash.base.BaseActivity;
import com.xcash.base.utils.TimeTool;
import com.xcash.models.local.KeyValueItem;
import com.xcash.wallet.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class PopupWindowHelp {

    public static final String WINDOWTOKENISNULL = "windowTokenIsNull";
    public static final String POPUPWINDOWKEY = "popupWindowKey";

    public static void showPopupWindowPasswordHintTips(final BaseActivity baseActivity, View view, final View needEnableView, final String passwordHint) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(WINDOWTOKENISNULL);
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = baseActivity.getLayoutInflater();
        View popupWindowView = layoutInflater.inflate(R.layout.popupwindow_password_hint, null);
        int popupwindow_horizontal_margin = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_horizontal_margin);
        int popupWindowWidth = BaseActivity.getScreenWidth(baseActivity) - popupwindow_horizontal_margin * 2;
        final String popupWindowKey = POPUPWINDOWKEY + TimeTool.getOnlyTimeWithoutSleep();
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, popupWindowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                baseActivity.removePopupWindow(popupWindowKey);
            }
        });
        final TextView textViewPasswordHint = (TextView) popupWindowView.findViewById(R.id.textViewPasswordHint);
        final TextView textViewOK = (TextView) popupWindowView.findViewById(R.id.textViewOK);
        textViewPasswordHint.setText(passwordHint);
        textViewOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwindowNormalAnimationCenter);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        baseActivity.addPopupWindow(popupWindowKey, popupWindow);
    }

    public interface OnShowPopupWindowNormalTipsListener {

        void okClick(PopupWindow popupWindow, View view);

    }

    public static void showPopupWindowNormalTips(final BaseActivity baseActivity, View view, final View needEnableView, final String tips, final OnShowPopupWindowNormalTipsListener onShowPopupWindowNormalTipsListener) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(WINDOWTOKENISNULL);
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = baseActivity.getLayoutInflater();
        View popupWindowView = layoutInflater.inflate(R.layout.popupwindow_normal_tips, null);
        int popupwindow_horizontal_margin = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_horizontal_margin);
        int popupWindowWidth = BaseActivity.getScreenWidth(baseActivity) - popupwindow_horizontal_margin * 2;
        final String popupWindowKey = POPUPWINDOWKEY + TimeTool.getOnlyTimeWithoutSleep();
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, popupWindowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                baseActivity.removePopupWindow(popupWindowKey);
            }
        });
        final TextView textViewTips = (TextView) popupWindowView.findViewById(R.id.textViewTips);
        final TextView textViewCancel = (TextView) popupWindowView.findViewById(R.id.textViewCancel);
        final TextView textViewOK = (TextView) popupWindowView.findViewById(R.id.textViewOK);
        textViewTips.setText(tips);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        textViewOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (onShowPopupWindowNormalTipsListener != null) {
                    onShowPopupWindowNormalTipsListener.okClick(popupWindow, textViewOK);
                }
            }
        });
        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwindowNormalAnimationCenter);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        baseActivity.addPopupWindow(popupWindowKey, popupWindow);
    }

    public interface OnShowPopupWindowCustomTipsListener {

        void initView(PopupWindow popupWindow, TextView textViewTips,TextView textViewLeft,TextView textViewRight);

    }

    public static void showPopupWindowCustomTips(final BaseActivity baseActivity, View view, final View needEnableView, final OnShowPopupWindowCustomTipsListener onShowPopupWindowCustomTipsListener) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(WINDOWTOKENISNULL);
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = baseActivity.getLayoutInflater();
        View popupWindowView = layoutInflater.inflate(R.layout.popupwindow_normal_tips, null);
        int popupwindow_horizontal_margin = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_horizontal_margin);
        int popupWindowWidth = BaseActivity.getScreenWidth(baseActivity) - popupwindow_horizontal_margin * 2;
        final String popupWindowKey = POPUPWINDOWKEY + TimeTool.getOnlyTimeWithoutSleep();
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, popupWindowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                baseActivity.removePopupWindow(popupWindowKey);
            }
        });
        final TextView textViewTips = (TextView) popupWindowView.findViewById(R.id.textViewTips);
        final TextView textViewCancel = (TextView) popupWindowView.findViewById(R.id.textViewCancel);
        final TextView textViewOK = (TextView) popupWindowView.findViewById(R.id.textViewOK);
        if (onShowPopupWindowCustomTipsListener != null) {
            onShowPopupWindowCustomTipsListener.initView(popupWindow,textViewTips,textViewCancel,textViewOK);
        }

        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwindowNormalAnimationCenter);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        baseActivity.addPopupWindow(popupWindowKey, popupWindow);
    }


    public interface OnShowPopupWindowPasswordTipsListener {

        void okClick(PopupWindow popupWindow, EditText editTextPassword, View view);

    }

    public static void showPopupWindowPasswordTips(final BaseActivity baseActivity, View view, final View needEnableView, final OnShowPopupWindowPasswordTipsListener onShowPopupWindowPasswordTipsListener) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(WINDOWTOKENISNULL);
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = baseActivity.getLayoutInflater();
        View popupWindowView = layoutInflater.inflate(R.layout.popupwindow_password, null);
        int popupwindow_horizontal_margin = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_horizontal_margin);
        int popupWindowWidth = BaseActivity.getScreenWidth(baseActivity) - popupwindow_horizontal_margin * 2;
        final String popupWindowKey = POPUPWINDOWKEY + TimeTool.getOnlyTimeWithoutSleep();
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, popupWindowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                baseActivity.removePopupWindow(popupWindowKey);
            }
        });
        final EditText editTextPassword = (EditText) popupWindowView.findViewById(R.id.editTextPassword);
        final TextView textViewCancel = (TextView) popupWindowView.findViewById(R.id.textViewCancel);
        final TextView textViewOK = (TextView) popupWindowView.findViewById(R.id.textViewOK);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        textViewOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (onShowPopupWindowPasswordTipsListener != null) {
                    onShowPopupWindowPasswordTipsListener.okClick(popupWindow, editTextPassword, textViewOK);
                }
            }
        });
        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwindowNormalAnimationCenter);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        baseActivity.addPopupWindow(popupWindowKey, popupWindow);
    }

    public interface OnShowPopupWindowChooseDateListener {

        void OKClick(DatePicker datePicker, PopupWindow popupWindow, View view);

    }

    public static void showPopupWindowChooseDate(final BaseActivity baseActivity, View view, final View needEnableView, String dateSelect, final OnShowPopupWindowChooseDateListener onShowPopupWindowChooseDateListener) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(WINDOWTOKENISNULL);
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = baseActivity.getLayoutInflater();
        View popupWindowView = layoutInflater.inflate(R.layout.popupwindow_date, null);
        int popupwindow_horizontal_margin = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_horizontal_margin);
        int popupWindowWidth = BaseActivity.getScreenWidth(baseActivity) - popupwindow_horizontal_margin * 2;
        final String popupWindowKey = POPUPWINDOWKEY + TimeTool.getOnlyTimeWithoutSleep();
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, popupWindowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                baseActivity.removePopupWindow(popupWindowKey);
            }
        });
        final DatePicker datePicker = (DatePicker) popupWindowView.findViewById(R.id.datePicker);
        final TextView textViewOK = (TextView) popupWindowView.findViewById(R.id.textViewOK);
        TextView textViewCancel = (TextView) popupWindowView.findViewById(R.id.textViewCancel);
        if (dateSelect == null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateSelect = simpleDateFormat.format(System.currentTimeMillis());
        }
        int[] day = {1970, 0, 1};
        try {
            String[] strings = dateSelect.split("-");
            if (strings.length >= 3) {
                day[0] = Integer.valueOf(strings[0]);
                day[1] = Integer.valueOf(strings[1]) - 1;
                day[2] = Integer.valueOf(strings[2]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        datePicker.init(day[0], day[1], day[2], new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            }
        });
        textViewOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (onShowPopupWindowChooseDateListener != null) {
                    onShowPopupWindowChooseDateListener.OKClick(datePicker, popupWindow, textViewOK);
                }
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwindowNormalAnimationCenter);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        baseActivity.addPopupWindow(popupWindowKey, popupWindow);
    }

    public static void showPopupWindowMenuListViewMore(final BaseActivity baseActivity, View view, final View needEnableView, int rightX, int topY, List<KeyValueItem> keyValueItems, int showCount, final AdapterView.OnItemClickListener adapterViewOnItemClickListener) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(WINDOWTOKENISNULL);
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = baseActivity.getLayoutInflater();
        View popupWindowView = layoutInflater.inflate(R.layout.popupwindow_listview_menu, null);
        int popupwindow_menu_listview_more_string_item_height = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_menu_listview_more_string_item_height);
        final String popupWindowKey = POPUPWINDOWKEY + TimeTool.getOnlyTimeWithoutSleep();
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                baseActivity.removePopupWindow(popupWindowKey);
            }
        });
        ListView listView = (ListView) popupWindowView.findViewById(R.id.listView);
        int count = 0;
        if (keyValueItems != null) {
            count = keyValueItems.size();
        }
        if (count > showCount) {
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = popupwindow_menu_listview_more_string_item_height * showCount;
            listView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            listView.setLayoutParams(layoutParams);
        }
        Menu_ListViewAdapter menu_ListViewAdapter = new Menu_ListViewAdapter(baseActivity, keyValueItems, listView, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                if (adapterViewOnItemClickListener != null) {
                    adapterViewOnItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
        listView.setAdapter(menu_ListViewAdapter);
        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwindowNormalAnimationCenter);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
        if (rightX < 0) {
            rightX = 0;
        }
        if (topY < 0) {
            topY = 0;
        }
        popupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.TOP, rightX, topY);
        baseActivity.addPopupWindow(popupWindowKey, popupWindow);
    }

    public static void showPopupWindowSearchListViewHistory(final BaseActivity baseActivity, View view, final View needEnableView, int width, int leftX, int topY, List<String> searchHistorys, int showCount, final Search_History_ListViewAdapter.OnSearchHistoryListener onSearchHistoryListener) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(WINDOWTOKENISNULL);
            return;
        }
        if (width <= 0) {
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = baseActivity.getLayoutInflater();
        View popupWindowView = layoutInflater.inflate(R.layout.popupwindow_search_listview_history, null);
        int popupwindow_search_history_item_height = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_search_history_item_height);
        final String popupWindowKey = POPUPWINDOWKEY + TimeTool.getOnlyTimeWithoutSleep();
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                baseActivity.removePopupWindow(popupWindowKey);
            }
        });
        ListView listView = (ListView) popupWindowView.findViewById(R.id.listView);
        int count = 0;
        if (searchHistorys != null) {
            count = searchHistorys.size();
        }
        if (count > showCount) {
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = popupwindow_search_history_item_height * showCount;
            listView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            listView.setLayoutParams(layoutParams);
        }
        Search_History_ListViewAdapter search_History_ListViewAdapter = new Search_History_ListViewAdapter(baseActivity, searchHistorys, listView, new Search_History_ListViewAdapter.OnSearchHistoryListener() {
            @Override
            public void onItemClick(BaseAdapter baseAdapter, AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                if (onSearchHistoryListener != null) {
                    onSearchHistoryListener.onItemClick(baseAdapter, parent, view, position, id);
                }
            }

            @Override
            public void onDelete(BaseAdapter baseAdapter, View view, int position) {
                if (onSearchHistoryListener != null) {
                    onSearchHistoryListener.onDelete(baseAdapter, view, position);
                }
            }
        });
        listView.setAdapter(search_History_ListViewAdapter);
        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwindowNormalAnimationCenter);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
        if (leftX < 0) {
            leftX = 0;
        }
        if (topY < 0) {
            topY = 0;
        }
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.TOP, leftX, topY);
        baseActivity.addPopupWindow(popupWindowKey, popupWindow);
    }

    public static void showPopupWindowListViewNormal(final BaseActivity baseActivity, View view, final View needEnableView, int width, int leftX, int topY, List<KeyValueItem> keyValueItems, int showCount, final AdapterView.OnItemClickListener adapterViewOnItemClickListener) {
        if (view.getWindowToken() == null) {
            BaseActivity.showSystemErrorLog(WINDOWTOKENISNULL);
            return;
        }
        if (width <= 0) {
            return;
        }
        if (needEnableView != null) {
            needEnableView.setEnabled(false);
        }
        LayoutInflater layoutInflater = baseActivity.getLayoutInflater();
        View popupWindowView = layoutInflater.inflate(R.layout.popupwindow_listview_normal, null);
        int popupwindow_listview_normal_item_height = baseActivity.getResources().getDimensionPixelSize(
                R.dimen.popupwindow_listview_normal_item_height);
        final String popupWindowKey = POPUPWINDOWKEY + TimeTool.getOnlyTimeWithoutSleep();
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
                windowManagerLayoutParams.alpha = 1.0f;
                baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
                if (needEnableView != null) {
                    needEnableView.setEnabled(true);
                }
                baseActivity.removePopupWindow(popupWindowKey);
            }
        });
        ListView listView = (ListView) popupWindowView.findViewById(R.id.listView);
        int count = 0;
        if (keyValueItems != null) {
            count = keyValueItems.size();
        }
        if (count > showCount) {
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = popupwindow_listview_normal_item_height * showCount;
            listView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            listView.setLayoutParams(layoutParams);
        }
        Normal_ListViewAdapter normal_ListViewAdapter = new Normal_ListViewAdapter(baseActivity, keyValueItems, listView, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                if (adapterViewOnItemClickListener != null) {
                    adapterViewOnItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
        listView.setAdapter(normal_ListViewAdapter);
        ColorDrawable colorDrawable = new ColorDrawable(Color.argb(0, 255, 255, 255));
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.popwindowNormalAnimationCenter);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        WindowManager.LayoutParams windowManagerLayoutParams = baseActivity.getWindow().getAttributes();
        windowManagerLayoutParams.alpha = 0.7f;
        baseActivity.getWindow().setAttributes(windowManagerLayoutParams);
        if (leftX < 0) {
            leftX = 0;
        }
        if (topY < 0) {
            topY = 0;
        }
        popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.TOP, leftX, topY);
        baseActivity.addPopupWindow(popupWindowKey, popupWindow);
    }

    public static String convertDate(DatePicker datePicker) {
        int year = datePicker.getYear();
        int month = datePicker.getMonth() + 1;
        int day = datePicker.getDayOfMonth();
        String monthString = String.valueOf(month);
        if (month < 10) {
            monthString = "0" + month;
        }
        String dayString = String.valueOf(day);
        if (day < 10) {
            dayString = "0" + day;
        }
        String date = year + "-" + monthString + "-" + dayString;
        return date;
    }

}
