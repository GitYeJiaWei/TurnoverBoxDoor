package com.uhf.uhf.data;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.ReturnBean;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.ReturnContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class ReturnModel implements ReturnContract.IReturnModel {
    private ApiService mApiService;

    public ReturnModel(ApiService apiService){
        this.mApiService = apiService;
    }

    @Override
    public Observable<BaseBean<List<ReturnBean>>> Return(String listEpcJson) {
        Map<String,String> map = new HashMap<>();
        map.put("listEpcJson",listEpcJson);
        return mApiService.getReturn(map);
    }
}
