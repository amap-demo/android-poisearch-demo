package com.amap.poisearch.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by liangchao_suxun on 2017/4/27.
 */

public class CityUtil {

    private static final String DEF_CITY_KEY ="def_city_key";

    /**
     * 返回默认的城市.如果SharedPreference返回为空，则取北京市
     */
    public static CityModel getDefCityModel(Context context) {
        if (context == null) {
            return null;
        }

        String defCityStr = PreferenceUtil.getStr(context, DEF_CITY_KEY);
        CityModel res= null;
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

    private static final String AMAP_CITY_FILENAME = "amap_cities.json";
    public static ArrayList<CityModel> getCityList(Context context) {
        String cityListStr = getFromAssets(context, AMAP_CITY_FILENAME);
        ArrayList<CityModel> cities;
        cities = new Gson().fromJson(cityListStr, new TypeToken<ArrayList<CityModel>>() {}.getType());
        return cities;
    }

    public static ArrayList<CityModel> getGroupCityList(Context context) {
        ArrayList<CityModel> res = new ArrayList<CityModel>();

        ArrayList<CityModel> oriCityList = getCityList(context);
        Collections.sort(oriCityList, new Comparator<CityModel>() {
            @Override
            public int compare(CityModel o1, CityModel o2) {
                return o1.getPinyin().compareTo(o2.getPinyin());
            }
        });

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

    private static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) { Result += line; }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
