package com.chhd.cniaoshops.util;

import com.chhd.cniaoshops.global.Config;
import com.chhd.cniaoshops.global.Constant;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.nohttp.rest.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by CWQ on 2016/11/2.
 */
public class LoggerUtils implements Constant {

    private static boolean isDebug = !Config.isRelease;

    private LoggerUtils() {

    }

    public static void v(String message) {
        if (isDebug) {
            Logger.v(message);
        }
    }

    public static void d(String message) {
        if (isDebug) {
            Logger.d(message);
        }
    }

    public static void d(Response<?> response) {
        if (isDebug) {
            String message =
                    "url:\t\t" + response.request().url()
                            + "\n\n"
                            + "params:\t" + response.request().getParamKeyValues().entrySet().toString().replace("[", "").replace("]", "").replace(", ", "\n" + "params:\t")
                            + "\n\n"
                            + "json:\t" + response.get();
            Logger.d(message);
        }
    }

    public static void d(okhttp3.Response response, String json) {
        if (isDebug) {
            try {
                String message =
                        "url:\t\t" + response.request().url()
                                + "\n\n"
                                + "json:\t" + json;
                Logger.d(message);
            } catch (Exception e) {
                Logger.e(e, "error");
            }
        }
    }

    public static void i(String message) {
        if (isDebug) {
            Logger.i(message);
        }
    }

    public static void e(Throwable throwable) {
        if (isDebug) {
            Logger.e(throwable, "error");
        }
    }

    public static void e(Throwable throwable, Response<?> response) {
        if (isDebug) {
            String message =
                    "url:\t\t" + response.request().url()
                            + "\n\n"
                            + "params:\t" + response.request().getParamKeyValues().entrySet().toString().replace("[", "").replace("]", "").replace(", ", "\n" + "params:\t")
                            + "\n\n"
                            + "json:\t" + response.get()
                            + "\n\n"
                            + "error";
            Logger.e(throwable, message);
        }
    }

    public static void e(String message) {
        if (isDebug) {
            Logger.e(message);
        }
    }

    public static void e(Response<?> response) {
        if (isDebug) {
            String message =
                    "url:\t\t" + response.request().url()
                            + "\n\n"
                            + "params:\t" + response.request().getParamKeyValues().entrySet().toString().replace("[", "").replace("]", "").replace(", ", "\n" + "params:\t")
                            + "\n\n"
                            + "json:\t" + response.get()
                            + "\n\n"
                            + "error";
            Logger.e(response.getException(), message);
        }
    }

}
