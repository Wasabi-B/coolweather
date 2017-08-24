package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * 项目名：     CoolWeather
 * 包名：       com.coolweather.android.db
 * 文件名：     Province
 * 创建者：     loovee
 * 创建时间：   2017/8/24
 * 描述：      TODO
 */

public class Province extends DataSupport {

    private int id;
    private String provinceName;  //省份的名字
    private int provinceCode;     //省的代码

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
