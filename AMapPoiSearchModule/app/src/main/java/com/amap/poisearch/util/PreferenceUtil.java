package com.amap.poisearch.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * Created by liangchao_suxun on 2017/4/27.
 */

public class PreferenceUtil {

    public static void save(Context context, String key, String val) {
        if (context == null) {
            return;
        }

        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(val)) {
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getClass().getPackage().getName(),
            Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static void save(Context context, String key, long val) {
        if (context == null) {
            return;
        }

        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getClass().getPackage().getName(),
            Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putLong(key, val);
        editor.commit();
    }

    public static String getStr(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getClass().getPackage().getName(),
            Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static long getLong(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getClass().getPackage().getName(),
            Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }

}
