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
package com.xcash.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;

import com.xcash.models.local.Setting;
import com.xcash.wallet.TheApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageTool {

    public static final String LANGUAGE_ENGLISH = "ENGLISH";
    public static final String LANGUAGE_CHINESE = "简体中文";

    public static Context initAppLanguage(Context context, String language) {
        if (language == null) {
            return context;
        }
        try {
            String localLanguage = null;
            if (language.equals(LANGUAGE_ENGLISH)) {
                localLanguage = Locale.ENGLISH.getLanguage();
            } else if (language.equals(LANGUAGE_CHINESE)) {
                localLanguage = Locale.CHINESE.getLanguage();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return createConfiguration(context, localLanguage);
            } else {
                updateConfiguration(context, localLanguage);
                return context;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static Context createConfiguration(Context context, String language) {
        if (language == null) {
            return context;
        }
        Resources resources = context.getResources();
        Locale locale = new Locale(language);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        return context.createConfigurationContext(configuration);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void updateConfiguration(Context context, String language) {
        if (language == null) {
            return;
        }
        Resources resources = context.getResources();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public static String getCurrentLanguage(Context context) {
        String language;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            language = context.getResources().getConfiguration().locale.getLanguage();
        }
        return language;
    }

    /**
     * return NotNull
     */
    public static Object[] getLanguagesInfo(Context context) {
        Setting setting = TheApplication.getSetting();
        String language = setting.getLanguage();
        if (language == null) {
            String localLanguage = getCurrentLanguage(context);
            if (localLanguage != null && localLanguage.equals(Locale.CHINESE.getLanguage())) {
                language = LANGUAGE_CHINESE;
            } else {
                language = LANGUAGE_ENGLISH;
            }
            setting.setLanguage(language);
            TheApplication.setAndWriteSetting(setting);
        }
        Object[] objects = new Object[3];
        List<String> languages = new ArrayList<>();
        languages.add(LanguageTool.LANGUAGE_ENGLISH);
        languages.add(LanguageTool.LANGUAGE_CHINESE);
        int selectPosition = 0;
        for (int i = 0; i < languages.size(); i++) {
            if (language != null && languages.get(i).equals(language)) {
                selectPosition = i;
                break;
            }
        }
        objects[0] = languages;
        objects[1] = selectPosition;
        objects[2] = language;
        return objects;
    }

    /**
     * return NotNull
     */
    public static String getSelectLanguage(Context context) {
        Setting setting = TheApplication.getSetting();
        String language = setting.getLanguage();
        if (language == null) {
            String localLanguage = getCurrentLanguage(context);
            if (localLanguage != null && localLanguage.equals(Locale.CHINESE.getLanguage())) {
                language = LANGUAGE_CHINESE;
            } else {
                language = LANGUAGE_ENGLISH;
            }
        }
        return language;
    }

    public static String getLocaleStringResource(Context context, String language, int resourceId) {
        Locale requestedLocale = Locale.ENGLISH;
        if (language != null && language.equals(LANGUAGE_CHINESE)) {
            requestedLocale = Locale.CHINESE;
        }
        String result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = new Configuration(context.getResources().getConfiguration());
            configuration.setLocale(requestedLocale);
            result = context.createConfigurationContext(configuration).getText(resourceId).toString();
        } else {
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();
            Locale savedLocale = configuration.locale;
            configuration.locale = requestedLocale;
            resources.updateConfiguration(configuration, null);
            result = resources.getString(resourceId);
            configuration.locale = savedLocale;
            resources.updateConfiguration(configuration, null);
        }
        return result;
    }

}
