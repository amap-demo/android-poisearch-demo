package com.amap.poisearch.searchmodule;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.amap.poisearch.R;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public class CurrCityWidget extends LinearLayout {
    public CurrCityWidget(Context context) {
        super(context);
        init();
    }

    public CurrCityWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurrCityWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextView mCurrCityTV;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_current_city, this);
        setBackgroundColor(getContext().getResources().getColor(R.color.common_bg));

        mCurrCityTV = (TextView)findViewById(R.id.curr_city_tv);
        mCurrCityTV.setText("");
    }

    private String stringFormat = new String("当前城市:%s");

    public void setCurrCity(String city) {
        if (city == null) {
            return;
        }
        mCurrCityTV.setText(String.format(stringFormat, city));
    }

}
