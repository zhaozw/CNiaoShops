package com.chhd.cniaoshops.http;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.ToastyUtils;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by CWQ on 2017/3/26.
 */

public abstract class SimpleCallback extends StringCallback {

    @Override
    public void onSuccess(String s, Call call, Response response) {
        LoggerUtils.d(response, s);
        success(s, call, response);
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        LoggerUtils.e(e);
        ToastyUtils.error(R.string.network_connect_fail);
        error(call, response, e);

    }

    public abstract void success(String s, Call call, Response response);

    public void error(Call call, Response response, Exception e) {
    }
}
