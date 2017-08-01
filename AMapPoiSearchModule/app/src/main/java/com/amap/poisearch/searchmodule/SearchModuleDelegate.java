package com.amap.poisearch.searchmodule;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import android.view.View;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.amap.poisearch.searchmodule.AMapSearchUtil.OnSugListener;
import com.amap.poisearch.searchmodule.ISearchModule.IDelegate;
import com.amap.poisearch.searchmodule.ISearchModule.IWidget;
import com.amap.poisearch.util.CityModel;
import com.amap.poisearch.util.CityUtil;
import com.amap.poisearch.util.FavAddressUtil;
import com.amap.poisearch.util.PoiItemDBHelper;

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

    int mPoiType = 0;

    /** 中心地址，检索时会以此ll为中心 */
    private LatLng mCenterLL;


    @Override
    public void bindParentDelegate(IParentDelegate delegate) {
        this.mParentDelegate = delegate;
    }

    @Override
    public void setPoiType(int poiType) {
        mPoiType = poiType;
        if (mWidget != null) {
            mWidget.setPoiType(poiType);
        }
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
        mWidget.setPoiType(mPoiType);

        // 获得context时，对mCurrCity进行检测，如果还没有初始化，则默认为默认值
        if (this.mCurrCity == null) {
            this.mCurrCity = CityUtil.getDefCityModel(context);
        }

        mWidget.setCityName(mCurrCity.getCity());

        init(context);

        return (View)mWidget;
    }

    public void init(Context context) {

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
        if (mWidget != null) {
            mWidget.setCityName(this.mCurrCity.getCity());
        }

        reload(null, true);
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

    private void reload(List<PoiItem> items, boolean hasHistory) {
        ArrayList<PoiListItemData> poiItems = new ArrayList<>();
        if (hasHistory) {
            ArrayList<PoiItem> hisItems = PoiItemDBHelper.getInstance().getLatestPois(mContext, POI_HIS_SIZE * 4);
            if (hisItems != null) {
                for (PoiItem hisItem : hisItems) {

                    if (hisItem == null || mCurrCity == null) {
                        continue;
                    }

                    if (!CityUtil.isSameCity(hisItem.getAdCode(), mCurrCity.getAdcode())) {
                        continue;
                    }

                    poiItems.add(new PoiListItemData(PoiListItemData.HIS_DATA, hisItem));

                    // 历史数据不超过POI_HIS_SIZE数量
                    if (poiItems.size() >= POI_HIS_SIZE) {
                        break;
                    }
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
        mCurrSearchId = java.lang.System.currentTimeMillis();

        if (TextUtils.isEmpty(inputStr)) {
            // 显示收藏的位置的选择panel
            setFavAddressVisible(true);
            reload(null, true);
            return;
        }

        AMapSearchUtil.doSug(mContext, mCurrSearchId, inputStr, mCurrCity.getAdcode(), mCenterLL, new OnSugListener() {
            @Override
            public void onSug(List<PoiItem> list, int i, long searchId) {
                // 只取最新的结果
                if (searchId < mCurrSearchId) {
                    return;
                }

                // 不显示收藏的位置的选择panel
                setFavAddressVisible(false);
                reload(list, false);
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
