package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 项目名：     CoolWeather
 * 包名：       com.coolweather.android.db
 * 文件名：     County
 * 创建者：     loovee
 * 创建时间：   2017/8/24
 * 描述：      TODO
 */

public class County extends DataSupport {
    private int id;
    private String countyName;      //县的名字
    private String weatherId;       //对应的天气ID
    private int cityId;             //所属城市ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
