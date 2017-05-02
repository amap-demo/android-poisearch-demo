 package com.amap.poisearch.searchmodule;

import com.amap.api.services.core.PoiItem;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public class PoiListItemData {
    public static final int HIS_DATA = 0;
    public static final int SEARCH_DATA = 1;

    int type;
    com.amap.api.services.core.PoiItem poiItem;

    public static int getHisData() {
        return HIS_DATA;
    }

    public static int getSearchData() {
        return SEARCH_DATA;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PoiItem getPoiItem() {
        return poiItem;
    }

    public void setPoiItem(PoiItem poiItem) {
        this.poiItem = poiItem;
    }

    public PoiListItemData(int type, com.amap.api.services.core.PoiItem poiItem) {
        this.type = type;
        this.poiItem = poiItem;
    }
}
