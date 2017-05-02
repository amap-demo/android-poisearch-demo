package com.amap.poisearch.searchmodule;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.amap.poisearch.R;
import com.amap.poisearch.searchmodule.ICityChooseModule.IDelegate;
import com.amap.poisearch.util.CityModel;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public class CityChooseWidget extends RelativeLayout implements ICityChooseModule.IWidget , CityListWidget.IParentWidget{

    private CurrCityWidget mCurrCityWidget;
    private CityListWidget mCityListWidget;

    public CityChooseWidget(Context context) {
        super(context);
        init();
    }

    public CityChooseWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CityChooseWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        int padding = (int)getContext().getResources().getDimension(R.dimen.padding);
        setPadding(padding, padding, padding, 0);
        LayoutInflater.from(getContext()).inflate(R.layout.widget_city_choose, this);

        mCurrCityWidget = (CurrCityWidget)findViewById(R.id.curr_city_widget);
        mCityListWidget = (CityListWidget)findViewById(R.id.city_list);

        mCityListWidget.setParentWidget(this);
    }

    private IDelegate mDelegate;
    @Override
    public void bindDelegate(IDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void setCurrCity(String cityName) {
        mCurrCityWidget.setCurrCity(cityName);
    }

    @Override
    public void loadCityList(ArrayList<CityModel> data){
        mCityListWidget.loadCityList(data);
    }

    @Override
    public void onSelCity(CityModel cityModel) {
        if (mDelegate != null) {
            mDelegate.onChooseCity(cityModel);
        }
    }
}
