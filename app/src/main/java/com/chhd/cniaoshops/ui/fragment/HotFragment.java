package com.chhd.cniaoshops.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Page;
import com.chhd.cniaoshops.bean.Wares;
import com.chhd.cniaoshops.http.OnResponse;
import com.chhd.cniaoshops.ui.decoration.SpaceItemDecoration;
import com.chhd.cniaoshops.ui.adapter.HotWaresAdapter;
import com.chhd.cniaoshops.ui.base.BaseFragment;
import com.chhd.cniaoshops.ui.items.HotWaresItem;
import com.chhd.cniaoshops.ui.items.ProgressItem;
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
import eu.davidea.fastscroller.FastScroller;
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
    @BindView(R.id.fast_scroller)
    FastScroller fastScroller;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;

    private List<AbstractFlexibleItem> items = new ArrayList<>();
    private HotWaresAdapter adatper;
    private int curPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;
    private StatusEnum state = StatusEnum.STATE_NORMAL;
    private ProgressItem progressItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_hot, null);

        ButterKnife.bind(this, view);

        refreshLayout.setSunStyle(true);
        refreshLayout.setMaterialRefreshListener(materialRefreshListener);
        refreshLayout.autoRefresh();
        refreshLayout.setProgressColors(getProgressColors());

        adatper = new HotWaresAdapter(items);
        progressItem = new ProgressItem(adatper, onClickListener);
        adatper.setEndlessScrollListener(endlessScrollListener, progressItem);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adatper);
        recyclerView.addItemDecoration(new SpaceItemDecoration(UiUtils.dp2px(10), true));

        adatper.setFastScroller(fastScroller, UiUtils.getColor(R.color.colorAccent));//Setup FastScroller after the Adapter has been added to the RecyclerView.
//        adatper.toggleFastScroller();

        return view;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressItem.setStatus(ProgressItem.StatusEnum.ON_LOAD);
            loadMoreData();
        }
    };

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
            progressItem.setStatus(ProgressItem.StatusEnum.ON_LOAD);
            loadMoreData();
        }
    };


    private MaterialRefreshListener materialRefreshListener = new MaterialRefreshListener() {

        @Override
        public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
            refreshData();
        }

        @Override
        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
            super.onRefreshLoadMore(materialRefreshLayout);
            if (curPage <= totalPage) {
                loadMoreData();
            } else {
                refreshLayout.finishRefreshLoadMore();
            }
        }
    };

    private void refreshData() {
        curPage = 1;
        state = StatusEnum.STATE_NORMAL;
        requestGetData();
    }

    private void loadMoreData() {
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
                    totalPage = page.getTotalPage();

                    curPage = ++curPage;

                    showData(page);

                } catch (Exception e) {
                    LoggerUtils.e(e);
                }
            }

            @Override
            public void onFail(int what, Response<String> response) {

                fail();
            }

            @Override
            public void onFinish(int what) {
                super.onFinish(what);
                finishRefresh();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int visibility = items.size() == 0 ? View.VISIBLE : View.INVISIBLE;
                        emptyView.setVisibility(visibility);
                    }
                }, delayMillis);

            }
        });

    }

    private void finishRefresh() {
        switch (state) {
            case STATE_NORMAL:
                refreshLayout.finishRefresh();
                break;
            case STATE_LOADMORE:
                refreshLayout.finishRefreshLoadMore();
                break;
        }
    }

    private void fail() {

        switch (state) {
            case STATE_NORMAL:

                break;
            case STATE_LOADMORE:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressItem.setStatus(ProgressItem.StatusEnum.ON_ERROR);
                    }
                }, 500);
                break;
        }

    }

    private void showData(Page<Wares> page) {
        switch (state) {
            case STATE_NORMAL: {
                items.clear();
                for (Wares wares : page.getList()) {
                    HotWaresItem item = new HotWaresItem(getActivity(), wares);
                    items.add(item);
                }
                adatper.notifyDataSetChanged();
            }
            break;
            case STATE_LOADMORE: {
                final List<AbstractFlexibleItem> newItems = new ArrayList<>();
                for (Wares wares : page.getList()) {
                    HotWaresItem item = new HotWaresItem(getActivity(), wares);
                    newItems.add(item);
                }

                if (newItems.size() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressItem.setStatus(ProgressItem.StatusEnum.ON_FINISH);
                            adatper.onLoadMoreComplete(newItems, 2000);
                        }
                    }, 500);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adatper.onLoadMoreComplete(newItems);
                        }
                    }, 500);
                }

            }
            break;
        }

    }

    enum StatusEnum {
        STATE_NORMAL, STATE_LOADMORE
    }
}
