package com.amap.poisearch.searchmodule;

import java.util.ArrayList;

import android.content.Context;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;

/**
 * Created by liangchao_suxun on 2017/4/27.
 * AMapSearch使用的帮助类
 */

public class AMapSearchUtil {

    public static void doPoiSearch(Context context, String searchId, String keyWord, String category, String adCode,
                                   int page,
                                   int pageSize,
                                   OnPoiSearchListener onPoiSearchListener) {

        PoiSearch.Query query = new PoiSearch.Query(keyWord, category, adCode);
        query.setPageSize(pageSize);
        query.setPageNum(page);
        PoiSearch poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(onPoiSearchListener);
        poiSearch.searchPOIAsyn();
    }

}
