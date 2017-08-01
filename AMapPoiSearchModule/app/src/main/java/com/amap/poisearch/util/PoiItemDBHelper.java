package com.amap.poisearch.util;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.amap.api.services.core.PoiItem;
import com.google.gson.Gson;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public class PoiItemDBHelper {

    private SQLiteDatabase db;
    private Gson gson;

    private static String POI_TABLE_NAME = "poi_his_list";

    static class Holder{
        public static PoiItemDBHelper instance = new PoiItemDBHelper();
    }

    public static PoiItemDBHelper getInstance(){
        return Holder.instance;
    }

    private Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    private SQLiteDatabase getWritableDB(Context context) {
        if (db == null) {
            db = DBHelper.createDefHelper(context).getWritableDatabase();
        }
        return db;
    }

    public static String getCreateSql() {
        return "create table poi_his_list(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "poiitem varchar(5000) NOT NULL , ctime double NOT NULL)";
    }

    public ArrayList<PoiItem> getLatestPois(Context context, int pageSize) {
        if (context == null) {
            return null;
        }

        String sqlStr = "select distinct(poiitem) , ctime from " + POI_TABLE_NAME + " order by ctime desc limit " + pageSize;

        Cursor cursor = getWritableDB(context).rawQuery(sqlStr, null);
        ArrayList<PoiItem> res = new ArrayList<>();
        while (cursor.moveToNext()) {
            String poiStr = cursor.getString(cursor.getColumnIndex("poiitem"));

            PoiItem poiItem;
            try {
                poiItem = getGson().fromJson(poiStr, PoiItem.class);
            } catch (Exception e) {
                continue;
            }

            if (poiItem != null) {
                res.add(poiItem);
            }
        }

        return res;
    }

    public boolean isExist(Context context, PoiItem poiItem) {
        String poiStr = getGson().toJson(poiItem);

        if (TextUtils.isEmpty(poiStr)) {
            return false;
        }

        String sqlStr = "select distinct(poiitem) , ctime from " + POI_TABLE_NAME + " where poiitem = '" + poiStr + "'";
        Cursor cursor = getWritableDB(context).rawQuery(sqlStr, null);

        if (cursor.moveToNext()) {
            return poiStr.equals(cursor.getString(cursor.getColumnIndex("poiitem")));
        }

        return false;
    }

    public void savePoiItem(Context context, PoiItem poiItem) {

        if (isExist(context, poiItem)) {
            updatePoiItem(context, poiItem);
            return;
        }

        if (poiItem == null) {
            return;
        }

        String poiStr = getGson().toJson(poiItem);
        if (TextUtils.isEmpty(poiStr)) {
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put("poiitem", poiStr);
        cv.put("ctime", System.currentTimeMillis());
        getWritableDB(context).insert(POI_TABLE_NAME, null, cv);
    }

    public void updatePoiItem(Context context, PoiItem poiItem) {
        if (poiItem == null) {
            return;
        }

        String poiStr = getGson().toJson(poiItem);
        ContentValues cv = new ContentValues();
        cv.put("poiitem", poiStr);
        cv.put("ctime", System.currentTimeMillis());
        getWritableDB(context).update(POI_TABLE_NAME, cv, " poiitem= ? ", new String[] {poiStr});
    }
}
