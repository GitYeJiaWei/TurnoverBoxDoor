package com.uhf.uhf.ui.activity;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uhf.uhf.AppApplication;
import com.uhf.uhf.R;
import com.uhf.uhf.common.util.ACache;
import com.uhf.uhf.common.util.ToastUtil;
import com.uhf.uhf.di.component.AppComponent;
import com.uhf.uhf.reader.base.ReaderBase;
import com.uhf.uhf.reader.helper.ReaderHelper;
import com.uhf.uhf.reader.helper.ReaderSetting;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功率设置
 */
public class PowerActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.seekBar1)
    SeekBar seekBar1;
    @BindView(R.id.tvShow1)
    TextView tvShow1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.seekBar2)
    SeekBar seekBar2;
    @BindView(R.id.tvShow2)
    TextView tvShow2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.seekBar3)
    SeekBar seekBar3;
    @BindView(R.id.tvShow3)
    TextView tvShow3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.seekBar4)
    SeekBar seekBar4;
    @BindView(R.id.tvShow4)
    TextView tvShow4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.seekBar5)
    SeekBar seekBar5;
    @BindView(R.id.tvShow5)
    TextView tvShow5;
    @BindView(R.id.tv6)
    TextView tv6;
    @BindView(R.id.seekBar6)
    SeekBar seekBar6;
    @BindView(R.id.tvShow6)
    TextView tvShow6;
    @BindView(R.id.tv7)
    TextView tv7;
    @BindView(R.id.seekBar7)
    SeekBar seekBar7;
    @BindView(R.id.tvShow7)
    TextView tvShow7;
    @BindView(R.id.tv8)
    TextView tv8;
    @BindView(R.id.seekBar8)
    SeekBar seekBar8;
    @BindView(R.id.tvShow8)
    TextView tvShow8;
    @BindView(R.id.btn_sure)
    Button btnSure;
    private ReaderBase mReader;
    private ReaderHelper mReaderHelper;
    private static ReaderSetting m_curReaderSetting;

    @Override
    public int setLayout() {
        return R.layout.activity_power;
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {
        setTitle("功率设置");
        initview();

    }

    private void initview() {
        String key1 = ACache.get(AppApplication.getApplication()).getAsString("key1");
        if (TextUtils.isEmpty(key1)) {
            key1 = "20";
        }
        tvShow1.setText(key1);
        seekBar1.setProgress(Integer.valueOf(key1));

        String key2 = ACache.get(AppApplication.getApplication()).getAsString("key2");
        if (TextUtils.isEmpty(key2)) {
            key2 = "20";
        }
        tvShow2.setText(key2);
        seekBar2.setProgress(Integer.valueOf(key2));

        String key3 = ACache.get(AppApplication.getApplication()).getAsString("key3");
        if (TextUtils.isEmpty(key3)) {
            key3 = "20";
        }
        tvShow3.setText(key3);
        seekBar3.setProgress(Integer.valueOf(key3));

        String key4 = ACache.get(AppApplication.getApplication()).getAsString("key4");
        if (TextUtils.isEmpty(key4)) {
            key4 = "20";
        }
        tvShow4.setText(key4);
        seekBar4.setProgress(Integer.valueOf(key4));

        String key5 = ACache.get(AppApplication.getApplication()).getAsString("key5");
        if (TextUtils.isEmpty(key5)) {
            key5 = "20";
        }
        tvShow5.setText(key5);
        seekBar5.setProgress(Integer.valueOf(key5));

        String key6 = ACache.get(AppApplication.getApplication()).getAsString("key6");
        if (TextUtils.isEmpty(key6)) {
            key6 = "20";
        }
        tvShow6.setText(key6);
        seekBar6.setProgress(Integer.valueOf(key6));

        String key7 = ACache.get(AppApplication.getApplication()).getAsString("key7");
        if (TextUtils.isEmpty(key7)) {
            key7 = "20";
        }
        tvShow7.setText(key7);
        seekBar7.setProgress(Integer.valueOf(key7));

        String key8 = ACache.get(AppApplication.getApplication()).getAsString("key8");
        if (TextUtils.isEmpty(key8)) {
            key8 = "20";
        }
        tvShow8.setText(key8);
        seekBar8.setProgress(Integer.valueOf(key8));

        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2.setOnSeekBarChangeListener(this);
        seekBar3.setOnSeekBarChangeListener(this);
        seekBar4.setOnSeekBarChangeListener(this);
        seekBar5.setOnSeekBarChangeListener(this);
        seekBar6.setOnSeekBarChangeListener(this);
        seekBar7.setOnSeekBarChangeListener(this);
        seekBar8.setOnSeekBarChangeListener(this);

    }


    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
        String key1 = tvShow1.getText().toString();
        ACache.get(AppApplication.getApplication()).put("key1", key1);
        String key2 = tvShow2.getText().toString();
        ACache.get(AppApplication.getApplication()).put("key2", key2);
        String key3 = tvShow3.getText().toString();
        ACache.get(AppApplication.getApplication()).put("key3", key3);
        String key4 = tvShow4.getText().toString();
        ACache.get(AppApplication.getApplication()).put("key4", key4);
        String key5 = tvShow5.getText().toString();
        ACache.get(AppApplication.getApplication()).put("key5", key5);
        String key6 = tvShow6.getText().toString();
        ACache.get(AppApplication.getApplication()).put("key6", key6);
        String key7 = tvShow7.getText().toString();
        ACache.get(AppApplication.getApplication()).put("key7", key7);
        String key8 = tvShow8.getText().toString();
        ACache.get(AppApplication.getApplication()).put("key8", key8);

        try {
            mReaderHelper = ReaderHelper.getDefaultHelper();
            mReader = mReaderHelper.getReader();
        } catch (Exception e) {
            return;
        }
        m_curReaderSetting = mReaderHelper.getCurReaderSetting();
        byte btOutputPower[] = new byte[8];
        try {
            btOutputPower[0] = (byte)Integer.parseInt(key1);
            btOutputPower[1] = (byte)Integer.parseInt(key2);
            btOutputPower[2] = (byte)Integer.parseInt(key3);
            btOutputPower[3] = (byte)Integer.parseInt(key4);
            btOutputPower[4] = (byte)Integer.parseInt(key5);
            btOutputPower[5] = (byte)Integer.parseInt(key6);
            btOutputPower[6] = (byte)Integer.parseInt(key7);
            btOutputPower[7] = (byte)Integer.parseInt(key8);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"InValid number",Toast.LENGTH_SHORT).show();
            return;
        }
        mReader.setOutputPower(m_curReaderSetting.btReadId, btOutputPower);
        m_curReaderSetting.btAryOutputPower = btOutputPower;

        ToastUtil.toast("保存成功");
        finish();
    }

    //拖动中
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBar1:
                tvShow1.setText(progress + "");
                break;
            case R.id.seekBar2:
                tvShow2.setText(progress + "");
                break;
            case R.id.seekBar3:
                tvShow3.setText(progress + "");
                break;
            case R.id.seekBar4:
                tvShow4.setText(progress + "");
                break;
            case R.id.seekBar5:
                tvShow5.setText(progress + "");
                break;
            case R.id.seekBar6:
                tvShow6.setText(progress + "");
                break;
            case R.id.seekBar7:
                tvShow7.setText(progress + "");
                break;
            case R.id.seekBar8:
                tvShow8.setText(progress + "");
                break;
        }
    }

    //开始拖动
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    //停止拖动
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
