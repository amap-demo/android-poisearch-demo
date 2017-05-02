 package com.amap.poisearch.searchmodule;

import java.util.ArrayList;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.amap.api.services.core.PoiItem;
import com.amap.poisearch.R;
import com.amap.poisearch.searchmodule.PoiListAdapter.Callback;

 /**
 * Created by liangchao_suxun on 2017/4/26.
 */

public class PoiListWidget extends FrameLayout {

    private PoiListView mPoiListView;
    private PoiListAdapter mPoiListAdapter;

    private IParentWidget mParentWidget = null;

    public PoiListWidget(Context context) {
        super(context);
        init();
    }

    public PoiListWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PoiListWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ArrayList<PoiListItemData> pois = new ArrayList<>();

    public void setParentWidget(IParentWidget widget) {
        this.mParentWidget = widget;
    }

    public void setFavAddressVisible(boolean isVisible) {
        mPoiListAdapter.setFavAddressVisible(isVisible);
        mPoiListAdapter.notifyDataSetChanged();
    }
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.widget_poi_list, this);
        mPoiListView = (PoiListView)findViewById(R.id.poi_lv);

        mPoiListAdapter = new PoiListAdapter(getContext(), pois);
        mPoiListView.setAdapter(mPoiListAdapter);

        mPoiListAdapter.setCallback(mPoiListAdapterCallback);
    }

    public void setHomeAddr(String homeAddr) {
        this.mPoiListAdapter.setHomeAddr(homeAddr);
        this.mPoiListAdapter.notifyDataSetChanged();
    }

    public void setCompAddr(String compAddr) {
        this.mPoiListAdapter.setCompAddr(compAddr);
        this.mPoiListAdapter.notifyDataSetChanged();
    }

    public void reloadPoiList(ArrayList<PoiListItemData> poiItems) {
        pois.clear();
        pois.addAll(poiItems);
        mPoiListAdapter.onLoadFinished();
        mPoiListAdapter.notifyDataSetChanged();
    }

    public void onLoading(){
        mPoiListAdapter.onLoading();
        mPoiListAdapter.notifyDataSetChanged();
    }

    private PoiListAdapter.Callback mPoiListAdapterCallback = new Callback() {
        @Override
        public void onFavAddressClick(int homeOrComp, int changeOrSel) {
            mParentWidget.onFavAddressClick(homeOrComp, changeOrSel);
        }

        @Override
        public void onSelPoiItem(PoiItem poiItem) {
            mParentWidget.onSelPoiItem(poiItem);
        }
    };


    public static interface IParentWidget{
        public void onFavAddressClick(int homeOrComp, int changeOrSel) ;
        public void onSelPoiItem(PoiItem poiItem);
    }

}
