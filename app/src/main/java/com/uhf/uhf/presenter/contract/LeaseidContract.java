package com.uhf.uhf.presenter.contract;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.LeaseBean;
import com.uhf.uhf.ui.BaseView;

import io.reactivex.Observable;

public interface LeaseidContract {
    //Model的接口,数据请求
    interface ILeaseidModel{
        Observable<BaseBean<LeaseBean>> leaseid(String cardCode,String cardType);
    }

    //View的接口，表明View要做的事情
    interface LeaseidView extends BaseView {
        void leaseidResult(BaseBean<LeaseBean> baseBean);
    }
}
