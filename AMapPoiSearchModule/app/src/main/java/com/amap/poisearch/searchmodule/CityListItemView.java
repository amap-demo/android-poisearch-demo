package com.amap.poisearch.searchmodule;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.poisearch.R;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public class CityListItemView {

    public static class GroupView extends RelativeLayout {
        public GroupView(Context context) {
            super(context);
            init();
        }

        public GroupView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public GroupView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private TextView mGroupTV;
        private void init() {
            int padding = (int)getContext().getResources().getDimension(R.dimen.padding);
            setPadding(padding, padding, padding, padding);

            LayoutInflater.from(getContext()).inflate(R.layout.widget_city_list_groupview, this);
            mGroupTV = (TextView)findViewById(R.id.group_tv);

            setBackgroundColor(getContext().getResources().getColor(R.color.divider_color));
        }

        public void setGroup(String group) {
            mGroupTV.setText(group);
        }
    }

    public static class ChildView extends RelativeLayout {

        public ChildView(Context context) {
            super(context);
            init();
        }

        public ChildView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public ChildView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private TextView mCityTV;
        private void init() {

            int padding = (int)getContext().getResources().getDimension(R.dimen.padding);
            setPadding(padding, padding, padding, padding);

            LayoutInflater.from(getContext()).inflate(R.layout.widget_city_list_child_view, this);

            mCityTV = (TextView)findViewById(R.id.city_name_tv);
        }

        public void setCity(String city) {
            if (TextUtils.isEmpty(city) || mCityTV == null) {
                return;
            }
            mCityTV.setText(city);
        }
    }
}
