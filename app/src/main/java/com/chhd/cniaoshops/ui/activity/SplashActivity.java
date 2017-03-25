package com.chhd.cniaoshops.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.base.BaseActivity;
import com.chhd.cniaoshops.global.AppApplication;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @BindViews({R.id.animation_view_0, R.id.animation_view_1, R.id.animation_view_2})
    List<LottieAnimationView> animationViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        if (AppApplication.isHotStart) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            init();
        }
    }

    @Override
    public void onBackPressed() {
    }

    private void init() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animationViews.get(1).playAnimation();
            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animationViews.get(2).playAnimation();
            }
        }, 1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onStop() {
        super.onStop();

        animationViews.get(1).cancelAnimation();
        animationViews.get(2).cancelAnimation();
    }
}
