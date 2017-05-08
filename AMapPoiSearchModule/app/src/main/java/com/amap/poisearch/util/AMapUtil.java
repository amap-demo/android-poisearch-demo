package com.amap.poisearch.util;

import android.location.Location;

/**
 * Created by liangchao_suxun on 2017/5/8.
 */

public class AMapUtil {
    public static float calDistance(Location location1, Location location2) {
        float res[] = new float[1];
        Location.distanceBetween(location1.getLatitude(), location1.getLongitude(), location2.getLatitude(),
            location2.getLongitude(), res);

        return res[0];
    }
}
