package com.chhd.cniaoshops.ui.adapter;


import com.chad.library.adapter.base.BaseViewHolder;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Wares;
import com.chhd.cniaoshops.ui.base.SimpleAdapter;
import com.chhd.cniaoshops.util.LoggerUtils;

import java.util.List;

/**
 * Created by CWQ on 2017/3/27.
 */

public class WaresAdapter extends SimpleAdapter<Wares, BaseViewHolder> {

    public WaresAdapter(List<Wares> data) {
        super(R.layout.grid_item_wares, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Wares item) {
        try {

        } catch (Exception e) {
            LoggerUtils.e(e);
        }
    }
}
