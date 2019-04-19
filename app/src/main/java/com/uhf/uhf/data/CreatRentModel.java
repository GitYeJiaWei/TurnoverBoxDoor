package com.uhf.uhf.data;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.CreateRentContract;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class CreatRentModel implements CreateRentContract.ICreateRentModel{
    @Override
    public Observable<BaseBean<String>> createRent(String ListEpcJson, String CustomerId) {
        Map<String,String> map = new HashMap<>();
        map.put("ListEpcJson",ListEpcJson);
        map.put("CustomerId",CustomerId);
        return mApiService.createRent(map);
    }

    private ApiService mApiService;

    public CreatRentModel(ApiService apiService){
        this.mApiService = apiService;
    }

}
