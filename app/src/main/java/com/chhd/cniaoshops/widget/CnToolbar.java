package com.chhd.cniaoshops.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chhd.cniaoshops.R;
import com.chhd.cniaoshops.util.UiUtils;

/**
 * Created by CWQ on 2016/11/15.
 */

public class CnToolbar extends Toolbar {

    private TextView tvTitle;
    private EditText etSearchView;
    private ImageButton ibRightButton;

    public CnToolbar(Context context) {
        this(context, null);
    }

    public CnToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CnToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews();

        setContentInsetsRelative(UiUtils.dp2px(10), UiUtils.dp2px(10));

        initAttrs(attrs);
    }

    private void initViews() {
        View view = View.inflate(getContext(), R.layout.toolbar_cn, null);
        tvTitle = (TextView) view.findViewById(R.id.toolbar_title);
        etSearchView = (EditText) view.findViewById(R.id.toolbar_searchview);
        ibRightButton = (ImageButton) view.findViewById(R.id.toolbar_rightButton);
        addView(view);
    }

    private void initAttrs(AttributeSet attrs) {
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), attrs, R.styleable.CnToolbar);

        Drawable icon = tintTypedArray.getDrawable(R.styleable.CnToolbar_rightButtonIcon);
        if (icon != null) {
            setRightButtonIcon(icon);
        }

        boolean isShowSearchView = tintTypedArray.getBoolean(R.styleable.CnToolbar_isShowSearchView, false);
        if (isShowSearchView) {
            showSearchView();
            hideTitleView();
        }
    }

    public void setRightButtonIcon(Drawable icon) {
        if (ibRightButton != null) {
            ibRightButton.setImageDrawable(icon);
        }
    }

    public void showSearchView() {
        if (etSearchView != null)
            etSearchView.setVisibility(VISIBLE);

    }


    public void hideSearchView() {
        if (etSearchView != null)
            etSearchView.setVisibility(INVISIBLE);
    }

    public void showTitleView() {
        if (tvTitle != null)
            tvTitle.setVisibility(VISIBLE);
    }


    public void hideTitleView() {
        if (tvTitle != null)
            tvTitle.setVisibility(INVISIBLE);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initViews();
        if (tvTitle != null) {
            tvTitle.setText(title);
            showTitleView();
        }
    }

    public void setRightButtonOnClickListener(OnClickListener onClickListener) {
        ibRightButton.setOnClickListener(onClickListener);
    }
}
