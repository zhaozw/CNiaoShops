package com.chhd.cniaoshops.global;

/**
 * Created by CWQ on 2016/11/18.
 */

public interface Constant {

    String TAG = "debug_Logger";
    String TAG_NOHTTP = "debug_NoHttp";

    String SERVER_URL = "http://112.124.22.238:8081/course_api/";

    int TIME_OUT = 5 * 1000;

    int delayMillis = 100;

    int[] SWIPE_REFRESH_LAYOUT_COLORS = new int[]{
            android.R.color.holo_green_light,
            android.R.color.holo_blue_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
    };
}
