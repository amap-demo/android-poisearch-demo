package com.amap.poisearch.searchmodule;

import java.text.DecimalFormat;

import android.location.Location;
import com.amap.api.services.core.PoiItem;
import com.amap.poisearch.util.AMapUtil;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public class PoiListItemData {
    public static final int HIS_DATA = 0;
    public static final int SEARCH_DATA = 1;

    int type;
    com.amap.api.services.core.PoiItem poiItem;

    private DecimalFormat disFormat = new DecimalFormat("0.0km");

    public String calDis(Location currLoc) {
        if (currLoc == null) {
            return "";
        }

        if (poiItem == null || poiItem.getLatLonPoint() == null) {
            return null;
        }

        Location location = new Location(currLoc);
        location.setLatitude(poiItem.getLatLonPoint().getLatitude());
        location.setLongitude(poiItem.getLatLonPoint().getLongitude());

        float distance = AMapUtil.calDistance(currLoc, location);

        distance = distance / 1000;

        if (distance < 0.1f) {
            return null;
        }

        return disFormat.format(distance);
    }

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
