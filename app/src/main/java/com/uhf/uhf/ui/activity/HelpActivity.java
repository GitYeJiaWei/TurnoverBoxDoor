package com.uhf.uhf.ui.activity;

import android.content.Intent;

import com.qs.helper.printer.PrintService;
import com.qs.helper.printer.PrinterClass;
import com.uhf.uhf.R;
import com.uhf.uhf.di.component.AppComponent;



public class HelpActivity extends BaseActivity {

    @Override
    public int setLayout() {
        return R.layout.activity_help;
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {

    }

    @Override
    public void init() {
        setTitle("蓝牙设置");
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        if (PrintService.pl != null && (position == 0 || position == 1) && PrintService.pl.getState() != PrinterClass.STATE_CONNECTED) {
            intent = new Intent();
            intent.setClass(HelpActivity.this, PrintSettingActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 0:
                if (PrintService.pl.getState() != PrinterClass.STATE_CONNECTED) {
                    HelpActivity.this.finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        //断开打印连接
        //PrintService.pl.disconnect();
        super.onDestroy();
    }
}

