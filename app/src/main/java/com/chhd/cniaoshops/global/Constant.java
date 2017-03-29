package com.chhd.cniaoshops.global;

import com.chhd.cniaoshops.R;

/**
 * Created by CWQ on 2016/11/18.
 */

public interface Constant {

    String TAG = "debug_Logger";
    String TAG_NOHTTP = "debug_NoHttp";
    String TAG_OKGO = "debug_OkGo";

    String SERVER_URL = "http://112.124.22.238:8081/course_api/";

    int TIME_OUT = 5 * 1000;

    int DELAYMILLIS_FOR_RQUEST_FINISH = 650;
    int DELAYMILLIS_FOR_SHOW_EMPTY = 100;

    int BANNER_DESCRIPTION_LAYOUT_HEIGHT = 30;

    int placeholderResId = R.drawable.empty_photo;

    int[] SWIPE_REFRESH_LAYOUT_COLORS = new int[]{
            android.R.color.holo_green_light,
            android.R.color.holo_blue_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
    };
}
