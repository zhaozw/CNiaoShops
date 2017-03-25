package com.chhd.cniaoshops.ui.base;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chhd.cniaoshops.global.Constant;

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
