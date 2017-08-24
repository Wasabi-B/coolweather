package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 项目名：     CoolWeather
 * 包名：       com.coolweather.android.db
 * 文件名：     City
 * 创建者：     loovee
 * 创建时间：   2017/8/24
 * 描述：      TODO
 */

public class City extends DataSupport {
    private int id;
    private String cityName;        //城市名字
    private int cityCode;           //城市代码
    private int provinceId;         //所属省份ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
