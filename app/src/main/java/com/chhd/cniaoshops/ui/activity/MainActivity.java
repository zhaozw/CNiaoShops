package com.chhd.cniaoshops.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.base.HideSoftInputActivity;
import com.chhd.cniaoshops.bean.Tab;
import com.chhd.cniaoshops.ui.fragment.CategoryFragment;
import com.chhd.cniaoshops.ui.fragment.HomeFragment;
import com.chhd.cniaoshops.ui.fragment.HotFragment;
import com.chhd.cniaoshops.ui.fragment.MineFragment;
import com.chhd.cniaoshops.global.AppApplication;
import com.chhd.cniaoshops.ui.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends HideSoftInputActivity {

    @BindView(android.R.id.tabcontent)
    FrameLayout frameLayout;
    @BindView(android.R.id.tabhost)
    FragmentTabHost tabHost;

    private List<Tab> tabs = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        context = this;

        AppApplication.isHotRun = true;

        initTab();

    }


    private void initTab() {

        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        tabs.add(new Tab(R.drawable.selector_icon_home, getString(R.string.tab_home), HomeFragment.class));
        tabs.add(new Tab(R.drawable.selector_icon_hot, getString(R.string.tab_hot), HotFragment.class));
        tabs.add(new Tab(R.drawable.selector_icon_category, getString(R.string.tab_classification), CategoryFragment.class));
        tabs.add(new Tab(R.drawable.selector_icon_cart, getString(R.string.tab_shopping_cart), CategoryFragment.class));
        tabs.add(new Tab(R.drawable.selector_icon_mine, getString(R.string.tab_me), MineFragment.class));

        for (Tab tab : tabs) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(tab.getTitle());
            tabSpec.setIndicator(getIndicator(tab));
            tabHost.addTab(tabSpec, tab.getFragment(), null);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        }

    }

    private View getIndicator(Tab tab) {
        View view = View.inflate(context, R.layout.tab_indicator, null);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        ivIcon.setImageResource(tab.getIcon());
        tvTitle.setText(tab.getTitle());
        return view;
    }
}
