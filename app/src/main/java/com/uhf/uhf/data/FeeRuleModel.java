package com.uhf.uhf.data;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.FeeRule;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.RuleListContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class FeeRuleModel implements RuleListContract.IFeeRuleModel {
    private ApiService mApiService;

    public FeeRuleModel(ApiService apiService){
        this.mApiService = apiService;
    }

    @Override
    public Observable<BaseBean<FeeRule>> feeRule() {
        Map<String,String> map = new HashMap<>();
        return mApiService.rulelist(map);
    }
}
