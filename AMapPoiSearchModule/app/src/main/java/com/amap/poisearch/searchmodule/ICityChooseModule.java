 package com.amap.poisearch.searchmodule;

import java.util.ArrayList;

import com.amap.poisearch.util.CityModel;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public interface ICityChooseModule {

    public static interface IWidget {

        /**
         * 绑定控制类
         *
         * @param delegate
         */
        public void bindDelegate(IDelegate delegate);

        public void setCurrCity(String cityName);

        public void loadCityList(ArrayList<CityModel> data);
    }

    public static interface IDelegate {

        public void bindParentDelegate(IParentDelegate delegate);

        public void setCurrCity(String cityName);

        public void onChooseCity(CityModel city);

        public void onCityInput(String cityInput);

        public void onCancel();

        public void loadCityList(ArrayList<CityModel> data);
    }

    public static interface IParentDelegate {
        public void onChooseCity(CityModel city);

        public void onCancel();
    }

}
