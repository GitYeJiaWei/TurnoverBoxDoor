package com.uhf.uhf.presenter.contract;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.ui.BaseView;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public interface SettingContract {
    //Model的接口,数据请求
    interface ISettingModel{
        Observable<BaseBean<String>> setting(String password, String newpassword, String twoPassword);
    }

    //View的接口，表明View要做的事情
    interface SettingView extends BaseView {
        void settingResult(BaseBean<String> baseBean);
    }
}
