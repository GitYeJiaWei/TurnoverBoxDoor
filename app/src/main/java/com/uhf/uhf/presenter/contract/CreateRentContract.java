package com.uhf.uhf.presenter.contract;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.ui.BaseView;

import io.reactivex.Observable;

public interface CreateRentContract {
    //Model的接口,数据请求
    interface ICreateRentModel{
        Observable<BaseBean<String>> createRent(String ListEpcJson,String CustomerId);
    }

    //View的接口，表明View要做的事情
    interface CreateRentView extends BaseView {
        void createRentResult(BaseBean<String> baseBean);
    }
}
