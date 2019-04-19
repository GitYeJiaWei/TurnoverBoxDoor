package com.uhf.uhf.data;

import com.uhf.uhf.bean.LoginBean;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.LoginContract;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class LoginModel implements LoginContract.ILoginModel{
    private ApiService mApiService;

    public LoginModel(ApiService apiService){
        this.mApiService = apiService;
    }

    @Override
    public Observable<LoginBean> login(String userName, String password) {
        Map<String,String> params = new HashMap<>();
        params.put("username",userName);
        params.put("password",password);
        params.put("grant_type","password");
        return mApiService.login(params);
    }
}
