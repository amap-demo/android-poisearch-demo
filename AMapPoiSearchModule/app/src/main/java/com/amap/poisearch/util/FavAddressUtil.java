package com.amap.poisearch.util;

import android.content.Context;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.PoiItem;
import com.google.gson.Gson;

/**
 * Created by liangchao_suxun on 2017/4/27.
 */

public class FavAddressUtil {

    private static final String FAV_HOME_ADDRESS_KEY = "fav_home_address_key";
    private static final String FAV_COMP_ADDRESS_KEY = "fav_comp_address_key";

    private static Gson mGsonHelper = null;
    private static Gson getGsonHelper(){
        if (mGsonHelper == null) {
            mGsonHelper = new Gson();
        }

        return mGsonHelper;
    }

    public static PoiItem getFavHomePoi(Context context){
        String poiStr = PreferenceUtil.getStr(context,FAV_HOME_ADDRESS_KEY);
        PoiItem poiItem = getGsonHelper().fromJson(poiStr, PoiItem.class);
        return poiItem;
    }

    public static PoiItem getFavCompPoi(Context context){
        String poiStr = PreferenceUtil.getStr(context,FAV_COMP_ADDRESS_KEY);
        PoiItem poiItem = getGsonHelper().fromJson(poiStr, PoiItem.class);
        return poiItem;
    }

    public static void saveFavHomePoi(Context context, PoiItem poiItem) {
        if (poiItem == null) {
            return;
        }

        String jsonStr = getGsonHelper().toJson(poiItem);
        PreferenceUtil.save(context, FAV_HOME_ADDRESS_KEY, jsonStr);
    }

    public static void saveFavCompPoi(Context context, PoiItem poiItem){
        if (poiItem == null) {
            return;
        }

        String jsonStr = getGsonHelper().toJson(poiItem);
        PreferenceUtil.save(context, FAV_COMP_ADDRESS_KEY, jsonStr);
    }

}
