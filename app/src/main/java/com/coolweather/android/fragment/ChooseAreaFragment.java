package com.coolweather.android.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.R;
import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 项目名：     CoolWeather
 * 包名：       com.coolweather.android.fragment
 * 文件名：     ChooseAreaFragment
 * 创建者：     loovee
 * 创建时间：   2017/9/5
 * 描述：      地区显示界面
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVLE_PROVINCE = 0;
    public static final int LEVLE_CITY = 1;
    public static final int LEVLE_COUNTY = 2;

    private TextView tvTitle;
    private Button btnBack;
    private ListView lvArea;
    private ProgressDialog progressDialog;

    private ArrayAdapter<String> adapter ;

    private List<String> dataList = new ArrayList<>();

    /*
    * 省列表
    * */
    private List<Province> provinceList;
    /*
    * 市列表
    * */
    private List<City> cityList;
    /*
    * 县列表
    * */
    private List<County> countyList;

    /*当前选中的级别，用于区分当前的界面显示的数据信息和界面操作*/
    private int currentLevel;
    /*选中的省份*/
    private Province selectedProvince;
    /*选中的城市*/
    private City selectedCity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btnBack = (Button) view.findViewById(R.id.btn_back);
        lvArea = (ListView) view.findViewById(R.id.lv_area);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        lvArea.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel==LEVLE_PROVINCE){
                    /*记录当前点击的是哪个省份，方便在查询城市的时候，利用省份的id进行过滤*/
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if (currentLevel == LEVLE_CITY){
                    /*记录当前点击的是哪个城市，方便在查询县的时候，利用城市的id进行过滤*/
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVLE_COUNTY){
                    queryCities();
                }else if (currentLevel == LEVLE_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }


    /**
    * 查询选中省内所有城市，优先从数据库查询，如果没有查询到再去服务器上查询
    * */
    private void queryCities() {
        tvTitle.setText(selectedProvince.getProvinceName());
        btnBack.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceId = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size()>0){
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            lvArea.setSelection(0);
            currentLevel = LEVLE_CITY;//当前界面为市级界面
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromService(address,"city");
        }
    }

    /*
    * 根据传入的地址和类型从服务器上查询省市县数据
    * */
    private void queryFromService(String address, final String type) {
        showProgressDialog();
        HttpUtil.senOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if (type.equals("city")) {
                                queryCities();
                            }else if (type.equals("county")){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
    * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
    * */
    private void queryCounties() {
        tvTitle.setText(selectedCity.getCityName());
        btnBack.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size()>0){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            lvArea.setSelection(0);
            currentLevel = LEVLE_COUNTY;//当前界面为县级界面
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromService(address,"county");
        }

    }

    /**
    * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器查询
    * */
    private void queryProvinces() {
        tvTitle.setText("中国");
        btnBack.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size()>0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            lvArea.setSelection(0);
            currentLevel = LEVLE_PROVINCE;//当前界面为省级界面
        }else {
            String address = "http://guolin.tech/api/china";
            queryFromService(address,"province");
        }

    }

    /**
    * 关闭进度对话框
    * */
    private void closeProgressDialog() {
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    /**
     * 显示进度对话框
     * */
    private void showProgressDialog() {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

}
