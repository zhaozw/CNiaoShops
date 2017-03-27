package com.chhd.cniaoshops.ui.base;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by CWQ on 2017/3/26.
 */

public abstract class SimpleAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    public SimpleAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public void setEmptyView(View emptyView) {
    }
}
