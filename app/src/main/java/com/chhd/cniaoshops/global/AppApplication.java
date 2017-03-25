package com.chhd.cniaoshops.global;

import android.app.Application;
import android.content.Context;

import com.chhd.cniaoshops.bean.User;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.yanzhenjie.nohttp.NoHttp;

/**
 * Created by CWQ on 2016/10/24.
 */
public class AppApplication extends Application implements Constant {

    public static Context context;
    public static boolean isHotStart;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        NoHttp.Config config = new NoHttp.Config()
                .setConnectTimeout(TIME_OUT)
                .setReadTimeout(TIME_OUT);

        NoHttp.initialize(this, config);

        com.yanzhenjie.nohttp.Logger.setDebug(true);
        com.yanzhenjie.nohttp.Logger.setTag(TAG_NOHTTP);

        com.orhanobut.logger.Logger.init(TAG).methodCount(3).methodOffset(1);

        Fresco.initialize(this);
    }
}
