package com.uhf.uhf.presenter;

import com.uhf.uhf.R;
import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.common.rx.subscriber.ProgressSubcriber;
import com.uhf.uhf.common.util.NetUtils;
import com.uhf.uhf.common.util.ToastUtil;
import com.uhf.uhf.presenter.contract.CreateDamageContract;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateDamagePresenter extends BasePresenter<CreateDamageContract.ICreateDamageModel,CreateDamageContract.CreateDamageView> {
    @Inject
    public CreateDamagePresenter(CreateDamageContract.ICreateDamageModel iCreateDamageModel, CreateDamageContract.CreateDamageView createDamageView) {
        super(iCreateDamageModel, createDamageView);
    }

    public void createDamage(String ListEpcJson){
        if (!NetUtils.isConnected(mContext)){
            ToastUtil.toast(R.string.error_network_unreachable);
            return;
        }
        mModel.createDamage(ListEpcJson)
                .subscribeOn(Schedulers.io())//访问数据在子线程
                .observeOn(AndroidSchedulers.mainThread())//拿到数据在主线程
                .subscribe(new ProgressSubcriber<BaseBean<String>>(mContext,mView) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        //当Observable发生事件的时候触发
                        mView.createDamageResult(baseBean);
                    }
                });
    }
}
