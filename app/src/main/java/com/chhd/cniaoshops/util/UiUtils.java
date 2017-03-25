package com.chhd.cniaoshops.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;

import com.chhd.cniaoshops.global.AppApplication;

/**
 * Created by CWQ on 2016/10/28.
 */
public class UiUtils {

    private UiUtils() {
    }

    public static Context getContext() {
        return AppApplication.context;
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static int dp2px(float dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public static int getScreenWidth() {
        int widthPixels = getContext().getResources().getDisplayMetrics().widthPixels;
        return widthPixels;
    }

    public static String getString(int resId) {
        return getContext().getString(resId);
    }

    public static String[] getStringArray(int id) {
        return getContext().getResources().getStringArray(id);
    }

    public static int getColor(int id) {
        return getResources().getColor(id);
    }

    public static int getTextSize(int id) {
        TypedValue value = new TypedValue();
        UiUtils.getResources().getValue(id, value, true);
        return (int) TypedValue.complexToFloat(value.data);
    }
}
