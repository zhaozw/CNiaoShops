package com.chhd.cniaoshops.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Category;
import com.chhd.cniaoshops.bean.Page;
import com.chhd.cniaoshops.bean.Wares;
import com.chhd.cniaoshops.biz.BannerBiz;
import com.chhd.cniaoshops.http.OnResponse;
import com.chhd.cniaoshops.http.SimpleCallback;
import com.chhd.cniaoshops.ui.StatusEnum;
import com.chhd.cniaoshops.ui.adapter.CategoryAdapter;
import com.chhd.cniaoshops.ui.adapter.WaresAdapter;
import com.chhd.cniaoshops.ui.base.BaseFragment;
import com.chhd.cniaoshops.ui.decoration.GridSpaceItemDecoration;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.chhd.cniaoshops.util.UiUtils;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lzy.okgo.OkGo;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by CWQ on 2016/10/24.
 */
public class CategoryFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.lv_category)
    ListView lvCategory;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.rv_wares)
    RecyclerView rvWares;

    private List<Category> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private List<Wares> waresList = new ArrayList<>();
    private View header;
    private int curPage = 1;
    private int pageSize = 10;
    private WaresAdapter waresAdapter;
    private StatusEnum state;
    private int curIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.fragment_category, null);

        ButterKnife.bind(this, view);

        initView();

        requestCategoryData();

        return view;
    }

    private void initView() {
        categoryAdapter = new CategoryAdapter(getActivity(), categories);
        lvCategory.setAdapter(categoryAdapter);
        lvCategory.setOnItemClickListener(this);
    }

    private void requestWares(long categoryId) {

        final String url = SERVER_URL + "wares/list";

        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.add("categoryId", categoryId);
        request.add("curPage", curPage);
        request.add("pageSize", pageSize);
        request.setCacheMode(CacheMode.DEFAULT);

        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, request, new OnResponse<String>() {

            @Override
            public void succeed(int what, com.yanzhenjie.nohttp.rest.Response<String> response) {
                try {
                    Type type = new TypeToken<Page<Wares>>() {
                    }.getType();
                    Page<Wares> data = new Gson().fromJson(response.get(), type);
                    showWaresData(data);
                } catch (Exception e) {
                    LoggerUtils.e(e, response);
                }
            }

            @Override
            public void failed(int what, com.yanzhenjie.nohttp.rest.Response<String> response) {
                super.failed(what, response);
                waresList.clear();
                waresAdapter.notifyDataSetChanged();
                fail();
            }

            @Override
            public void finish(int what) {
                super.finish(what);
                refreshLayout.finishRefreshing();
                waresAdapter.setCustomEmptyView(rvWares);
            }
        });
    }

    private void fail() {
        switch (state) {
            case ON_NORMAL:
                waresAdapter.loadMoreComplete();
                break;
            case ON_LOAD_MORE:
                waresAdapter.loadMoreFail();
                break;
        }

    }

    private void refreshData() {
        curPage = 1;
        state = StatusEnum.ON_NORMAL;
        requestWares(categories.get(curIndex).getId());

    }

    private void loadMoreData() {
        curPage = ++curPage;
        state = StatusEnum.ON_LOAD_MORE;
        requestWares(categories.get(curIndex).getId());

    }

    private void showWaresData(Page<Wares> data) {
        switch (state) {
            case ON_NORMAL:
                waresList.clear();
                waresList.addAll(data.getList());
                waresAdapter.notifyDataSetChanged();
                waresAdapter.loadMoreComplete();
                break;
            case ON_LOAD_MORE:
                if (data.getList().isEmpty()) {
                    waresAdapter.loadMoreEnd();
                } else {
                    waresList.addAll(data.getList());
                    waresAdapter.notifyDataSetChanged();
                    waresAdapter.loadMoreComplete();
                }
                break;
        }
    }

    private RefreshListenerAdapter refreshListenerAdapter = new RefreshListenerAdapter() {

        @Override
        public void onRefresh(TwinklingRefreshLayout refreshLayout) {
            super.onRefresh(refreshLayout);
            refreshData();
        }
    };

    private void requestCategoryData() {

        final String url = SERVER_URL + "category/list";

        OkGo
                .post(url)
                .execute(new SimpleCallback(getActivity()) {

                    @Override
                    public void success(String s, Call call, Response response) {
                        try {
                            Type type = new TypeToken<List<Category>>() {
                            }.getType();
                            List<Category> data = new Gson().fromJson(s, type);
                            categoryAdapter.notifyDataChanged(data);
                            lvCategory.setItemChecked(0, true);
                        } catch (Exception e) {
                            LoggerUtils.e(e);
                        }
                    }

                    @Override
                    public void after(String s, Exception e) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int visibility = categories.isEmpty() ? View.VISIBLE : View.INVISIBLE;
                                emptyView.setVisibility(visibility);

                                if (!categories.isEmpty()) {
                                    initWaresLayout();
                                }
                            }
                        }, DELAYMILLIS_FOR_SHOW_EMPTY);
                    }
                });
    }

    private void initWaresLayout() {

        initSliderLayout();

        waresAdapter = new WaresAdapter(waresList);
        waresAdapter.addHeaderView(header);
        waresAdapter.setEnableLoadMore(true);
        waresAdapter.setLoadMoreView(moreView);
        waresAdapter.setOnLoadMoreListener(requestLoadMoreListener);

        rvWares.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvWares.setAdapter(waresAdapter);
        rvWares.addItemDecoration(new GridSpaceItemDecoration(2, UiUtils.dp2px(5), true, true));

        refreshLayout.setOnRefreshListener(refreshListenerAdapter);
        refreshLayout.startRefresh();
    }

    private BaseQuickAdapter.RequestLoadMoreListener requestLoadMoreListener = new BaseQuickAdapter.RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            loadMoreData();
        }
    };

    private LoadMoreView moreView = new LoadMoreView() {
        @Override
        public int getLayoutId() {
            return R.layout.view_load_more;
        }

        @Override
        protected int getLoadingViewId() {
            return R.id.progress_bar;
        }

        @Override
        protected int getLoadFailViewId() {
            return R.id.tv_fail;
        }

        @Override
        protected int getLoadEndViewId() {
            return R.id.tv_finish;
        }
    };

    private void initSliderLayout() {

        header = View.inflate(getActivity(), R.layout.header_banner, null);

        SliderLayout sliderLayout = ButterKnife.findById(header, R.id.slider_layout);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sliderLayout.getLayoutParams();
        params.height = UiUtils.dp2px(150);
        sliderLayout.setLayoutParams(params);

        View indicators = View.inflate(getActivity(), R.layout.indicators_default, null);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, UiUtils.dp2px(BANNER_DESCRIPTION_LAYOUT_HEIGHT));
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        curIndex = position;
        refreshLayout.startRefresh();
    }
}
