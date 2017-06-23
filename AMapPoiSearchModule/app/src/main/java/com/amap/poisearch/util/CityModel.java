package com.amap.poisearch.util;

import android.os.Parcel;
import android.os.Parcelable;
import com.amap.api.maps.offlinemap.OfflineMapCity;

/**
 * Created by liangchao_suxun on 2017/4/27.
 */

public class CityModel implements Parcelable{
    private String adcode;
    private String city;
    private String code;
    private String jianpin;
    private String pinyin;
    private double lat;
    private double lng;

    public CityModel() {
        ;
    }

    public CityModel(OfflineMapCity mapCity) {
        if (mapCity == null) {
            return;
        }
        this.adcode = mapCity.getAdcode();
        this.city = mapCity.getCity();
        this.code = mapCity.getCode();
        this.jianpin = mapCity.getJianpin();
        this.pinyin = mapCity.getPinyin();
        this.type = 0;
    }

    public static CityModel createGroupModel(char groupChar) {
        CityModel res = new CityModel();
        res.city = String.valueOf(groupChar);
        res.type = 1;
        return res;
    }

    public boolean isGroupModel(){
        return type == 1;
    }

    /** 在选择城市列表展示时会用到。0 == 普通的城市数据 ， 1==group数据。*/
    public int type;

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getJianpin() {
        return jianpin;
    }

    public void setJianpin(String jianpin) {
        this.jianpin = jianpin;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }


    public static final Creator<CityModel> CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new CityModel(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    public static Creator<CityModel> getCreator() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adcode);
        dest.writeString(city);
        dest.writeString(code);
        dest.writeString(jianpin);
        dest.writeString(pinyin);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    protected CityModel(Parcel parcel) {
        adcode = parcel.readString();
        city = parcel.readString();
        code = parcel.readString();
        jianpin = parcel.readString();
        pinyin = parcel.readString();
        lat = parcel.readDouble();
        lng = parcel.readDouble();
    }
}

