package com.chhd.cniaoshops.ui.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Wares;
import com.chhd.cniaoshops.ui.base.SimpleAdapter;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CWQ on 2017/3/27.
 */

public class WaresAdapter extends SimpleAdapter<Wares, BaseViewHolder> {

    public WaresAdapter(List<Wares> data) {
        super(R.layout.grid_item_wares, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Wares item) {
        Holder holder = new Holder(helper.itemView);
        Picasso
                .with(mContext)
                .load(item.getImgUrl())
                .into(holder.ivPic);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("￥%.2f", item.getPrice()));
    }

    class Holder {

        @BindView(R.id.iv_pic)
        ImageView ivPic;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;

        public Holder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
