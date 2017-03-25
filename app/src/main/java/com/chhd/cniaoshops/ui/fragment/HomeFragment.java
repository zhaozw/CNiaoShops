package com.chhd.cniaoshops.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.ui.adapter.HomeCategoryAdapter;
import com.chhd.cniaoshops.ui.base.BaseFragment;
import com.chhd.cniaoshops.bean.Banner;
import com.chhd.cniaoshops.bean.HomeCampaign;
import com.chhd.cniaoshops.bean.HomeCategory;
import com.chhd.cniaoshops.ui.SpaceItemDecoration;
import com.chhd.cniaoshops.http.BaseCallback;
import com.chhd.cniaoshops.http.OkHttpHelper;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.UiUtils;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CWQ on 2016/10/24.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private View header;
    private SliderLayout sliderLayout;
    private PagerIndicator pagerIndicator;

    private List<HomeCategory> data = new ArrayList<>();
    private List<Banner> banners = new ArrayList<>();
    private int placeholderResId = R.drawable.empty_photo;
    private List<HomeCampaign> campaigns = new ArrayList<>();
    private HomeCategoryAdapter adapter;
    private HomeFragment instance = this;
    private TextView empty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_home, null);

        ButterKnife.bind(this, view);

        initView();

        requestHomeCampaign();

        return view;
    }

    private void requestHomeCampaign() {

        String url = SERVER_URL + "campaign/recommend";

        OkHttpHelper
                .getInstance()
                .get(url, new BaseCallback<List<HomeCampaign>>() {

                    @Override
                    public void onBeforeRequest(Request request) {

                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        LoggerUtils.e(e);
                    }

                    @Override
                    public void onResponse(Response response) {

                    }

                    @Override
                    public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                        if (!homeCampaigns.isEmpty()) {
                            for (int i = 0; i < homeCampaigns.size(); i++) {
                                if (i % 2 == 0) {
                                    homeCampaigns.get(i).setItemType(HomeCategory.TYPE_RIGHT);
                                } else {
                                    homeCampaigns.get(i).setItemType(HomeCategory.TYPE_LEFT);
                                }
                            }
                            instance.campaigns.clear();
                            instance.campaigns.addAll(homeCampaigns);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {
                        LoggerUtils.e(e);
                    }

                    @Override
                    public void onAfter(Response response, Exception e) {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        adapter.setEmptyView(empty);
                    }
                });


    }


    private void requestBannerImages() {

        String url = "http://112.124.22.238:8081/course_api/banner/query";

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody
                .Builder()
                .add("type", "1")
                .build();

        Request request = new Request
                .Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                LoggerUtils.e(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Type type = new TypeToken<List<Banner>>() {
                    }.getType();
                    banners = new Gson().fromJson(json, type);
                }
            }
        });
    }

    private void initView() {

        initSliderLayout();

        initRecyclerView();

        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(SWIPE_REFRESH_LAYOUT_COLORS);
        swipeRefreshLayout.setOnRefreshListener(new MyOnRefreshListener());
    }

    private void initRecyclerView() {

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtils.dp2px(40));
        empty = new TextView(getActivity());
        empty.setLayoutParams(params);
        empty.setText(R.string.no_more);
        empty.setGravity(Gravity.CENTER);
        empty.setTextColor(UiUtils.getColor(R.color.def_text_light));
        empty.setTextSize(TypedValue.COMPLEX_UNIT_SP, UiUtils.getTextSize(R.dimen.text_size_small));

        adapter = new HomeCategoryAdapter(campaigns);
        adapter.addHeaderView(header);
        adapter.setHeaderAndEmpty(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(UiUtils.dp2px(10)));
    }

    private void initHomeCategory() {

        data.add(new HomeCategory("热门活动", R.mipmap.img_big_1, R.mipmap.img_1_small1, R.mipmap.img_1_small2));
        data.add(new HomeCategory("有利可图", R.mipmap.img_big_4, R.mipmap.img_4_small1, R.mipmap.img_4_small2));
        data.add(new HomeCategory("品牌街", R.mipmap.img_big_2, R.mipmap.img_2_small1, R.mipmap.img_2_small2));
        data.add(new HomeCategory("金融街 包赚翻", R.mipmap.img_big_1, R.mipmap.img_3_small1, R.mipmap.imag_3_small2));
        data.add(new HomeCategory("超值购", R.mipmap.img_big_0, R.mipmap.img_0_small1, R.mipmap.img_0_small2));

        for (int i = 0; i < data.size(); i++) {
            if (i % 2 == 0) {
                data.get(i).setItemType(HomeCategory.TYPE_LEFT);
            } else {
                data.get(i).setItemType(HomeCategory.TYPE_RIGHT);
            }
        }

    }

    private void initSliderLayout() {

        header = View.inflate(getActivity(), R.layout.header_home, null);
        sliderLayout = ButterKnife.findById(header, R.id.slider_layout);
        pagerIndicator = ButterKnife.findById(header, R.id.pager_indicator);

        sliderLayout.setCustomIndicator(pagerIndicator);
        sliderLayout.startAutoCycle(5000, 5000, true);

        String[] imgUrls = UiUtils.getStringArray(R.array.banner_img_urls);
        String[] titles = UiUtils.getStringArray(R.array.banner_titles);

        for (int i = 0; i < imgUrls.length; i++) {
            TextSliderView sliderView = new TextSliderView(getActivity());
            sliderView.image(imgUrls[i]);
            sliderView.description(titles[i]);
            sliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderView.empty(placeholderResId);
            sliderView.error(placeholderResId);
            sliderLayout.addSlider(sliderView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sliderLayout.stopAutoCycle();
    }

    private class MyOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            requestHomeCampaign();
        }
    }
}
