package com.amap.poisearch.searchmodule;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.road.Crossroad;

/**
 * Created by liangchao_suxun on 2017/4/27.
 * AMapSearch使用的帮助类
 */

public class AMapSearchUtil {

    public static interface OnLatestPoiSearchListener {
        void onPoiSearched(PoiResult var1, int var2, long searchId);

        void onPoiItemSearched(PoiItem var1, int var2, long searchId);
    }

    public static interface OnSugListener {
        void onSug(List<PoiItem> tips, int i, long searchId);
    }

    /**
     * 进行poi检索
     *
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
    public static void doSug(final Context context, final long searchId, final String keyword, final String cityCode,
                             LatLng centerLL,
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

                    //如果经纬度不存在，则忽略
                    if (tip.getPoint().getLatitude() <= 0 || tip.getPoint().getLongitude() <= 0) {
                        continue;
                    }

                    PoiItem poiItem = new PoiItem(tip.getPoiID(), tip.getPoint(), tip.getName(), tip.getAddress());
                    poiItem.setAdCode(tip.getAdcode());
                    poiItem.setTypeCode(tip.getTypeCode());
                    pois.add(poiItem);
                }

                if (pois.size() > 0) {
                    inputtipsListener.onSug(pois, i, searchId);
                } else {
                    //如果tip返回的结果为空，则进行poi检索
                    doPoiSearch(context, searchId, keyword, "", cityCode, 0, 10, new OnLatestPoiSearchListener() {
                        @Override
                        public void onPoiSearched(PoiResult var1, int var2, long searchId) {
                            if (var1 == null || var1.getPois() == null) {
                                inputtipsListener.onSug(new ArrayList<PoiItem>(), -1, searchId);
                                return;
                            }
                            ArrayList<PoiItem> items = var1.getPois();
                            inputtipsListener.onSug(items, var2, searchId);
                        }

                        @Override
                        public void onPoiItemSearched(PoiItem var1, int var2, long searchId) {

                        }
                    });
                }

            }
        });
        inputTips.requestInputtipsAsyn();
    }

    /**
     * 进行反geo检索
     * 取点逻辑是：
     * 1、首先，默认的poi列表是按权重排序的，如果按距离排序可能会把一些很不重要的poi点排到前面来，经测试，还是使用默认的顺序效果好一些。
     * 2、取点逻辑是：先去poi列表的第一个，如果第一个poi的distance小于15米，则设置name为poi.name，经纬度为poi.location，如果大于15米，则设置name为“poi
     * .name+附近”，经纬度为当前逆地理请求的经纬度，以防止选点跳跃;
     * 3、对逆地理返回的交叉路口数组进行排序，取第一个距离最近的路口，如果该路况的distance小于15米且小于第一个poi的distance，则设置显示name为“xx和xx交叉路口”,设置经纬度为该路口location。
     *
     * @param context
     * @param lat
     * @param lng
     * @param searchId
     * @param onPoiSearchListener
     */
    public static void doGeoSearch(Context context, final double lat, final double lng, final long searchId,
                                   final OnLatestPoiSearchListener onPoiSearchListener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);

        geocoderSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                try {

                    if (regeocodeResult == null || regeocodeResult.getRegeocodeAddress() == null) {
                        onPoiSearchListener.onPoiItemSearched(null, 0, searchId);
                        return;
                    }

                    RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();

                    //poi目标点应该小于5000米。如果均大于5000米，则选择第一个poi点
                    int minDis = 5000;
                    com.amap.api.services.core.PoiItem poiItem = regeocodeResult.getRegeocodeAddress().getPois().get(0);

                    for (int ind = 0; regeocodeAddress.getPois() != null && ind < regeocodeAddress.getPois().size();
                         ind++) {
                        PoiItem item = regeocodeResult.getRegeocodeAddress().getPois().get(ind);
                        if (item.getDistance() > minDis) {
                            continue;
                        }
                        poiItem = item;
                        break;
                    }

                    float poiDis = poiItem == null ? 10 * 1000 : poiItem.getDistance();

                    //如果最近的crossroad的距离小于poi的距离，则选择最近的crossroad
                    float minCrossRoadDis = 10000;
                    Crossroad nearCrossRoad = null;
                    for (int ind = 0;
                         regeocodeAddress.getCrossroads() != null && ind < regeocodeAddress.getCrossroads().size();
                         ind++) {

                        Crossroad crossroad = regeocodeAddress.getCrossroads().get(ind);

                        //将最近的crossroad记为nearCrossRoad ， 同时nearCrossRoad需要小于上述poi的距离，否则无效
                        if (crossroad.getDistance() < minCrossRoadDis && crossroad.getDistance() < poiDis) {
                            nearCrossRoad = crossroad;
                            minCrossRoadDis = nearCrossRoad.getDistance();
                        }
                    }

                    //如果大于15米，则表示为附近
                    float nearbyDis = 15.f;
                    if (nearCrossRoad != null) {
                        String poiItemName = nearCrossRoad.getFirstRoadName() + "和" + nearCrossRoad.getSecondRoadName()
                            + "交叉口";

                        PoiItem crossRoadPoi = new PoiItem(nearCrossRoad.getId(), nearCrossRoad.getCenterPoint(),
                            poiItemName, poiItemName);
                        onPoiSearchListener.onPoiItemSearched(crossRoadPoi, 0, searchId);
                        return;
                    }

                    if (poiItem == null) {
                        onPoiSearchListener.onPoiItemSearched(null, 0, searchId);
                        return;
                    }

                    if (poiItem.getDistance() > nearbyDis) {
                        PoiItem res = new PoiItem(poiItem.getPoiId(), new LatLonPoint(lat, lng),
                            poiItem.getTitle() + "附近", poiItem.getSnippet() + "附近");
                        onPoiSearchListener.onPoiItemSearched(res, 0, searchId);
                        return;
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
