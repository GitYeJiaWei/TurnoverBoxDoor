package com.uhf.uhf.presenter;

import com.uhf.uhf.R;
import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.FindBean;
import com.uhf.uhf.common.rx.subscriber.ProgressSubcriber;
import com.uhf.uhf.common.util.NetUtils;
import com.uhf.uhf.common.util.ToastUtil;
import com.uhf.uhf.presenter.contract.FindContract;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FindPresenter extends BasePresenter<FindContract.IFindModel,FindContract.FindView> {
    @Inject
    public FindPresenter(FindContract.IFindModel iFindModel, FindContract.FindView findView) {
        super(iFindModel, findView);
    }

    public void find(String epc){
        if (!NetUtils.isConnected(mContext)){
            ToastUtil.toast(R.string.error_network_unreachable);
            return;
        }
        mModel.find(epc)
                .subscribeOn(Schedulers.io())//访问数据在子线程
                .observeOn(AndroidSchedulers.mainThread())//拿到数据在主线程
                .subscribe(new ProgressSubcriber<BaseBean<FindBean>>(mContext,mView) {
                    @Override
                    public void onNext(BaseBean<FindBean> baseBean) {
                        //当Observable发生事件的时候触发
                        mView.findResult(baseBean);
                    }
                });
    }
}
