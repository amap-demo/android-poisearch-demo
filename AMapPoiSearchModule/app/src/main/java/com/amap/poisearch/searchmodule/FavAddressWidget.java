package com.amap.poisearch.searchmodule;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.amap.poisearch.R;

/**
 * Created by liangchao_suxun on 2017/4/26.
 */

public class FavAddressWidget extends RelativeLayout implements View.OnClickListener {

    public static int HOME_TYPE = 0;
    public static int COMP_TYPE = 1;

    private FavAddressItemWidget mHome;
    private FavAddressItemWidget mComp;

    public FavAddressWidget(Context context) {
        super(context);
        init();
    }

    public FavAddressWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FavAddressWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_fav_address, this);

        mHome = (FavAddressItemWidget)findViewById(R.id.home_fav_address_item);
        mComp = (FavAddressItemWidget)findViewById(R.id.comp_fav_address_item);

        mHome.setOnClickListener(this);
        mComp.setOnClickListener(this);

        mHome.setType(HOME_TYPE);
        mComp.setType(COMP_TYPE);

        mHome.setSubTitle(null);
        mComp.setSubTitle(null);
    }

    public void setVisible(boolean isVisible) {
        View subView = getChildAt(0);
        if (isVisible) {
            subView.setVisibility(View.VISIBLE);
        } else {
            subView.setVisibility(View.GONE);
        }
    }

    public void setHomeAddr(String addr) {
        if (TextUtils.isEmpty(addr)) {
            return;
        }

        mHome.setSubTitle(addr);
    }

    public void setCompAddr(String addr) {
        if (TextUtils.isEmpty(addr)) {
            return;
        }

        mComp.setSubTitle(addr);
    }

    private PoiListAdapter.Callback mCallback = null;

    public void setCallback(PoiListAdapter.Callback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onClick(View v) {
        if (mCallback == null) {
            return;
        }

        if (v.getId() == R.id.home_fav_address_item) {
            if (mHome.isAddressEmpty()) {
                mCallback.onFavAddressClick(PoiListAdapter.Callback.HOME_MODE, PoiListAdapter.Callback.CHANGE_MODE);
            } else {
                mCallback.onFavAddressClick(PoiListAdapter.Callback.HOME_MODE, PoiListAdapter.Callback.SEL_MODE);
            }
        } else {
            if (mComp.isAddressEmpty()) {
                mCallback.onFavAddressClick(PoiListAdapter.Callback.COMP_MODE, PoiListAdapter.Callback.CHANGE_MODE);
            } else {
                mCallback.onFavAddressClick(PoiListAdapter.Callback.COMP_MODE, PoiListAdapter.Callback.SEL_MODE);
            }
        }

    }
}

