 package com.amap.poisearch.searchmodule;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.poisearch.R;

/**
 * Created by liangchao_suxun on 2017/4/26.
 */

public class PoiSearchWidget extends RelativeLayout {

    private View mCityNameContainer;
    private TextView mCityNameTV;
    private EditText mPoiInputTV;
    private TextView mCancelTV;
    private View mClearIV;

    /**
     * def value is 北京
     */
    private String mCityName = "北京";

    private IParentWidget mParentWidget = null;

    public PoiSearchWidget(Context context) {
        super(context);
        init();
    }

    public PoiSearchWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PoiSearchWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setParentWidget(IParentWidget widget) {
        this.mParentWidget = widget;
    }

    public void setCityName(String cityName) {
        this.mCityName = cityName;
        mCityNameTV.setText(this.mCityName);
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_poi_search, this);
        setBackgroundResource(R.color.common_bg);

        mCityNameContainer = findViewById(R.id.city_name_container);
        mCityNameTV = (TextView)findViewById(R.id.city_name_tv);
        mPoiInputTV = (EditText)findViewById(R.id.poi_input_et);
        mCancelTV = (TextView)findViewById(R.id.cancel_tv);
        mClearIV = findViewById(R.id.clear_iv_container);

        mCityNameContainer.setOnClickListener(mOnClickListener);
        mCancelTV.setOnClickListener(mOnClickListener);
        mClearIV.setOnClickListener(mOnClickListener);

        mCityNameTV.setText(mCityName);

        mPoiInputTV.addTextChangedListener(mInputWatcher);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.city_name_container) {
                if (mParentWidget != null) {
                    mParentWidget.onChangeCityName();
                }
            } else if (v.getId() == R.id.cancel_tv) {
                if (mParentWidget != null) {
                    mParentWidget.onCancel();
                }
            } else if (v.getId() == R.id.clear_iv_container) {
                mPoiInputTV.setText("");
            }

        }
    };

    private TextWatcher mInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == null) {
                return;
            }

            String temp = s.toString();

            if (temp.length() > 0) {
                mClearIV.setVisibility(View.VISIBLE);
            } else {
                mClearIV.setVisibility(View.GONE);
            }

            if (mParentWidget != null) {
                mParentWidget.onInput(temp);
            }

        }
    };

    public static interface IParentWidget {
        public void onInput(String inputStr);

        public void onChangeCityName();

        public void onCancel();
    }

}
