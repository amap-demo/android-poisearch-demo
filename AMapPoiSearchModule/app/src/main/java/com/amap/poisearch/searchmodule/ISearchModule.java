package com.amap.poisearch.searchmodule;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import com.amap.api.services.core.PoiItem;
import com.amap.poisearch.util.CityModel;

/**
 * Created by liangchao_suxun on 2017/4/26.
 */

public interface ISearchModule {


    public static interface IWidget {

        /**
         * 绑定控制类
         * @param delegate
         */
        public void bindDelegate(IDelegate delegate);

        /** 设置所在城市的名字,默认为北京*/
        public void setCityName(String cityName);

        /**
         * 设置收藏地址选择的view是否可见
         * @param isVisible
         */
        public void setFavAddressVisible(boolean isVisible);

        /**
         * 设置收藏的家的地址
         * @param address
         */
        public void setFavHomeAddress(String address);

        /**
         * 设置收藏的公司的地址
         * @param address
         */
        public void setFavCompAddress(String address);

        /**
         * 重新加载poi检索结果
         * @param poiItems
         */
        public void reloadPoiList(ArrayList<PoiListItemData> poiItems);

    }

    public static interface IDelegate {

        /**
         * 绑定父控制类。不能处理的逻辑交由父控制类处理
         * @param delegate
         */
        public void bindParentDelegate(IParentDelegate delegate);

        /** 获得显示的view*/
        public View getWidget(Context context);

        /** 设置所在城市*/
        public void setCity(CityModel city);

        /**
         * 设置收藏地址选择的view是否可见
         * @param isVisible
         */
        public void setFavAddressVisible(boolean isVisible);

        /**
         * 设置收藏的家的PoiItem
         * @param address
         */
        public void setFavHomePoi(PoiItem address);

        /**
         * 设置收藏的公司PoiItem
         * @param address
         */
        public void setFavCompPoi(PoiItem address);

        /** 对inputStr进行搜索 */
        public void onSearch(String inputStr);

        /** 更换城市 */
        public void onChangeCityName();

        /** 取消 */
        public void onCancel();

        /**
         * 用户触发 设置家的地址 的回调
         */
        public void onSetFavHomePoi();

        /**
         * 用户触发 设置公司地址 的回调
         */
        public void onSetFavCompPoi();

        /**
         * 用户触发 选择了家的地址 的回调
         */
        public void onChooseFavHomePoi();

        /**
         * 用户触发 选择了公司地址 的回调
         */
        public void onChooseFavCompPoi();

        /**
         * 用户选择了某个PoiItem
         * @param poiItem
         */
        public void onSelPoiItem(PoiItem poiItem);


        /**
         * 父Delegate，IDelegate不能处理的交给IParentDelegate
         */
        public static interface IParentDelegate {
            /** 更换城市 */
            public void onChangeCityName();

            /** 取消 */
            public void onCancel();

            /**
             * 用户触发 设置家的地址 的回调
             */
            public void onSetFavHomePoi();

            /**
             * 用户触发 设置公司地址 的回调
             */
            public void onSetFavCompPoi();

            /**
             * 用户触发 选择了家的地址 的回调
             */
            public void onChooseFavHomePoi(PoiItem poiItemData);

            /**
             * 用户触发 选择了公司地址 的回调
             */
            public void onChooseFavCompPoi(PoiItem poiItem);

            /**
             * 用户选择了某个PoiItem
             * @param poiItem
             */
            public void onSelPoiItem(PoiItem poiItem);
        }
    }


}
