package com.uhf.uhf.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.uhf.uhf.AppApplication;
import com.uhf.uhf.R;
import com.uhf.uhf.bean.LoginBean;
import com.uhf.uhf.common.ActivityCollecter;
import com.uhf.uhf.common.util.ACache;
import com.uhf.uhf.common.util.ToastUtil;
import com.uhf.uhf.di.component.AppComponent;
import com.uhf.uhf.di.component.DaggerLoginComponent;
import com.uhf.uhf.di.module.LoginModule;
import com.uhf.uhf.presenter.LoginPresenter;
import com.uhf.uhf.presenter.contract.LoginContract;
import com.uhf.uhf.reader.helper.ReaderHelper;
import com.uhf.uhf.serialport.SerialPort;
import com.uhf.uhf.serialport.SerialPortFinder;
import com.uhf.uhf.ui.widget.LoadingButton;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 1.MVP 大家都知道 P的作用是让MV间接拥有肮脏的PY交易，而不是直接让他们进行交易。
 * 2.Rxjava 响应式编程 0.0 一个特别屌的地方就是你可以随便切换线程,异步
 * 3.Retrofit 代替Volley的东东，网络请求
 * 4.Dagger2 Android 的IOC框架，即控制反转，也叫依赖注入，解耦用的
 * 4.DataBinding MVVM的东东，用起来比较方便，可以让bean与View绑定，抛弃setText()!
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.LoginView {

    @BindView(R.id.rfid)
    ImageView rfid;
    @BindView(R.id.txt_mobi)
    EditText txtMobi;
    @BindView(R.id.txt_password)
    EditText txtPassword;
    @BindView(R.id.btn_login)
    LoadingButton btnLogin;

    public static SerialPort mSerialPort = null;
    private int baud = 115200;
    private ReaderHelper mReaderHelper;

    public static final String REAL_NAME = "realname";
    public static final String USER_NAME = "username";
    public static final String PASS_WORD = "password";
    public static final String TOKEN ="token";

    @Override
    public int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {
        //mPresenter 不再以new的形式创建
        //Dagger就是依赖注入，解耦用的。常见的使用地方就是注入Presenter到Activity中
        //其实就是使用Component创建一个Presenter，而Presenter所需的参数都是由Moudule提供的

        DaggerLoginComponent.builder().appComponent(appComponent).loginModule(new LoginModule(this))
                .build().inject(this);
    }

    @Override
    public void init() {
        String username = ACache.get(AppApplication.getApplication()).getAsString(USER_NAME);
        String password = ACache.get(AppApplication.getApplication()).getAsString(PASS_WORD);

        if (!TextUtils.isEmpty(username)) {
            txtMobi.setText(username);
        }
        if (!TextUtils.isEmpty(password)) {
            txtPassword.setText(password);
        }
        initView();

    }

    private void initView(){
        //初始化二维码扫描头
        if (Build.VERSION.SDK_INT > 21) {

            //扫条码 需要相机对应用开启相机和存储权限；
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            } else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
            }
            //读写内存权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 请求权限
                ActivityCompat
                        .requestPermissions(
                                this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                2);
            }

        } else {
            //这个说明系统版本在6.0之下，不需要动态获取权限。
        }
    }

    @Override
    public void loginResult(LoginBean baseBean) {
        if (baseBean == null) {
            ToastUtil.toast("登陆失败");
            return;
        }

        try {
            mSerialPort = new SerialPort(new File("/dev/ttyS1"), baud, 0);
            try {
                mReaderHelper = ReaderHelper.getDefaultHelper();
                mReaderHelper.setReader(mSerialPort.getInputStream(), mSerialPort.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
                return ;
            }

            ACache.get(AppApplication.getApplication()).put(REAL_NAME, baseBean.getRealName());
            ACache.get(AppApplication.getApplication()).put(USER_NAME, txtMobi.getText().toString());
            ACache.get(AppApplication.getApplication()).put(PASS_WORD, txtPassword.getText().toString());
            ACache.get(AppApplication.getApplication()).put(TOKEN, baseBean.getAccess_token());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            ToastUtil.toast("登陆成功");
        } catch (SecurityException e) {
            ToastUtil.toast(R.string.error_security);
        } catch (IOException e) {
            ToastUtil.toast(R.string.error_unknown);
        } catch (InvalidParameterException e) {
            ToastUtil.toast(R.string.error_configuration);
        }
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        String username = txtMobi.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            ToastUtil.toast("请输入账号");
            return;
        }
        String password = txtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.toast("请输入密码");
            return;
        }
        ACache.get(AppApplication.getApplication()).put(TOKEN, "");
        mPresenter.login(username, password);
    }

    @Override
    public void showLoading() {
        btnLogin.showLoading();
    }

    @Override
    public void showError(String msg) {
        btnLogin.showButtonText();
        ToastUtil.toast("操作失败,请退出重新登录");
    }

    @Override
    public void dismissLoading() {
        btnLogin.showButtonText();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            ActivityCollecter.finishAll();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
