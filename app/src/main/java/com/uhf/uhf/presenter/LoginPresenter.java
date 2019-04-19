package com.uhf.uhf.presenter;

import com.uhf.uhf.R;
import com.uhf.uhf.bean.LoginBean;
import com.uhf.uhf.common.rx.subscriber.ProgressSubcriber;
import com.uhf.uhf.common.util.NetUtils;
import com.uhf.uhf.common.util.ToastUtil;
import com.uhf.uhf.presenter.contract.LoginContract;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 桥梁，用来处理model和view
 */
public class LoginPresenter extends BasePresenter<LoginContract.ILoginModel,LoginContract.LoginView>{

    //把presenter注入到activity，那么就要在presenter的构造函数添加注解@Inject
    @Inject
    public LoginPresenter(LoginContract.ILoginModel iLoginModel, LoginContract.LoginView loginView) {
        super(iLoginModel, loginView);
    }

    public void login(String userName,String passord){
        if (!NetUtils.isConnected(mContext)){
            ToastUtil.toast(R.string.error_network_unreachable);
            return;
        }
        mModel.login(userName,passord)
                .subscribeOn(Schedulers.io())//访问数据在子线程
                .observeOn(AndroidSchedulers.mainThread())//拿到数据在主线程
                .subscribe(new ProgressSubcriber<LoginBean>(mContext,mView) {
                    @Override
                    public void onNext(LoginBean baseBean) {
                        //当Observable发生事件的时候触发
                        mView.loginResult(baseBean);
                    }
                });
    }
}
