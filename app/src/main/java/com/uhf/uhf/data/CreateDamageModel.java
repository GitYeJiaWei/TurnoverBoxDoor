package com.uhf.uhf.data;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.CreateDamageContract;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class CreateDamageModel implements CreateDamageContract.ICreateDamageModel {
    @Override
    public Observable<BaseBean<String>> createDamage(String ListEpcJson) {
        Map<String,String> map = new HashMap<>();
        map.put("ListEpcJson",ListEpcJson);
        return mApiService.createDamage(map);
    }

    private ApiService mApiService;

    public CreateDamageModel(ApiService apiService){
        this.mApiService = apiService;
    }

}
