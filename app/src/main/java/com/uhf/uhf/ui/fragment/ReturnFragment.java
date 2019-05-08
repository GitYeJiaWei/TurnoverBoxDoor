package com.uhf.uhf.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.uhf.uhf.AppApplication;
import com.uhf.uhf.R;
import com.uhf.uhf.bean.EPC;
import com.uhf.uhf.common.util.ACache;
import com.uhf.uhf.di.component.AppComponent;
import com.uhf.uhf.ui.activity.ReturnActivity;
import com.uhf.uhf.ui.adapter.InitListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 退还
 */
public class ReturnFragment extends BaseFragment {
    @BindView(R.id.btn_lease)
    Button btnLease;
    @BindView(R.id.list_lease)
    ListView listLease;
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
    }

    @Override
    public void setBarCode(String barCode) {
    }

    @OnClick({R.id.btn_lease})
    public void onViewClicked() {
        Intent intent = new Intent(AppApplication.getApplication(), ReturnActivity.class);
        startActivity(intent);
    }
}
