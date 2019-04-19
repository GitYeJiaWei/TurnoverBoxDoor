package com.uhf.uhf.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.uhf.uhf.AppApplication;
import com.uhf.uhf.R;
import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.EPC;
import com.uhf.uhf.bean.LeaseBean;
import com.uhf.uhf.common.util.ACache;
import com.uhf.uhf.common.util.SoundManage;
import com.uhf.uhf.common.util.ToastUtil;
import com.uhf.uhf.di.component.AppComponent;
import com.uhf.uhf.di.component.DaggerLeaseComponent;
import com.uhf.uhf.di.module.LeaseidModule;
import com.uhf.uhf.presenter.LeaseidPresenter;
import com.uhf.uhf.presenter.contract.LeaseidContract;
import com.uhf.uhf.ui.activity.ReturnActivity;
import com.uhf.uhf.ui.adapter.InitListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 退还
 */
public class ReturnFragment extends BaseFragment<LeaseidPresenter> implements LeaseidContract.LeaseidView {
    @BindView(R.id.btn_lease)
    Button btnLease;
    String cardCode;
    @BindView(R.id.list_lease)
    ListView listLease;
    @BindView(R.id.et_back)
    EditText etBack;
    @BindView(R.id.btn_clear)
    Button btnClear;
    private InitListAdapter initListAdapter;

    public static ReturnFragment newInstance() {
        return new ReturnFragment();
    }

    @Override
    public int setLayout() {
        return R.layout.return_layout;
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {
        DaggerLeaseComponent.builder().appComponent(appComponent).leaseidModule(new LeaseidModule(this))
                .build().inject(this);
    }

    @Override
    public void init(View view) {
        initListAdapter = new InitListAdapter(getActivity(), "returnResult");
        listLease.setAdapter(initListAdapter);

        ACache aCache = ACache.get(AppApplication.getApplication());
        ArrayList<EPC> leaseResultlist = (ArrayList<EPC>) aCache.getAsObject("returnResult");
        if (leaseResultlist != null) {
            initListAdapter.updateDatas(leaseResultlist);
        }
        etBack.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public void setBarCode(String barCode) {

    }

    @Override
    public void myOnKeyDwon() {
        readTag();
    }


    private void readTag() {
        String entity = etBack.getText().toString().trim();
        if (!TextUtils.isEmpty(entity)) {
            SoundManage.PlaySound(AppApplication.getApplication(), SoundManage.SoundType.SUCCESS);
            cardCode = entity;
            mPresenter.leaseid(cardCode, "2");
        } else {
            SoundManage.PlaySound(mActivity, SoundManage.SoundType.FAILURE);
            ToastUtil.toast("退还卡扫描失败,请将感应模块贴近卡片重新扫描");
        }
    }

    @OnClick({R.id.btn_lease,R.id.btn_clear})
    public void onViewClicked(View view)
    {
        switch (view.getId()){
            case R.id.btn_lease:
                readTag();
                break;
            case R.id.btn_clear:
                etBack.setText("");
                break;
        }
    }

    @Override
    public void leaseidResult(BaseBean<LeaseBean> baseBean) {
        if (baseBean == null) {
            ToastUtil.toast("扫描退还卡失败");
            return;
        }
        if (baseBean.getCode() == 0) {
            ToastUtil.toast("扫描退还卡成功");
            Intent intent = new Intent(AppApplication.getApplication(), ReturnActivity.class);
            intent.putExtra("TID", cardCode);
            intent.putExtra("cardCode", baseBean.getData());
            startActivity(intent);
        } else {
            ToastUtil.toast(baseBean.getMessage());
        }
    }

    @Override
    public void showError(String msg) {
        ToastUtil.toast("操作失败,请退出重新登录");
    }
}
