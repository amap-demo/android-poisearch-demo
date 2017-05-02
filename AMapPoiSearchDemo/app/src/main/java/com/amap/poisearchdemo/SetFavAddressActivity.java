package com.amap.poisearchdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import com.amap.api.services.core.PoiItem;
import com.amap.poisearch.searchmodule.ISearchModule.IDelegate.IParentDelegate;
import com.amap.poisearch.searchmodule.SearchModuleDelegate;
import com.amap.poisearch.util.CityModel;
import com.google.gson.Gson;

/**
 * Created by liangchao_suxun on 2017/4/28.
 */

public class SetFavAddressActivity extends AppCompatActivity {

    private SearchModuleDelegate mSearchModuelDeletage;

    private int mFavType = 0;

    public static final String CURR_CITY_KEY = "curr_city_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setfavaddress);

        RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);
        mSearchModuelDeletage = new SearchModuleDelegate();
        mSearchModuelDeletage.bindParentDelegate(mSearchModuleParentDelegate);
        contentView.addView(mSearchModuelDeletage.getWidget(this));

        mSearchModuelDeletage.setFavAddressVisible(false);

        mFavType = getIntent().getIntExtra(MainActivity.FAVTYPE_KEY, -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            String currCityStr = getIntent().getStringExtra(CURR_CITY_KEY);
            Gson gson = new Gson();
            CityModel cityModel = gson.fromJson(currCityStr, CityModel.class);
            mSearchModuelDeletage.setCity(cityModel);
        } catch (Exception e) {
            ;
        }


    }

    private SearchModuleDelegate.IParentDelegate mSearchModuleParentDelegate = new IParentDelegate() {
        @Override
        public void onChangeCityName() {
            Intent intent = new Intent();
            intent.setClass(SetFavAddressActivity.this, CityChooseActivity.class);
            intent.putExtra(CityChooseActivity.CURR_CITY_KEY, mSearchModuelDeletage.getCurrCity().getCity());
            SetFavAddressActivity.this.startActivityForResult(intent, 1);
        }

        @Override
        public void onCancel() {
            SetFavAddressActivity.this.finish();
        }

        @Override
        public void onSetFavHomePoi() {
        }

        @Override
        public void onSetFavCompPoi() {
        }

        @Override
        public void onChooseFavHomePoi(PoiItem poiItem) {
        }

        @Override
        public void onChooseFavCompPoi(PoiItem poiItem) {
        }

        @Override
        public void onSelPoiItem(PoiItem poiItem) {
            String poiitemStr = new Gson().toJson(poiItem);
            Intent resIntent = new Intent();
            resIntent.putExtra(MainActivity.FAVTYPE_KEY, mFavType);
            resIntent.putExtra(MainActivity.POIITEM_STR_KEY, poiitemStr);
            SetFavAddressActivity.this.setResult(RESULT_OK, resIntent);
            SetFavAddressActivity.this.finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 表示结果来自城市选择actiivty
        if (requestCode == 1 && requestCode == RESULT_OK) {
            String currCityStr = data.getStringExtra(CityChooseActivity.CURR_CITY_KEY);
            Gson gson = new Gson();
            CityModel cityModel = gson.fromJson(currCityStr, CityModel.class);
            mSearchModuelDeletage.setCity(cityModel);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
