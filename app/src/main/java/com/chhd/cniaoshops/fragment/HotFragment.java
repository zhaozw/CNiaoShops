package com.chhd.cniaoshops.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.adapter.HotWaresAdapter;
import com.chhd.cniaoshops.base.BaseFragment;
import com.chhd.cniaoshops.bean.Page;
import com.chhd.cniaoshops.bean.Wares;
import com.chhd.cniaoshops.clazz.SpaceItemDecoration;
import com.chhd.cniaoshops.http.OnResponse;
import com.chhd.cniaoshops.items.HotWaresItem;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.UiUtils;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
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
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by CWQ on 2016/10/24.
 */
public class HotFragment extends BaseFragment {

    @BindView(R.id.refresh_layout)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<AbstractFlexibleItem> items = new ArrayList<>();
    private HotWaresAdapter adatper;
    private int curPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private StatusEnum state = StatusEnum.STATE_NORMAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = UiUtils.inflate(R.layout.fragment_hot);

        ButterKnife.bind(this, view);

        refreshLayout.setSunStyle(true);
        refreshLayout.setMaterialRefreshListener(materialRefreshListener);
        refreshLayout.autoRefresh();
        refreshLayout.setLoadMore(true);
        refreshLayout.setProgressColors(getProgressColors());

        adatper = new HotWaresAdapter(items);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adatper);
        recyclerView.addItemDecoration(new SpaceItemDecoration(UiUtils.dp2px(10), true));

        return view;
    }

    private int[] getProgressColors() {

        int[] colors = new int[SWIPE_REFRESH_LAYOUT_COLORS.length];

        for (int i = 0; i < SWIPE_REFRESH_LAYOUT_COLORS.length; i++) {
            colors[i] = UiUtils.getColor(SWIPE_REFRESH_LAYOUT_COLORS[i]);
        }

        return colors;
    }

    FlexibleAdapter.EndlessScrollListener endlessScrollListener = new FlexibleAdapter.EndlessScrollListener() {

        @Override
        public void noMoreLoad(int newItemsSize) {

        }

        @Override
        public void onLoadMore(int lastPosition, int currentPage) {

        }
    };


    MaterialRefreshListener materialRefreshListener = new MaterialRefreshListener() {

        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            refreshData();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            super.onRefreshLoadMore(materialRefreshLayout);

        }
    };

    private void refreshData() {
        curPage = 1;
        state = StatusEnum.STATE_NORMAL;
        requestGetData();
    }

    private void loadMoreData() {
        curPage = ++curPage;
        state = StatusEnum.STATE_LOADMORE;
        requestGetData();
    }

    private void requestGetData() {

        String url = SERVER_URL + "wares/hot";

        final Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);

        request.add("curPage", curPage);
        request.add("pageSize", pageSize);

        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, request, new OnResponse<String>() {


            @Override
            public void onSuccess(int what, Response<String> response) {
                try {

                    Type type = new TypeToken<Page<Wares>>() {
                    }.getType();
                    Page<Wares> page = new Gson().fromJson(response.get(), type);

                    curPage = page.getCurrentPage();
                    totalPage =page.getTotalPage();

                } catch (Exception e) {
                    LoggerUtils.e(e);
                }
            }

            @Override
            public void onFinish(int what) {
                super.onFinish(what);

                refreshLayout.finishRefresh();
            }
        });

    }

    private void showData(Page<Wares> page) {
        switch (state) {
            case STATE_NORMAL:
                items.clear();
                List<Wares> list = page.getList();
                for (Wares wares : list) {
                    HotWaresItem item = new HotWaresItem(getActivity(), wares);
                    items.add(item);
                }
                break;
            case STATE_LOADMORE:

                break;
        }
        adatper.notifyDataSetChanged();
    }

    enum StatusEnum {
        STATE_NORMAL, STATE_LOADMORE
    }
}
