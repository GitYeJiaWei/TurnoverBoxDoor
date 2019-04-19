package com.uhf.uhf.data;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.CreateReturnContract;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class CreateReturnModel implements CreateReturnContract.ICreateReturnModel {
    @Override
    public Observable<BaseBean<String>> createReturn(String customerId,String damageFee,String listEpcJson) {
        Map<String,String> map = new HashMap<>();
        map.put("customerId",customerId);
        map.put("damageFee",damageFee);
        map.put("listEpcJson",listEpcJson);
        return mApiService.createReturn(map);
    }

    private ApiService mApiService;

    public CreateReturnModel(ApiService apiService){
        this.mApiService = apiService;
    }
}
