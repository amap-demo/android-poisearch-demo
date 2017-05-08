package com.amap.poisearch.searchmodule;

import java.util.ArrayList;

import android.content.Context;
import com.amap.api.services.core.PoiItem;
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

}
