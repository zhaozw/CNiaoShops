package com.chhd.cniaoshops.base;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.chhd.cniaoshops.global.Constant;
import com.chhd.cniaoshops.util.LoggerUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements Constant {

    private List<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activities.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        activities.remove(this);
    }
}
