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
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by CWQ on 2017/3/26.
 */

public abstract class SimpleCallback extends StringCallback implements Constant {

    private boolean isToastError = true;
    private int delayMillis = DELAYMILLIS_FOR_RQUEST_FINISH;
    private long startTimeMillis;
    private Context progressDialog;
    private MaterialDialog dialog;

    public SimpleCallback() {
    }

    public SimpleCallback(boolean isToastError) {
        this.isToastError = isToastError;
    }

    public SimpleCallback(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public SimpleCallback(Context progressDialog) {
        this.progressDialog = progressDialog;
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        startTimeMillis = System.currentTimeMillis();
        if (progressDialog != null && progressDialog instanceof Activity) {
            dialog = DialogUtils.newProgressDialog(progressDialog);
            dialog.show();
        }
        before(request);
    }

    @Override
    public void onSuccess(final String s, final Call call, final Response response) {
        LoggerUtils.d(response, s);
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            success(s, call, response);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    success(s, call, response);
                }
            }, delayMillis - timeDif);
        }
    }

    @Override
    public void onError(final Call call, final Response response, final Exception e) {
        super.onError(call, response, e);
        LoggerUtils.e(e);
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            error(call, response, e);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    error(call, response, e);
                }
            }, delayMillis - timeDif);
        }
    }

    @Override
    public void onAfter(final String s, final Exception e) {
        super.onAfter(s, e);
        long timeDif = getTimeDif();
        if (timeDif > delayMillis) {
            after(s, e);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    after(s, e);
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

    public void before(BaseRequest request) {

    }

    public abstract void success(String s, Call call, Response response);

    public void error(Call call, Response response, Exception e) {
        ToastyUtils.error(R.string.network_connect_fail);
    }

    public void after(String s, Exception e) {

    }
}
