package com.amap.poisearch.searchmodule;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.amap.poisearch.R;
import com.amap.poisearch.util.CityModel;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public class CityListWidget extends ListView {

    public CityListWidget(Context context) {
        super(context);
        init();
    }

    public CityListWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CityListWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private CityListAdapter mCityListAdapter;
    private IParentWidget mIParentWidget;

    public void setParentWidget(IParentWidget mIParentWidget) {
        this.mIParentWidget = mIParentWidget;
    }

    private void init() {
        setBackgroundColor(getContext().getResources().getColor(R.color.common_bg));
        mCityListAdapter = new CityListAdapter(getContext());
        setAdapter(mCityListAdapter);

        setDivider(new ColorDrawable(getResources().getColor(R.color.divider_color)));
        setDividerHeight(1);

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0 || position >= mCityListAdapter.getData().size()) {
                    return;
                }

                if (mCityListAdapter.getData().get(position).isGroupModel()) {
                    return;
                }

                if (mIParentWidget == null) {
                    return;
                }

                mIParentWidget.onSelCity(mCityListAdapter.getData().get(position));
            }
        });
    }

    public void loadCityList(ArrayList<CityModel> data) {
        mCityListAdapter.setData(data);
        mCityListAdapter.notifyDataSetChanged();

    }

    public static class CityListAdapter extends BaseAdapter {

        ArrayList<CityModel> data;
        private Context context;

        public CityListAdapter(Context context) {
            this.context = context;
        }

        public void setData(ArrayList<CityModel> data) {
            this.data = data;
        }

        ArrayList<CityModel> getData() {
            return data;
        }

        @Override
        public int getCount() {
            if (data == null) {
                return 0;
            }
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            CityModel cityData = data.get(position);
            return cityData.isGroupModel() ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CityModel cityData = data.get(position);

            if (convertView == null) {
                if (cityData.isGroupModel()) {
                    convertView = new CityListItemView.GroupView(context);
                } else {
                    convertView = new CityListItemView.ChildView(context);
                }
            }

            if (cityData.isGroupModel()) {
                ((CityListItemView.GroupView)convertView).setGroup(cityData.getCity().toUpperCase());
                return convertView;
            }

            ((CityListItemView.ChildView)convertView).setCity(cityData.getCity());
            return convertView;
        }

    }

    public static interface IParentWidget{
        public void onSelCity(CityModel cityModel);
    }

}
