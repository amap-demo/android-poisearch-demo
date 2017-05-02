package com.amap.poisearch.searchmodule;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.amap.api.services.core.PoiItem;
import com.amap.poisearch.R;
import com.amap.poisearch.searchmodule.ISearchModule.IDelegate;
import com.amap.poisearch.searchmodule.ISearchModule.IWidget;
import com.amap.poisearch.searchmodule.PoiListAdapter.Callback;
import com.amap.poisearch.searchmodule.PoiSearchWidget.IParentWidget;

/**
 * Created by liangchao_suxun on 2017/4/26.
 */

class SearchModuleWidget extends RelativeLayout implements IWidget {

    private PoiSearchWidget mPoiSearchWidget;

    private PoiListWidget mPoiListWidget;

    private IDelegate mDelegate;

    public SearchModuleWidget(Context context) {
        super(context);
        init();
    }

    public SearchModuleWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchModuleWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_search_module, this);

        mPoiSearchWidget = (PoiSearchWidget)findViewById(R.id.poi_search_widget);
        mPoiListWidget = (PoiListWidget)findViewById(R.id.poi_list_widget);

        mPoiSearchWidget.setParentWidget(mPoiSearchParentWidgetDelegate);
        mPoiListWidget.setParentWidget(mPoiListParentWidgetDelegate);
    }

    @Override
    public void bindDelegate(IDelegate delegate) {
        this.mDelegate = delegate;
    }

    @Override
    public void setCityName(String cityName) {
        if (mPoiSearchWidget != null) {
            mPoiSearchWidget.setCityName(cityName);
        }
    }

    @Override
    public void setFavAddressVisible(boolean isVisible) {
        mPoiListWidget.setFavAddressVisible(isVisible);
    }

    @Override
    public void setFavHomeAddress(String address) {
        if (TextUtils.isEmpty(address)) {
            return;
        }

        mPoiListWidget.setHomeAddr(address);
    }

    @Override
    public void setFavCompAddress(String address) {
        if (TextUtils.isEmpty(address)) {
            return;
        }

        mPoiListWidget.setCompAddr(address);
    }

    @Override
    public void reloadPoiList(ArrayList<PoiListItemData> poiItems) {
        mPoiListWidget.reloadPoiList(poiItems);
    }

    private PoiSearchWidget.IParentWidget mPoiSearchParentWidgetDelegate = new IParentWidget() {
        @Override
        public void onInput(String inputStr) {
            mPoiListWidget.onLoading();
            mDelegate.onSearch(inputStr);
        }

        @Override
        public void onChangeCityName() {
            mDelegate.onChangeCityName();
        }

        @Override
        public void onCancel() {
            mDelegate.onCancel();
        }
    };

    private PoiListWidget.IParentWidget mPoiListParentWidgetDelegate = new PoiListWidget.IParentWidget() {

        @Override
        public void onFavAddressClick(int homeOrComp, int changeOrSel) {

            if (homeOrComp == Callback.HOME_MODE) {
                switch (changeOrSel) {
                    case Callback.CHANGE_MODE:
                        mDelegate.onSetFavHomePoi();
                        break;
                    case Callback.SEL_MODE:
                        mDelegate.onChooseFavHomePoi();
                        break;
                }
            } else {
                switch (changeOrSel) {
                    case Callback.CHANGE_MODE:
                        mDelegate.onSetFavCompPoi();
                        break;
                    case Callback.SEL_MODE:
                        mDelegate.onChooseFavCompPoi();
                        break;
                }
            }
        }

        @Override
        public void onSelPoiItem(PoiItem poiItem) {
            mDelegate.onSelPoiItem(poiItem);
        }
    };
}
