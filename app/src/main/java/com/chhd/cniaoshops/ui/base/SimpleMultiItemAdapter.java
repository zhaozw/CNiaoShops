package com.chhd.cniaoshops.ui.base;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.util.LoggerUtils;

import java.util.List;

/**
 * Created by CWQ on 2017/3/26.
 */

public abstract class SimpleMultiItemAdapter<T extends MultiItemEntity, K extends BaseViewHolder> extends BaseMultiItemQuickAdapter<T, K> {

    public SimpleMultiItemAdapter(List<T> data) {
        super(data);
    }

    public void setEmptyView() {
        if (getEmptyView() == null) {
            View emptyView = View.inflate(mContext, R.layout.view_empty, null);
            super.setEmptyView(emptyView);
        }
    }
}
