package com.chhd.cniaoshops.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Andy on 2017/1/13.
 */

public interface OnItemClickListener {

    void onItemClick(RecyclerView.Adapter adapter, View parent, int position);

    void onItemChildClick(RecyclerView.Adapter adapter, View parent, View child, int position);
}
