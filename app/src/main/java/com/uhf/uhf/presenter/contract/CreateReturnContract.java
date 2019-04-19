package com.uhf.uhf.presenter.contract;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.ui.BaseView;

import io.reactivex.Observable;

public interface CreateReturnContract {
    //Model的接口,数据请求
    interface ICreateReturnModel{
        Observable<BaseBean<String>> createReturn(String customerId,String damageFee,String listEpcJson);
    }

    //View的接口，表明View要做的事情
    interface CreateReturnView extends BaseView {
        void createReturnResult(BaseBean<String> baseBean);
    }
}
