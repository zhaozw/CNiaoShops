package com.chhd.cniaoshops.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.util.UiUtils;

import java.util.List;

/**
 * Created by CWQ on 2017/3/26.
 */

public abstract class SimpleAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    public SimpleAdapter(List<T> data) {
        super(data);
        openLoadAnimation();
    }

    public SimpleAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
        openLoadAnimation();
    }

    public void setCustomEmptyView() {
        if (getEmptyView() == null) {
            View emptyView = View.inflate(mContext, R.layout.view_empty, null);
            super.setEmptyView(emptyView);
        }
    }

    /**
     * 设置包含头部的空布局
     *
     * @param recyclerView
     */
    public void setCustomEmptyView(RecyclerView recyclerView) {
        if (getEmptyView() == null) {
            int height = recyclerView.getHeight() - getHeaderLayout().getHeight() - UiUtils.getStatusBarHeight();
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            View emptyView = View.inflate(mContext, R.layout.view_empty, null);
            emptyView.setLayoutParams(params);
            setHeaderAndEmpty(true);
            setEmptyView(emptyView);
        }
    }
}
