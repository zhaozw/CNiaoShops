package com.chhd.cniaoshops.items;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chhd.cniaoshops.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by CWQ on 2017/3/21.
 */

public class ProgressItem extends AbstractFlexibleItem<ProgressItem.Holder> {


    @Override
    public boolean equals(Object o) {
        return false;
    }

    static final class Holder extends FlexibleViewHolder {

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.tv_none)
        TextView tvNone;

        public Holder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            ButterKnife.bind(this, view);
        }
    }
}
