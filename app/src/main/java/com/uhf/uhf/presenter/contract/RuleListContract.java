package com.uhf.uhf.presenter.contract;

import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.FeeRule;
import com.uhf.uhf.ui.BaseView;

import java.util.List;

import io.reactivex.Observable;

public interface RuleListContract {
    //Model的接口,数据请求
    interface IFeeRuleModel{
        Observable<BaseBean<FeeRule>> feeRule();
    }

    //View的接口，表明View要做的事情
    interface FeeRuleView extends BaseView {
        void feeRuleResult(BaseBean<FeeRule> baseBean);
    }
}
