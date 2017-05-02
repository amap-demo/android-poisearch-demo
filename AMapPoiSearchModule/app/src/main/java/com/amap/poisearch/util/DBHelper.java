package com.amap.poisearch.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mDataBase = null;

    public static DBHelper createDefHelper(Context context) {
        String name = context.getPackageName();
        int version = 1;
        return new DBHelper(context, name, null, version);
    }

    public DBHelper(Context context, String name, CursorFactory factory,
                    int version) {
        super(context, name, factory, version);

        mDataBase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PoiItemDBHelper.getCreateSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ;
    }
}
