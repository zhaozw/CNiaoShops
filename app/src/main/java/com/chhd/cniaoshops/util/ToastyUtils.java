package com.chhd.cniaoshops.util;

import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Created by CWQ on 2017/3/21.
 */

public class ToastyUtils {

    private ToastyUtils() {
    }

    public static void error(CharSequence message) {
        Toasty.error(UiUtils.getContext(), message, Toast.LENGTH_SHORT, true).show();
    }

    public static void error(int resId) {
        Toasty.error(UiUtils.getContext(), UiUtils.getString(resId), Toast.LENGTH_SHORT, true).show();
    }
}
