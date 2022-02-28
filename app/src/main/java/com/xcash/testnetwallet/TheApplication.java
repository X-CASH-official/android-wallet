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
package com.xcash.testnetwallet;

import android.app.Activity;
import android.app.Application;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.xcash.base.BaseActivity;
import com.xcash.base.recyclerviewlibrary.adapters.BaseRecyclerViewAdapter;
import com.xcash.base.recyclerviewlibrary.adapters.LoadMoreLayerRecyclerViewAdapter;
import com.xcash.base.recyclerviewlibrary.adapters.LoadMoreRecyclerViewAdapter;
import com.xcash.base.utils.ActivityManager;
import com.xcash.base.utils.CatchBugManager;
import com.xcash.base.utils.FileTool;
import com.xcash.base.utils.MD5Tool;
import com.xcash.models.local.Setting;
import com.xcash.utils.WalletServiceHelper;
import com.xcash.views.tablayout.TabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TheApplication extends Application {

    public static final String THEAPPLICATIONKEY = "theApplication";
    public static final String DEFAULT_ROOT_PATH = "xcash";
    public static final String DOWNLOADCACHE_DIR_NAME = "downloadCache";
    public static final String SETTING_LASTFILENAME = "setting.txt";
    public static final int AUTOREFRESHDELAY = 350;
    public static final int DEFAULT_SQLDELAY = 350;
    public static final int DEFAULT_WALLETOPERATEDELAY = 400;


    private static TheApplication theApplication;
    private static String filesDirRootPath = null;
    private static Setting setting;
    private WalletServiceHelper walletServiceHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        //LogTool.setDebug(true);
        CatchBugManager.getInstance(this);
        walletServiceHelper = new WalletServiceHelper(this);
        walletServiceHelper.bindService();
        theApplication = this;
    }

    public static TheApplication getTheApplication() {
        return theApplication;
    }

    public WalletServiceHelper getWalletServiceHelper() {
        return walletServiceHelper;
    }

    public static synchronized String getFilesDirRootPath() {
        if (filesDirRootPath == null) {
            File fileCacheDir = new File(theApplication.getFilesDir(), DEFAULT_ROOT_PATH);
            filesDirRootPath = fileCacheDir.getAbsolutePath();
            if (!fileCacheDir.exists()) {
                FileTool.mkdirs(filesDirRootPath);
            }
        }
        return filesDirRootPath;
    }

    public static synchronized String getExternalFilesDirRootPath() {
        return theApplication.getExternalFilesDir(DEFAULT_ROOT_PATH).getAbsolutePath();
    }

    public static void throwIfNotOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("Must running on mainThread");
        }
    }

    /**
     * return NotNull
     */
    public static Setting getSetting() {
        throwIfNotOnMainThread();
        if (setting == null) {
            String rootPath = getFilesDirRootPath();
            if (rootPath != null) {
                String fileName = rootPath + File.separator + TheApplication.SETTING_LASTFILENAME;
                String settingWithJson = FileTool.readContent(fileName);
                if (settingWithJson != null) {
                    try {
                        setting = new Gson().fromJson(settingWithJson, Setting.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (setting == null) {
            setting = new Setting();
        }
        return setting;
    }

    public static void setAndWriteSetting(Setting newSetting) {
        throwIfNotOnMainThread();
        if (newSetting != null) {
            setting = newSetting;
            String settingWithJson = null;
            try {
                settingWithJson = new Gson().toJson(setting);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (settingWithJson != null) {
                String rootPath = getFilesDirRootPath();
                if (rootPath != null) {
                    String fileName = rootPath + File.separator + TheApplication.SETTING_LASTFILENAME;
                    FileTool.writeContent(fileName, settingWithJson, false);
                }
            }
        }
    }

    public static String getDownloadCacheFilePath(String url) {
        String downloadCacheFilePath = null;
        if (url == null) {
            return downloadCacheFilePath;
        }
        String md5 = MD5Tool.stringToMD5(url);
        String rootPath = getExternalFilesDirRootPath();
        if (md5 != null && rootPath != null) {
            String downloadCachePath = rootPath + File.separator + DOWNLOADCACHE_DIR_NAME;
            File fileDownloadCacheDir = new File(downloadCachePath);
            if (!fileDownloadCacheDir.exists()) {
                fileDownloadCacheDir.mkdir();
            }
            downloadCacheFilePath = downloadCachePath + File.separator + md5 + TheApplication.getUrlContentType(url);
        }
        return downloadCacheFilePath;
    }

    public static void setColorSchemeColors(BaseActivity baseActivity, SwipeRefreshLayout swipeRefreshLayout) {
        TypedArray typedArray = baseActivity.getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary, R.attr.mainColorDeepen});
        try {
            int colorPrimary = typedArray.getColor(0, 0xffffffff);
            int mainColorDeepen = typedArray.getColor(1, 0xffffffff);
            swipeRefreshLayout.setColorSchemeColors(colorPrimary, mainColorDeepen);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * return NotNull
     */
    public static String getUrlContentType(String url) {
        String contentType = "";
        int lastIndex = url.lastIndexOf(".");
        if (lastIndex != -1) {
            contentType = url.substring(lastIndex);
        }
        return contentType;
    }

    public static void setLayoutParams(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        boolean changed = false;
        if (width != -1 && layoutParams.width != width) {
            layoutParams.width = width;
            changed = true;
        }
        if (height != -1 && layoutParams.height != height) {
            layoutParams.height = height;
            changed = true;
        }
        if (changed) {
            view.setLayoutParams(layoutParams);
        }
    }

    public static void setLayoutMargins(View view, int top, int bottom, int left, int right) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLayoutParams = null;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        } else {
            marginLayoutParams = new ViewGroup.MarginLayoutParams(layoutParams);
        }
        boolean changed = false;
        if (top != -1 && marginLayoutParams.topMargin != top) {
            marginLayoutParams.topMargin = top;
            changed = true;
        }
        if (bottom != -1 && marginLayoutParams.bottomMargin != bottom) {
            marginLayoutParams.bottomMargin = bottom;
            changed = true;
        }
        if (left != -1 && marginLayoutParams.leftMargin != left) {
            marginLayoutParams.leftMargin = left;
            changed = true;
        }
        if (right != -1 && marginLayoutParams.rightMargin != right) {
            marginLayoutParams.rightMargin = right;
            changed = true;
        }
        if (changed) {
            view.setLayoutParams(marginLayoutParams);
        }
    }

    public static void setCursorToLast(EditText editText) {
        CharSequence charSequence = editText.getText();
        if (charSequence != null && charSequence instanceof Spannable) {
            Spannable spannable = (Spannable) charSequence;
            Selection.setSelection(spannable, spannable.length());
        }
    }

    public static <T> void addAllFormBaseRecyclerViewAdapter(BaseRecyclerViewAdapter baseRecyclerViewAdapter, List<T> newDatas) {
        if (baseRecyclerViewAdapter != null) {
            baseRecyclerViewAdapter.addAll(newDatas);
        }
    }

    public static <T> void replaceAllFormBaseRecyclerViewAdapter(BaseRecyclerViewAdapter baseRecyclerViewAdapter, List<T> newDatas, RecyclerView recyclerView) {
        if (baseRecyclerViewAdapter != null) {
            baseRecyclerViewAdapter.replaceAllUseNotifyDataSetChangedWithScrollTop(newDatas, recyclerView);
        }
    }

    public static <T> void loadMoreErrorFormBaseRecyclerViewAdapter(BaseRecyclerViewAdapter baseRecyclerViewAdapter) {
        if (baseRecyclerViewAdapter != null) {
            if (baseRecyclerViewAdapter instanceof LoadMoreRecyclerViewAdapter) {
                LoadMoreRecyclerViewAdapter loadMoreRecyclerViewAdapter = (LoadMoreRecyclerViewAdapter) baseRecyclerViewAdapter;
                loadMoreRecyclerViewAdapter.loadMoreError();
            } else if (baseRecyclerViewAdapter instanceof LoadMoreLayerRecyclerViewAdapter) {
                LoadMoreLayerRecyclerViewAdapter loadMoreLayerRecyclerViewAdapter = (LoadMoreLayerRecyclerViewAdapter) baseRecyclerViewAdapter;
                loadMoreLayerRecyclerViewAdapter.loadMoreError();
            }
        }
    }

    public static Activity getActivityFromActivityManager(String className) {
        Activity activity = null;
        ArrayList<Object[]> arrayListActivitys = ActivityManager.getInstance().getTheActivitysByClassName(className);
        if (arrayListActivitys != null && arrayListActivitys.size() > 0 && arrayListActivitys.get(0) != null) {
            activity = (Activity) (arrayListActivitys.get(0)[1]);
        }
        return activity;
    }

    public static void killActivityFromActivityManager(String className) {
        ArrayList<Object[]> arrayListActivitys = ActivityManager.getInstance().getTheActivitysByClassName(className);
        for (int i = 0; i < arrayListActivitys.size(); i++) {
            ((Activity) (arrayListActivitys.get(i)[1])).finish();
        }
    }

    public static void cancelAllDialogFromActivityManager() {
        ArrayList<BaseActivity> arrayListBaseActivitys = ActivityManager.getInstance().getAllBaseActivitys();
        for (int i = 0; i < arrayListBaseActivitys.size(); i++) {
            arrayListBaseActivitys.get(i).dismissAndRemoveAllProgressDialog();
        }
    }

    public static void killAllActivityExceptMe(String activityKey) {
        ActivityManager.getInstance().killAllActivityWithOutMe(activityKey);
    }

    public static void refreshPageScrolled(Handler handler, final TabLayout tabLayout) {
        if (handler != null && tabLayout != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    tabLayout.refreshPageScrolled();
                }
            });
        }
    }

}
