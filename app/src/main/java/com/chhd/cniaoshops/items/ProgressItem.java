package com.chhd.cniaoshops.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.util.LoggerUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.Payload;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by CWQ on 2017/3/21.
 */

public class ProgressItem extends AbstractFlexibleItem<ProgressItem.Holder> {

    private StatusEnum status = StatusEnum.ON_LOAD;
    private Context context;
    private FlexibleAdapter adapter;

    public ProgressItem() {
    }

    public ProgressItem(Context context) {
        this.context = context;
    }

    public ProgressItem(Context context, FlexibleAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
        adapter.notifyItemChanged(adapter.getItemCount() - 1);

    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_load_more;
    }

    @Override
    public Holder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new Holder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, Holder holder, int position, List payloads) {

        switch (status) {
            case ON_LOAD:
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.tvFail.setVisibility(View.INVISIBLE);
                holder.tvNone.setVisibility(View.INVISIBLE);
                break;
            case ON_FINISH:
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.tvFail.setVisibility(View.INVISIBLE);
                holder.tvNone.setVisibility(View.VISIBLE);
                break;
            case ON_ERROR:
                holder.progressBar.setVisibility(View.INVISIBLE);
                holder.tvFail.setVisibility(View.VISIBLE);
                holder.tvNone.setVisibility(View.INVISIBLE);
                break;
        }
    }

    static final class Holder extends FlexibleViewHolder {

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.tv_fail)
        TextView tvFail;
        @BindView(R.id.tv_none)
        TextView tvNone;

        public Holder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            ButterKnife.bind(this, view);
        }
    }

    public enum StatusEnum {
        ON_LOAD,
        ON_FINISH,
        ON_ERROR
    }
}
