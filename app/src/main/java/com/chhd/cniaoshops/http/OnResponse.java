package com.chhd.cniaoshops.http;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.ToastyUtils;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by CWQ on 2017/3/21.
 */

public abstract class OnResponse<T> implements OnResponseListener {

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onSucceed(int what, Response response) {
        LoggerUtils.d(response);

        onSuccess(what, response);
    }

    @Override
    public void onFailed(int what, Response response) {
        LoggerUtils.e(response);
        ToastyUtils.error(R.string.network_connect_fail);
    }

    @Override
    public void onFinish(int what) {

    }

    public abstract void onSuccess(int what, Response<T> response);
}
