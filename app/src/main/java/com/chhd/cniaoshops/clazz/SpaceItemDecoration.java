package com.chhd.cniaoshops.clazz;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Andy on 2016/11/7.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private int space;
    private int orientation = VERTICAL;
    private int padding;

    private boolean isEquidistance;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(int space, boolean isEquidistance) {
        this.space = space;
        this.isEquidistance = isEquidistance;
    }

    public SpaceItemDecoration(int space, int orientation) {
        this.space = space;
        this.orientation = orientation;
    }

    public SpaceItemDecoration(int space, int orientation, int padding) {
        this.space = space;
        this.orientation = orientation;
        this.padding = padding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (orientation == HORIZONTAL) {
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.set(space, 0, 0, 0);
            }
        } else {
            if (isEquidistance) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.set(space, 0, space, space);
                } else {
                    outRect.set(space, space, space, space);
                }
            } else {
                if (padding != 0) {
                    if (parent.getChildAdapterPosition(view) == 0) {
                        outRect.set(padding, padding, padding, 0);
                    } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
                        outRect.set(padding, space, padding, padding);
                    } else {
                        outRect.set(padding, space, padding, 0);
                    }

                } else {
                    if (parent.getChildAdapterPosition(view) != 0) {
                        outRect.set(0, space, 0, 0);
                    }
                }
            }
        }
    }

}
