package com.amap.poisearch.searchmodule;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amap.poisearch.R;
import com.amap.poisearch.util.CityModel;

/**
 * Created by liangchao_suxun on 2017/5/2.
 */

public class CityInputWidget extends RelativeLayout {
    public CityInputWidget(Context context) {
        super(context);
        init();

    }

    public CityInputWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CityInputWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private EditText mInputET;
    private TextView mCancelTV;
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.widget_city_input, this);
        setBackgroundResource(R.color.common_bg);

        int padding = (int)getContext().getResources().getDimension(R.dimen.padding);
        setPadding(padding, 0, padding, 0);

        mInputET = (EditText)findViewById(R.id.city_input_et);
        mCancelTV = (TextView)findViewById(R.id.cancel_tv);

        mInputET.addTextChangedListener(mTextWatcher);
        mCancelTV.setOnClickListener(mOnClickListener);

    }


    private IParentWidget mIParentWidget;

    public void setParentWidget(IParentWidget mIParentWidget) {
        this.mIParentWidget = mIParentWidget;
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mIParentWidget != null) {
                mIParentWidget.onCityInput(s != null ? s.toString() : "");
            }
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cancel_tv) {
                if (mIParentWidget != null) {
                    mIParentWidget.onCancel();
                }
            }
        }
    };

    public static interface IParentWidget{
        public void onCityInput(String cityInput);
        public void onCancel();
    }

}
