package com.amap.poisearch.searchmodule;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.poisearch.searchmodule.AMapSearchUtil.OnLatestPoiSearchListener;
import  com.amap.poisearch.searchmodule.ISearchModule.IDelegate;
import  com.amap.poisearch.searchmodule.ISearchModule.IWidget;
import  com.amap.poisearch.util.CityModel;
import  com.amap.poisearch.util.CityUtil;
import  com.amap.poisearch.util.FavAddressUtil;
import  com.amap.poisearch.util.PoiItemDBHelper;

/**
 * Created by liangchao_suxun on 2017/4/26.
 */

public class SearchModuleDelegate implements IDelegate {

    private IWidget mWidget;

    /**
     * 默认为北京
     */
    private CityModel mCurrCity;

    private int pageSize = 10;
    private Context mContext;

    private String mCategory = "";


    private PoiItem mFavHomePoi;
    private PoiItem mFavCompPoi;

    private IParentDelegate mParentDelegate;

    @Override
    public void bindParentDelegate(IParentDelegate delegate) {
        this.mParentDelegate = delegate;
    }

    @Override
    public View getWidget(Context context) {
        if (context == null) {
            return null;
        }

        this.mContext = context;

        if (mWidget != null) {
            return (View)mWidget;
        }

        mWidget = new SearchModuleWidget(context);
        mWidget.bindDelegate(this);

        init(context);

        return (View)mWidget;
    }

    public void init(Context context) {
        // 获得context时，对mCurrCity进行检测，如果还没有初始化，则默认为默认值
        if (this.mCurrCity == null) {
            setCity(CityUtil.getDefCityModel(context));
        }

        mFavHomePoi = FavAddressUtil.getFavHomePoi(context);
        mFavCompPoi = FavAddressUtil.getFavCompPoi(context);

        if (mFavHomePoi != null) {
            mWidget.setFavHomeAddress(mFavHomePoi.getTitle());
        }

        if (mFavCompPoi != null) {
            mWidget.setFavCompAddress(mFavCompPoi.getTitle());
        }

        reload(null, true);
    }

    public CityModel getCurrCity() {
        return mCurrCity;
    }

    @Override
    public void setCity(CityModel city) {
        if (city == null) {
            return;
        }

        this.mCurrCity = city;
        mWidget.setCityName(this.mCurrCity.getCity());

    }

    @Override
    public void setCurrLoc(Location currLoc) {
        mWidget.setCurrLoc(currLoc);
    }

    @Override
    public void setFavAddressVisible(boolean isVisible) {
        mWidget.setFavAddressVisible(isVisible);
    }

    @Override
    public void setFavHomePoi(PoiItem address) {
        if (address == null) {
            return;
        }
        this.mFavHomePoi = address;
        mWidget.setFavHomeAddress(mFavHomePoi.getTitle());
    }

    @Override
    public void setFavCompPoi(PoiItem address) {
        if (address == null) {
            return;
        }

        this.mFavCompPoi = address;
        mWidget.setFavCompAddress(mFavCompPoi.getTitle());
    }

    private static final int POI_HIS_SIZE = 5;

    private void reload(ArrayList<PoiItem> items , boolean hasHistory) {
        ArrayList<PoiListItemData> poiItems = new ArrayList<>();

        if (hasHistory) {
            ArrayList<PoiItem> hisItems = PoiItemDBHelper.getInstance().getLatestPois(mContext, POI_HIS_SIZE);
            if (hisItems != null) {
                for (PoiItem hisItem : hisItems) {
                    poiItems.add(new PoiListItemData(PoiListItemData.HIS_DATA, hisItem));
                }
            }
        }

        if (items != null) {
            for (PoiItem searchItem : items) {
                poiItems.add(new PoiListItemData(PoiListItemData.SEARCH_DATA, searchItem));
            }
        }

        mWidget.reloadPoiList(poiItems);
    }

    private long mCurrSearchId = 0;
    @Override
    public void onSearch(String inputStr) {
        mCurrSearchId= java.lang.System.currentTimeMillis();

        if (TextUtils.isEmpty(inputStr)) {
            // 显示收藏的位置的选择panel
            setFavAddressVisible(true);
            reload(null, true);
            return;
        }

        AMapSearchUtil.doPoiSearch(mContext, mCurrSearchId, inputStr, mCategory,
            mCurrCity.getAdcode(), 0, this.pageSize, new AMapSearchUtil.OnLatestPoiSearchListener() {
                @Override
                public void onPoiSearched(PoiResult poiResult, int i, long searchId) {
                    // 只取最新的结果
                    if (searchId < mCurrSearchId) {
                        return;
                    }

                    // 不显示收藏的位置的选择panel
                    setFavAddressVisible(false);
                    ArrayList<PoiItem> items = poiResult.getPois();
                    reload(items, false);
                }

                @Override
                public void onPoiItemSearched(PoiItem poiItem, int i, long searchId) {
                }
            });

    }

    @Override
    public void onChangeCityName() {
        if (mParentDelegate == null) {
            return;
        }

        mParentDelegate.onChangeCityName();
    }

    @Override
    public void onCancel() {
        if (mParentDelegate == null) {
            return;
        }

        mParentDelegate.onCancel();
    }

    @Override
    public void onSetFavHomePoi() {
        if (mParentDelegate == null) {
            return;
        }
        mParentDelegate.onSetFavHomePoi();
    }

    @Override
    public void onSetFavCompPoi() {
        if (mParentDelegate == null) {
            return;
        }
        mParentDelegate.onSetFavCompPoi();
    }

    @Override
    public void onChooseFavHomePoi() {
        if (mParentDelegate == null) {
            return;
        }
        mParentDelegate.onChooseFavHomePoi(mFavHomePoi);
    }

    @Override
    public void onChooseFavCompPoi() {
        if (mParentDelegate == null) {
            return;
        }
        mParentDelegate.onChooseFavCompPoi(mFavCompPoi);
    }

    @Override
    public void onSelPoiItem(PoiItem poiItem) {
        if (mParentDelegate == null) {
            return;
        }
        mParentDelegate.onSelPoiItem(poiItem);
    }
}
