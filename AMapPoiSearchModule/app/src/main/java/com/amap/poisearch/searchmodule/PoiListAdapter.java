 package com.amap.poisearch.searchmodule;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.api.services.core.PoiItem;
import com.amap.poisearch.R;

/**
 * Created by liangchao_suxun on 2017/4/26.
 */

public class PoiListAdapter extends BaseAdapter {

    private Context context;

    private String homeAddr;

    private String compAddr;

    ArrayList<PoiListItemData> data;

    private int loadStatus;

    private boolean isFavViewVisible = true;

    private static final int LOAD_STATUS_LOADING = 0;
    private static final int LOAD_STATUS_FINISHED = 1;

    private Callback mCallback;

    public static interface Callback{
        public static final int HOME_MODE = 0;
        public static final int COMP_MODE = 1;

        public static final int CHANGE_MODE = 0;
        public static final int SEL_MODE =  1;

        public void onFavAddressClick(int homeOrComp, int changeOrSel);

        public void onSelPoiItem(PoiItem poiItem);
    }

    public PoiListAdapter(Context context , ArrayList<PoiListItemData> data) {
        this.context = context;
        this.data = data;
    }

    public void setHomeAddr(String homeAddr) {
        this.homeAddr = homeAddr;
    }

    public void setCompAddr(String compAddr) {
        this.compAddr = compAddr;
    }

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public void onLoading(){
        loadStatus = LOAD_STATUS_LOADING;
    }

    public void onLoadFinished(){
        loadStatus = LOAD_STATUS_FINISHED;
    }

    public void setFavAddressVisible(boolean isVisible) {
        isFavViewVisible = isVisible;
    }

    @Override
    public int getCount() {
        if (loadStatus == LOAD_STATUS_LOADING) {
            return 1;
        } else {
            if (data == null) {
                return 0;
            }

            return data.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (loadStatus == LOAD_STATUS_LOADING) {
            return 0;
        }

        if (position == 0) {
            return 1;
        }

        if (position > 0) {
            return 2;
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (loadStatus == LOAD_STATUS_FINISHED) {
            if (getItemViewType(position) == 1) {

                if (!isFavViewVisible) {
                    return new View(context);
                }

                if (convertView == null) {
                    convertView = new FavAddressWidget(context);
                }

                ((FavAddressWidget)convertView).setHomeAddr(homeAddr);
                ((FavAddressWidget)convertView).setCompAddr(compAddr);

                ((FavAddressWidget)convertView).setCallback(mCallback);

                return convertView;
            }

            if (getItemViewType(position) == 2) {
                if (convertView == null) {
                    convertView = new PoiListItemWidget(context);
                }

                if (convertView.getTag() == null) {
                    PoiItemWidgetTag tag = new PoiItemWidgetTag();

                    tag.iconIV = (ImageView)convertView.findViewById(R.id.icon_iv);
                    tag.titleTV = (TextView)convertView.findViewById(R.id.title_tv);
                    tag.subTitleTV = (TextView)convertView.findViewById(R.id.sub_title_tv);

                    convertView.setTag(tag);
                    convertView.setOnClickListener(mItemClickListener);
                }

                PoiItemWidgetTag tag = (PoiItemWidgetTag)convertView.getTag();

                PoiListItemData poiItem = data.get(position - 1);
                tag.from(poiItem);

                return convertView;
            }

            return new View(context);

        } else if (loadStatus == LOAD_STATUS_LOADING) {
            convertView = getLoadingView(context);
            return convertView;
        }

        return new View(context);
    }

    private RelativeLayout mLoadingView = null;
    private View getLoadingView(Context context) {
        if (mLoadingView == null) {
            mLoadingView = new RelativeLayout(context);
            LayoutInflater.from(context).inflate(R.layout.widget_poi_list_loading_item, mLoadingView);
        }

        return mLoadingView;
    }

    private static class PoiItemWidgetTag{
        public ImageView iconIV;
        public TextView titleTV;
        public TextView subTitleTV;

        public PoiListItemData mPoiItem;

        public void from(PoiListItemData poiItem) {
            mPoiItem = poiItem;

            titleTV.setText(mPoiItem.poiItem.getTitle());
            subTitleTV.setText(mPoiItem.poiItem.getSnippet());

            if (mPoiItem.type == PoiListItemData.HIS_DATA) {
                iconIV.setImageResource(R.mipmap.time);
            } else {
                iconIV.setImageResource(R.mipmap.poi);
            }
        }
    }

    private View.OnClickListener mItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!(v instanceof PoiListItemWidget)) {
                return;
            }

            if (mCallback == null) {
                return;
            }

            if (v.getTag() == null || (!(v.getTag() instanceof PoiItemWidgetTag))) {
                return;
            }

            PoiItemWidgetTag tag = (PoiItemWidgetTag)v.getTag();

            mCallback.onSelPoiItem(tag.mPoiItem.poiItem);
        }
    };
}
