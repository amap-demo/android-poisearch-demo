package com.amap.poisearch.searchmodule;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;

/**
 * Created by liangchao_suxun on 2017/4/27.
 * AMapSearch使用的帮助类
 */

public class AMapSearchUtil {

    public static interface OnLatestPoiSearchListener {
        void onPoiSearched(PoiResult var1, int var2 , long searchId);
        void onPoiItemSearched(PoiItem var1, int var2, long searchId);
    }

    public static interface OnSugListener{
        void onSug(List<PoiItem> tips, int i, long searchId);
    }

    /**
     * 进行poi检索
     * @param context
     * @param searchId
     * @param keyWord
     * @param category
     * @param adCode
     * @param page
     * @param pageSize
     * @param onPoiSearchListener
     */
    public static void doPoiSearch(Context context, final long searchId, String keyWord, String category, String adCode,
                                   int page,
                                   int pageSize,
                                   final OnLatestPoiSearchListener onPoiSearchListener) {
        if (onPoiSearchListener == null) {
            return;
        }

        PoiSearch.Query query = new PoiSearch.Query(keyWord, category, adCode);
        query.setPageSize(pageSize);
        query.setPageNum(page);
        PoiSearch poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(new OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                onPoiSearchListener.onPoiSearched(poiResult, i, searchId);
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                onPoiSearchListener.onPoiItemSearched(poiItem, i, searchId);
            }
        });
        poiSearch.searchPOIAsyn();
    }

    /**
     * 进行sug检索
     *
     * @param context
     * @param searchId
     * @param keyword
     * @param cityCode
     * @param inputtipsListener
     */
    public static void doSug(Context context, final long searchId, String keyword, String cityCode, LatLng centerLL,
                             final OnSugListener inputtipsListener) {

        InputtipsQuery inputquery = new InputtipsQuery(keyword, cityCode);
        inputquery.setCityLimit(true);

        if (centerLL != null) {
            inputquery.setLocation(new LatLonPoint(centerLL.latitude, centerLL.longitude));
        }

        Inputtips inputTips = new Inputtips(context, inputquery);
        inputTips.setInputtipsListener(new InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> list, int i) {
                List<PoiItem> pois = new ArrayList<PoiItem>();

                for (int ind = 0; list != null && ind < list.size(); ind++) {
                    Tip tip = list.get(ind);
                    if (tip == null) {
                        continue;
                    }

                    PoiItem poiItem = new PoiItem(tip.getPoiID(), tip.getPoint(), tip.getName(), tip.getAddress());
                    poiItem.setAdCode(tip.getAdcode());
                    poiItem.setTypeCode(tip.getTypeCode());
                    pois.add(poiItem);
                }
                inputtipsListener.onSug(pois, i, searchId);
            }
        });
        inputTips.requestInputtipsAsyn();
    }

    /**
     * 进行反geo检索
     * @param context
     * @param lat
     * @param lng
     * @param searchId
     * @param onPoiSearchListener
     */
    public static void doGeoSearch(Context context, double lat, double lng, final long searchId,
                                   final OnLatestPoiSearchListener onPoiSearchListener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);

        geocoderSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                try {
                    int minDis = 5000;
                    com.amap.api.services.core.PoiItem poiItem = regeocodeResult.getRegeocodeAddress().getPois().get(0);

                    for(int ind=0; ind<regeocodeResult.getRegeocodeAddress().getPois().size();ind++) {
                        PoiItem item = regeocodeResult.getRegeocodeAddress().getPois().get(ind);
                        if (item.getDistance() < minDis) {
                            minDis = item.getDistance();
                            poiItem = item;
                        }
                    }
                    onPoiSearchListener.onPoiItemSearched(poiItem, 0, searchId);
                } catch (Exception e) {
                    onPoiSearchListener.onPoiItemSearched(null, 0, searchId);
                }

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(lat, lng), 2000,
            GeocodeSearch.AMAP);

        geocoderSearch.getFromLocationAsyn(query);

    }

}
