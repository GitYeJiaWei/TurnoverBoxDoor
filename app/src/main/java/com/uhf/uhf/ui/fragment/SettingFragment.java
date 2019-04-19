package com.uhf.uhf.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.uhf.uhf.R;
import com.uhf.uhf.di.component.AppComponent;
import com.uhf.uhf.ui.activity.BleActivity;
import com.uhf.uhf.ui.activity.HelpActivity;
import com.uhf.uhf.ui.activity.InformActivity;
import com.uhf.uhf.ui.activity.PowerActivity;
import com.uhf.uhf.ui.activity.UserActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 * */
public class SettingFragment extends BaseFragment {
    @BindView(R.id.lin_inform)
    LinearLayout linInform;
    @BindView(R.id.lin_user)
    LinearLayout linUser;
    @BindView(R.id.lin_message)
    LinearLayout linMessage;
    @BindView(R.id.lin_ble)
    LinearLayout linBle;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public int setLayout() {
        return R.layout.setting_layout;
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {

    }

    @Override
    public void init(View view) {

    }

    @Override
    public void setBarCode(String barCode) {

    }


    @OnClick({R.id.lin_inform, R.id.lin_user, R.id.lin_message,R.id.lin_ble})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lin_inform:
                startActivity(new Intent(getActivity(), InformActivity.class));
                break;
            case R.id.lin_user:
                startActivity(new Intent(getActivity(), UserActivity.class));
                break;
            case R.id.lin_message:
                startActivity(new Intent(getActivity(), PowerActivity.class));
                break;
            case R.id.lin_ble:
                startActivity(new Intent(getActivity(), BleActivity.class));
                break;
        }
    }
}
