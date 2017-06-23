package com.amap.poisearch.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;

/**
 * Created by liangchao_suxun on 2017/4/27.
 */

public class CityUtil {

    private static final String DEF_CITY_KEY = "def_city_key";

    /**
     * 返回默认的城市.如果SharedPreference返回为空，则取北京市
     */
    public static CityModel getDefCityModel(Context context) {
        if (context == null) {
            return null;
        }

        String defCityStr = PreferenceUtil.getStr(context, DEF_CITY_KEY);
        CityModel res = null;
        try {
            res = new Gson().fromJson(defCityStr, CityModel.class);
        } catch (Exception e) {
            ; // just in case
        }

        if (res != null) {
            return res;
        }

        ArrayList<CityModel> cities = getCityList(context);

        if (cities == null) {
            return null;
        }

        for (CityModel cityModel : cities) {
            if ("北京市".equals(cityModel.getCity())) {
                PreferenceUtil.save(context, DEF_CITY_KEY, new Gson().toJson(cityModel));
                return cityModel;
            }
        }

        return null;
    }

    public static ArrayList<CityModel> getCityList(Context context) {
        return getCityList(context, null);
    }

    public static CityModel getCityByCode(Context context, String adCode) {
        ArrayList<CityModel> cityList = getCityList(context);
        if (cityList == null) {
            return null;
        }

        for (CityModel cityItem : cityList) {
            if (cityItem.getCode().equals(adCode)) {
                return cityItem;
            }
        }

        return null;
    }

    public static ArrayList<CityModel> getCityList(Context context, String filterStr) {
        ArrayList<CityModel> cities;
        cities = getCitiesFromAssets(context);

        if (TextUtils.isEmpty(filterStr)) {
            return cities;
        }

        ArrayList<CityModel> res = new ArrayList<>();
        for (CityModel item : cities) {
            if (TextUtils.isEmpty(filterStr) || item.getPinyin().contains(filterStr) || item.getCity().contains(
                filterStr)) {
                res.add(item);
            }
        }

        return res;
    }

    private static volatile HashSet<String> mHotCities = new HashSet<>();

    public static void setHotCities(HashSet<String> hotCities) {
        if (hotCities == null || hotCities.size() == 0) {
            mHotCities.add("北京市");
            mHotCities.add("广州市");
            mHotCities.add("成都市");
            mHotCities.add("上海市");
            mHotCities.add("杭州市");
            mHotCities.add("武汉市");
            return;
        }

        mHotCities.clear();
        mHotCities.addAll(hotCities);
    }

    public static char HOT_CITY_CHAR = '#';

    public static ArrayList<CityModel> getGroupCityList(Context context) {
        return getGroupCityList(context, null);
    }

    public static ArrayList<CityModel> getGroupCityList(Context context, String filterStr) {
        ArrayList<CityModel> res = new ArrayList<CityModel>();

        ArrayList<CityModel> oriCityList = getCityList(context, filterStr);
        Collections.sort(oriCityList, new Comparator<CityModel>() {
            @Override
            public int compare(CityModel o1, CityModel o2) {
                return o1.getPinyin().compareTo(o2.getPinyin());
            }
        });

        // 先录入热门城市
        if (mHotCities != null && mHotCities.size() > 0) {
            for (CityModel item : oriCityList) {
                if (mHotCities.contains(item.getCity())) {
                    if (res.size() == 0) {
                        res.add(CityModel.createGroupModel(HOT_CITY_CHAR));
                    }
                    res.add(item);
                    continue;
                }
            }
        }

        char currChar = ' ';
        for (CityModel item : oriCityList) {
            try {
                char groupChar = item.getPinyin().charAt(0);

                if (groupChar != currChar) {
                    CityModel groupModel = CityModel.createGroupModel(groupChar);
                    res.add(groupModel);
                    currChar = groupChar;
                }

                res.add(item);
            } catch (Exception e) {
                continue;
            }
        }

        return res;
    }

    private static ArrayList<CityModel> cityListCache = new ArrayList<>();

    /**
     * 通过assets读取citylist
     * @param context
     * @return
     */
    private static ArrayList<CityModel> getCitiesFromAssets(Context context) {
        if (cityListCache != null && cityListCache.size() > 0) {
            return cityListCache;
        }

        ArrayList<CityModel> cityList = null;

        try {
            InputStream cityListInputStream = context.getResources().getAssets().open("city_list.data");
            InputStreamReader inputReader = new InputStreamReader(cityListInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputReader);
            String cityJson = new String();

            String res = "";
            while ((res = bufferedReader.readLine()) != null) {
                cityJson = cityJson + res;
            }
            Gson gson = new Gson();
            cityList = gson.fromJson(cityJson, new TypeToken<List<CityModel>>() {}.getType());

            cityListCache = cityList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cityList;
    }

    private static final int ADCODE_LENGTH = 6;
    private static final int ADCODE_MEAN_LENGTH = 2;
    public static boolean isSameCity(String adCode, String cityCode2) {
        if (adCode == null || cityCode2 == null) {
            return false;
        }

        if (adCode.length() != ADCODE_LENGTH && cityCode2.length() != ADCODE_LENGTH) {
            return false;
        }
        return adCode.substring(0, ADCODE_MEAN_LENGTH).equals(cityCode2.substring(0, ADCODE_MEAN_LENGTH));
    }
}
