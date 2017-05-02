package com.amap.poisearch.searchmodule;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import com.amap.poisearch.searchmodule.ICityChooseModule.IParentDelegate;
import com.amap.poisearch.searchmodule.ICityChooseModule.IWidget;
import com.amap.poisearch.util.CityModel;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public class CityChooseDelegate implements ICityChooseModule.IDelegate {

    IParentDelegate mIParentDelegate;
    @Override
    public void bindParentDelegate(IParentDelegate delegate) {
        this.mIParentDelegate = delegate;
    }

    @Override
    public void setCurrCity(String cityName) {
        mWidget.setCurrCity(cityName);
    }

    @Override
    public void onChooseCity(CityModel city) {
        if (mIParentDelegate != null) {
            mIParentDelegate.onChooseCity(city);
        }
    }

    @Override
    public void loadCityList(ArrayList<CityModel> data){
        mWidget.loadCityList(data);
    }

    private Context mContext;
    private IWidget mWidget;

    public View getWidget(Context context) {
        if (context == null) {
            return null;
        }

        this.mContext = context;

        if (mWidget != null) {
            return (View)mWidget;
        }

        mWidget = new CityChooseWidget(context);
        mWidget.bindDelegate(this);

        return (View)mWidget;
    }


}
