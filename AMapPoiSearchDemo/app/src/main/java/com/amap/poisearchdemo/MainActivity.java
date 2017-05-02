package com.amap.poisearchdemo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.amap.api.services.core.PoiItem;
import com.amap.poisearch.searchmodule.ISearchModule.IDelegate.IParentDelegate;
import com.amap.poisearch.searchmodule.SearchModuleDelegate;
import com.amap.poisearch.util.CityModel;
import com.amap.poisearch.util.FavAddressUtil;
import com.amap.poisearch.util.PoiItemDBHelper;
import com.google.gson.Gson;

public class MainActivity extends BaseMapActivity {

    private SearchModuleDelegate mSearchModuelDeletage;

    public static int MAIN_ACTIVITY_REQUEST_FAV_ADDRESS_CODE = 1;

    public static int MAIN_ACTIVITY_REQUEST_CHOOSE_CITY_ADDRESS_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout contentView = (RelativeLayout)findViewById(R.id.content_view);

        mSearchModuelDeletage = new SearchModuleDelegate();
        mSearchModuelDeletage.bindParentDelegate(mSearchModuleParentDelegate);
        contentView.addView(mSearchModuelDeletage.getWidget(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private SearchModuleDelegate.IParentDelegate mSearchModuleParentDelegate = new IParentDelegate() {
        @Override
        public void onChangeCityName() {
            showToast("选择城市");
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, CityChooseActivity.class);
            intent.putExtra(CityChooseActivity.CURR_CITY_KEY, mSearchModuelDeletage.getCurrCity().getCity());
            MainActivity.this.startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CHOOSE_CITY_ADDRESS_CODE);
        }

        @Override
        public void onCancel() {
            showToast("取消");
        }

        private void toSetFavAddressActivity(int type) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, SetFavAddressActivity.class);
            intent.putExtra(FAVTYPE_KEY, type);
            Gson gson = new Gson();
            intent.putExtra(SetFavAddressActivity.CURR_CITY_KEY, gson.toJson(mSearchModuelDeletage.getCurrCity()));
            startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_FAV_ADDRESS_CODE);
        }
        @Override
        public void onSetFavHomePoi() {
            showToast("设置家的地址");
            toSetFavAddressActivity(0);
        }

        @Override
        public void onSetFavCompPoi() {
            showToast("设置公司地址");
            toSetFavAddressActivity(1);
        }

        @Override
        public void onChooseFavHomePoi(PoiItem poiItem) {
        }

        @Override
        public void onChooseFavCompPoi(PoiItem poiItem) {
        }

        @Override
        public void onSelPoiItem(PoiItem poiItem) {
            saveToCache(poiItem);
            showToast("选择了检索结果的 " + poiItem.getTitle());
        }

        private void saveToCache(PoiItem poiItem) {
            PoiItemDBHelper.getInstance().savePoiItem(MainActivity.this, poiItem);
        }
    };


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public static final String FAVTYPE_KEY = "favtype";
    public static final String POIITEM_STR_KEY = "poiitem_str";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (MAIN_ACTIVITY_REQUEST_FAV_ADDRESS_CODE == requestCode && resultCode == RESULT_OK) {
            String poiitemStr = data.getStringExtra(POIITEM_STR_KEY);
            int favType = data.getIntExtra(FAVTYPE_KEY, -1);

            PoiItem poiItem = new Gson().fromJson(poiitemStr, PoiItem.class);

            if (favType == 0) {
                FavAddressUtil.saveFavHomePoi(this, poiItem);
                mSearchModuelDeletage.setFavHomePoi(poiItem);
            } else if (favType == 1) {
                FavAddressUtil.saveFavCompPoi(this, poiItem);
                mSearchModuelDeletage.setFavCompPoi(poiItem);
            }
        }

        if (MAIN_ACTIVITY_REQUEST_CHOOSE_CITY_ADDRESS_CODE == requestCode && resultCode == RESULT_OK) {
            String currCityStr = data.getStringExtra(CityChooseActivity.CURR_CITY_KEY);
            Gson gson = new Gson();
            CityModel cityModel = gson.fromJson(currCityStr, CityModel.class);
            mSearchModuelDeletage.setCity(cityModel);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

