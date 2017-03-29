package com.chhd.cniaoshops.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Banner;
import com.chhd.cniaoshops.bean.HomeCampaign;
import com.chhd.cniaoshops.bean.HomeCategory;
import com.chhd.cniaoshops.biz.BannerBiz;
import com.chhd.cniaoshops.http.OnResponse;
import com.chhd.cniaoshops.ui.adapter.HomeCategoryAdapter;
import com.chhd.cniaoshops.ui.base.BaseFragment;
import com.chhd.cniaoshops.ui.decoration.SpaceItemDecoration;
import com.chhd.cniaoshops.util.UiUtils;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CWQ on 2016/10/24.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private View header;

    private List<HomeCategory> data = new ArrayList<>();
    private List<Banner> banners = new ArrayList<>();
    private List<HomeCampaign> campaigns = new ArrayList<>();
    private HomeCategoryAdapter adapter;
    private HomeFragment instance = this;
    private View empty;
    private SliderLayout sliderLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_home, null);

        ButterKnife.bind(this, view);

        initView();

        refresh();

        return view;
    }

    private void requestHomeCampaign() {

        String url = SERVER_URL + "campaign/recommend";

        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);

        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, request, new OnResponse<String>() {

            @Override
            public void succeed(int what, Response<String> response) {
                Type type = new TypeToken<List<HomeCampaign>>() {
                }.getType();
                List<HomeCampaign> list = new Gson().fromJson(response.get(), type);
                showHomeCampaign(list);
            }

            @Override
            public void finish(int what) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.setCustomEmptyView(recyclerView);
                }
            }
        });
    }

    private void showHomeCampaign(List<HomeCampaign> homeCampaigns) {
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

//    private void requestBannerImages() {
//
//        String url = "http://112.124.22.238:8081/course_api/banner/query";
//
//        OkHttpClient client = new OkHttpClient();
//
//        RequestBody body = new FormBody
//                .Builder()
//                .add("type", "1")
//                .build();
//
//        Request request = new Request
//                .Builder()
//                .url(url)
//                .post(body)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LoggerUtils.e(e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String json = response.body().string();
//                    Type type = new TypeToken<List<Banner>>() {
//                    }.getType();
//                    banners = new Gson().fromJson(json, type);
//                }
//            }
//        });
//    }

    private void initView() {

        initSliderLayout();

        initRecyclerView();

        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(SWIPE_REFRESH_LAYOUT_COLORS);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    private void refresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });
    }

    private void initRecyclerView() {

        adapter = new HomeCategoryAdapter(campaigns);
        adapter.addHeaderView(header);

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

        header = View.inflate(getActivity(), R.layout.header_banner, null);
        sliderLayout = ButterKnife.findById(header, R.id.slider_layout);

        View indicators = View.inflate(getActivity(), R.layout.indicators_bird, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, UiUtils.dp2px(BANNER_DESCRIPTION_LAYOUT_HEIGHT));
        params.alignWithParent = true;
        params.rightMargin = UiUtils.dp2px(10);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ((RelativeLayout) header).addView(indicators, params);
        PagerIndicator pagerIndicator = ButterKnife.findById(header, R.id.pager_indicator);
        sliderLayout.setCustomIndicator(pagerIndicator);

        sliderLayout.startAutoCycle(5000, 5000, true);

        List<BaseSliderView> banners = new BannerBiz(getActivity()).getBanner();
        for (BaseSliderView bannser : banners) {
            sliderLayout.addSlider(bannser);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sliderLayout.stopAutoCycle();
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            requestHomeCampaign();
        }
    };
}
