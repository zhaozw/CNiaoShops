package com.chhd.cniaoshops.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chhd.cniaoshops.R;


public class DialogUtils {

    private DialogUtils() {
    }

    public static MaterialDialog.Builder newBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .titleColorRes(R.color.def_text_black)
                .contentColorRes(R.color.def_text_black)
                .widgetColorRes(R.color.colorAccent)
                .canceledOnTouchOutside(false);

    }

    public static MaterialDialog newProgressDialog(Context context) {
        return newBuilder(context)
                .content(R.string.please_waiting)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .build();
    }

}
