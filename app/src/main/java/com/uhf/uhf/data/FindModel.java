package com.uhf.uhf.data;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.FindBean;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.FindContract;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class FindModel implements FindContract.IFindModel {
    @Override
    public Observable<BaseBean<FindBean>> find(String epc) {
        Map<String,String> map = new HashMap<>();
        map.put("epc",epc);
        return mApiService.find(map);
    }

    private ApiService mApiService;

    public FindModel(ApiService apiService){
        this.mApiService = apiService;
    }
}
