package com.chhd.cniaoshops.http;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.util.DialogUtils;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.ToastyUtils;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by CWQ on 2017/3/21.
 */

public abstract class OnResponse<T> implements OnResponseListener<T>, Constant {

    private boolean isToastError = true;
    private int delayMillis = DELAYMILLIS_FOR_RQUEST_FINISH;
    private long startTimeMillis;
    private Context progressDialog;
    private MaterialDialog dialog;

    public OnResponse() {
    }

    public OnResponse(boolean isToastError) {
        this.isToastError = isToastError;
    }

    public OnResponse(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public OnResponse(Context progressDialog) {
        this.progressDialog = progressDialog;
    }

    @Override
    public void onStart(int what) {
        startTimeMillis = System.currentTimeMillis();
        if (progressDialog != null && progressDialog instanceof Activity) {
            dialog = DialogUtils.newProgressDialog(progressDialog);
            dialog.show();
        }
        start(what);
    }

    @Override
    public void onSucceed(final int what, final Response response) {
        LoggerUtils.d(response);
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            succeed(what, response);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    succeed(what, response);
                }
            }, delayMillis - timeDif);
        }
    }

    @Override
    public void onFailed(final int what, final Response response) {
        LoggerUtils.e(response);
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            failed(what, response);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    failed(what, response);
                }
            }, delayMillis - timeDif);
        }
    }

    @Override
    public void onFinish(final int what) {
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            finish(what);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish(what);
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }, delayMillis - timeDif);
        }
    }

    private long getTimeDif() {
        return System.currentTimeMillis() - startTimeMillis;
    }

    public void start(int what) {

    }

    public abstract void succeed(int what, Response<T> response);

    public void failed(int what, Response<T> response) {
        if (isToastError) {
            ToastyUtils.error(R.string.network_connect_fail);
        }
    }

    public void finish(int what) {
    }
}
