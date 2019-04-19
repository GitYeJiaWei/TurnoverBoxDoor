package com.uhf.uhf.data;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.LeaseBean;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.LeaseidContract;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class LeaseidModel implements LeaseidContract.ILeaseidModel{
    private ApiService mApiService;

    public LeaseidModel(ApiService apiService){
        this.mApiService = apiService;
    }

    @Override
    public Observable<BaseBean<LeaseBean>> leaseid(String cardCode,String cardType) {
        Map<String,String> map = new HashMap<>();
        map.put("cardCode",cardCode);
        map.put("cardType",cardType);
        return mApiService.leaseid(map);
    }
}
