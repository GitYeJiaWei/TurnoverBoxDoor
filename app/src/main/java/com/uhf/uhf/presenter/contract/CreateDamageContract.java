package com.uhf.uhf.presenter.contract;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.ui.BaseView;

import io.reactivex.Observable;

public interface CreateDamageContract {
    //Model的接口,数据请求
    interface ICreateDamageModel{
        Observable<BaseBean<String>> createDamage(String ListEpcJson);
    }

    //View的接口，表明View要做的事情
    interface CreateDamageView extends BaseView {
        void createDamageResult(BaseBean<String> baseBean);
    }
}
