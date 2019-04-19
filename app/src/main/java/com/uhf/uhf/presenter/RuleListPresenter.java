package com.uhf.uhf.presenter;

import com.uhf.uhf.R;
import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.FeeRule;
import com.uhf.uhf.common.rx.subscriber.ProgressSubcriber;
import com.uhf.uhf.common.util.NetUtils;
import com.uhf.uhf.common.util.ToastUtil;
import com.uhf.uhf.presenter.contract.RuleListContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RuleListPresenter extends BasePresenter<RuleListContract.IFeeRuleModel,RuleListContract.FeeRuleView>{
    @Inject
    public RuleListPresenter(RuleListContract.IFeeRuleModel iFeeRuleModel, RuleListContract.FeeRuleView feeRuleView) {
        super(iFeeRuleModel, feeRuleView);
    }

    public void feeRule(){
        if (!NetUtils.isConnected(mContext)){
            ToastUtil.toast(R.string.error_network_unreachable);
            return;
        }
        mModel.feeRule()
                .subscribeOn(Schedulers.io())//访问数据在子线程
                .observeOn(AndroidSchedulers.mainThread())//拿到数据在主线程
                .subscribe(new ProgressSubcriber<BaseBean<FeeRule>>(mContext,mView) {
                    @Override
                    public void onNext(BaseBean<FeeRule> baseBean) {
                        //当Observable发生事件的时候触发
                        mView.feeRuleResult(baseBean);
                    }
                });
    }
}
