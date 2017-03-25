package com.chhd.cniaoshops.items;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.bean.Wares;
import com.chhd.cniaoshops.util.LoggerUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by CWQ on 2017/3/15.
 */

public class HotWaresItem extends AbstractFlexibleItem<HotWaresItem.Holder> {

    private Context context;
    private Wares wares;

    public HotWaresItem(Wares wares) {
        this.wares = wares;
    }

    public HotWaresItem(Context context, Wares wares) {
        this.context = context;
        this.wares = wares;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.list_item_hot_wares;
    }

    @Override
    public Holder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new Holder(LayoutInflater.from(context).inflate(getLayoutRes(), parent, false), adapter);
    }


    @Override
    public void bindViewHolder(FlexibleAdapter adapter, Holder holder, int position, List payloads) {
        holder.ivPic.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.tvTitle.setText(wares.getName());
        holder.tvPrice.setText("" + wares.getPrice());
    }

    static final class Holder extends FlexibleViewHolder {

        @BindView(R.id.iv_pic)
        SimpleDraweeView ivPic;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.btn_buy)
        Button btnBuy;

        public Holder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            ButterKnife.bind(this, view);
        }
    }
}
