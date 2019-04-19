package com.uhf.uhf.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qs.helper.printer.PrintService;
import com.qs.helper.printer.PrinterClass;
import com.uhf.uhf.AppApplication;
import com.uhf.uhf.R;
import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.EPC;
import com.uhf.uhf.bean.FeeRule;
import com.uhf.uhf.bean.LeaseBean;
import com.uhf.uhf.common.util.ACache;
import com.uhf.uhf.common.util.ToastUtil;
import com.uhf.uhf.di.component.AppComponent;
import com.uhf.uhf.di.component.DaggerCreatRentComponent;
import com.uhf.uhf.di.module.CreatRentModule;
import com.uhf.uhf.presenter.CreatRentPresenter;
import com.uhf.uhf.presenter.contract.CreateRentContract;
import com.uhf.uhf.reader.base.ReaderBase;
import com.uhf.uhf.reader.helper.InventoryBuffer;
import com.uhf.uhf.reader.helper.ReaderHelper;
import com.uhf.uhf.reader.helper.ReaderSetting;
import com.uhf.uhf.ui.adapter.LeaseScanadapter;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class LeaseActivity extends BaseActivity<CreatRentPresenter> implements CreateRentContract.CreateRentView {

    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.list_lease)
    ListView listLease;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_print)
    Button btnPrint;
    LeaseScanadapter leaseScanadapter = null;
    @BindView(R.id.lin_lease)
    LinearLayout linLease;
    @BindView(R.id.tv_tid)
    TextView tvTid;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    private ArrayList<EPC> epclist = new ArrayList<>();
    LeaseBean leaseBean;
    private ConcurrentHashMap<String, List<EPC>> hashMap = new ConcurrentHashMap<>();
    private HashMap<String, String> map = new HashMap<>();
    private BaseBean<FeeRule> baseBean = null;

    private ReaderBase mReader;
    private ReaderHelper mReaderHelper;
    private static InventoryBuffer m_curInventoryBuffer;
    private static ReaderSetting m_curReaderSetting;
    private int epcNum=0;
    private double sum = 0;
    String leaseResult = null;

    @Override
    public int setLayout() {
        return R.layout.activity_lease;
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {
        DaggerCreatRentComponent.builder().appComponent(appComponent).creatRentModule(new CreatRentModule(this))
                .build().inject(this);
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        if (intent != null) {
            leaseBean = (LeaseBean) intent.getSerializableExtra("cardCode");
            String Tid = intent.getStringExtra("TID");
            tvName.setText(leaseBean.getContactName());
            tvTid.setText(Tid);
        }
        if (leaseBean == null) {
            ToastUtil.toast("获取数据失败");
            finish();
        }

        setTitle("扫描出库");
        linLease.setVisibility(View.GONE);
        hashMap.clear();
        map.clear();
        leaseScanadapter = new LeaseScanadapter(this, "lease");
        listLease.setAdapter(leaseScanadapter);

        baseBean = (BaseBean<FeeRule>)ACache.get(AppApplication.getApplication()).getAsObject("feeRule");
        if (baseBean==null){
            finish();
        }

        try {
            mReaderHelper = ReaderHelper.getDefaultHelper();
            mReader = mReaderHelper.getReader();
        } catch (Exception e) {
            return;
        }

        epcNum = 0;

        m_curReaderSetting = mReaderHelper.getCurReaderSetting();
        m_curInventoryBuffer = mReaderHelper.getCurInventoryBuffer();

        m_curInventoryBuffer.lAntenna.add(new Byte((byte) 0x00));
        m_curInventoryBuffer.lAntenna.add(new Byte((byte) 0x01));
        m_curInventoryBuffer.lAntenna.add(new Byte((byte) 0x02));
        m_curInventoryBuffer.lAntenna.add(new Byte((byte) 0x03));
        m_curInventoryBuffer.lAntenna.add(new Byte((byte) 0x04));
        m_curInventoryBuffer.lAntenna.add(new Byte((byte) 0x05));
        m_curInventoryBuffer.lAntenna.add(new Byte((byte) 0x06));
        m_curInventoryBuffer.lAntenna.add(new Byte((byte) 0x07));

    }

    @OnClick({R.id.btn_scan, R.id.btn_commit, R.id.btn_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                linLease.setVisibility(View.VISIBLE);
                startstop();
                break;
            case R.id.btn_commit:
                if (!btnScan.getText().toString()
                        .equals(getResources().getString(R.string.lease_next))){
                    startstop();
                }
                ArrayList<String> arrayList = new ArrayList<>();
                Iterator it = hashMap.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    for (int i = 0; i < hashMap.get(key).size(); i++) {
                        arrayList.add(hashMap.get(key).get(i).getData1());
                    }
                }
                mPresenter.creatrent(AppApplication.getGson().toJson(arrayList), leaseBean.getId());
                break;
            case R.id.btn_print:
                if (!btnScan.getText().toString()
                        .equals(getResources().getString(R.string.lease_next))){
                    startstop();
                }
                createDialog();
                break;
        }
    }

    private void print(){
            if (PrintService.pl == null || PrintService.pl.getState() != PrinterClass.STATE_CONNECTED) {
                ToastUtil.toast("请先到设置中连接打印机");
                startActivity(new Intent(LeaseActivity.this,BleActivity.class));
                return;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String str_time = simpleDateFormat.format(date);

            String mess="";
            for (int i = 0; i < epclist.size(); i++) {
                String epc = epclist.get(i).getEpc();
                String data = epclist.get(i).getData2();
                String money = epclist.get(i).getMoney()+"";
                mess += epc+"    |    "+data+"     |   "+money+"\n";
            }
            String message =
                    "*********租赁信息*********\n" +
                            "租赁单号："+leaseResult+"\n" +
                            "租赁人："+leaseBean.getContactName()+"\n" +
                            "租赁卡："+tvTid.getText().toString()+"\n" +
                            "--------------------------\n" +
                            "规格   |   数量   |   押金\n" +
                            mess+
                            "--------------------------\n" +
                            "累计租赁（个）："+map.size()+"\n"+
                            "应付金额（元）："+sum+"\n"+
                            "操作员："+ACache.get(AppApplication.getApplication()).getAsString(LoginActivity.REAL_NAME)+"\n" +
                            "打印时间："+str_time+"\n\n\n";
            try {
                byte[] send=message.getBytes("GBK");
                PrintService.pl.write(send);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            PrintService.pl.printText("\n");
            PrintService.pl.write(new byte[] { 0x1d, 0x0c });
            startActivity(new Intent(LeaseActivity.this,MainActivity.class));
            finish();
    }

    private void createDialog(){
        if (TextUtils.isEmpty(leaseResult)){
            ToastUtil.toast("请先提交再打印！");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提交成功：");
        builder.setMessage("是否打印小票?");
        builder.setIcon(R.mipmap.ic_launcher_round);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        //设置正面按钮
        builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                print();
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("不是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(LeaseActivity.this,MainActivity.class));
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        //显示对话框
        dialog.show();
    }

    @Override
    public void createRentResult(BaseBean<String> baseBean) {
        if (baseBean==null){
            ToastUtil.toast("出库提交失败");
            return;
        }
        if (baseBean.getCode()==0){
            ToastUtil.toast("出库提交成功");

            ACache aCache = ACache.get(AppApplication.getApplication());
            leaseResult = baseBean.getData();
            String name = tvName.getText().toString();
            String num = map.size()+"";

            EPC epc = new EPC();
            epc.setData1(leaseResult);
            epc.setData2(name);
            epc.setData3(num);
            epc.setData4(aCache.getAsString("username"));

            //使用getAsObject()，直接进行强转
            ArrayList<EPC> leaseResultlist = (ArrayList<EPC>) aCache.getAsObject("leaseResult");
            if (leaseResultlist==null){
                leaseResultlist = new ArrayList<>();
            }
            leaseResultlist.add(epc);
            aCache.put("leaseResult",leaseResultlist,ACache.TIME_DAY);

            createDialog();
        }else {
            ToastUtil.toast(baseBean.getMessage());
        }

    }

    @Override
    public void showError(String msg) {
        ToastUtil.toast("操作失败,请退出重新登录");
    }

    private void startstop(){
        m_curInventoryBuffer.nIndexAntenna = 0;
        m_curInventoryBuffer.nCommond = 0;

        m_curInventoryBuffer.bLoopInventoryReal = true;
        m_curInventoryBuffer.btRepeat = (byte) Integer.parseInt("1");

        m_curInventoryBuffer.bLoopInventory = false;
        m_curInventoryBuffer.bLoopCustomizedSession = false;

        if (!btnScan.getText().toString()
                .equals(getResources().getString(R.string.lease_next))) {
            mReaderHelper.setInventoryFlag(false);
            m_curInventoryBuffer.bLoopInventoryReal = false;
            m_curInventoryBuffer.bLoopInventory = false;
            m_curInventoryBuffer.bLoopCustomizedSession = false;

            refreshStartStop(false);
            mLoopHandler.removeCallbacks(mLoopRunnable);
            mHandler.removeCallbacks(mRefreshRunnable);
            refreshList();
            return;
        } else {
            if (m_curInventoryBuffer.lAntenna.size() <= 0) {
                Toast.makeText(this,
                        getResources().getString(R.string.antenna_empty),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            //refreshStartStop(true);
        }

        mReaderHelper.setInventoryFlag(true);
        // end_fixed by lei.li 2016/11/04

        mReaderHelper.clearInventoryTotal();
        byte btWorkAntenna = 0;
        if (m_curInventoryBuffer.lAntenna.size() > 0) {//选择的天线数
            btWorkAntenna = m_curInventoryBuffer.lAntenna
                    .get(m_curInventoryBuffer.nIndexAntenna);
            if (btWorkAntenna < 0)
                btWorkAntenna = 0;
        }

        mReader.setWorkAntenna(m_curReaderSetting.btReadId, btWorkAntenna);
        //mReaderHelper.runLoopInventroy();
        m_curReaderSetting.btWorkAntenna = btWorkAntenna;
        refreshStartStop(true);

        mLoopHandler.removeCallbacks(mLoopRunnable);//移除定时器
        mLoopHandler.postDelayed(mLoopRunnable, 100);//启动定时器
        mHandler.removeCallbacks(mRefreshRunnable);
        mHandler.postDelayed(mRefreshRunnable, 100);
    }

    private void refreshStartStop(boolean start) {
        if (start) {
            refresh();
            btnScan.setText(getResources()
                    .getString(R.string.lease_stop));
        } else {
            btnScan.setText(getResources().getString(
                    R.string.lease_next));
        }
    }

    public void refresh() {
        m_curInventoryBuffer.clearInventoryRealResult();
        refreshList();
    }

    private void refreshList() {
        if (m_curInventoryBuffer.lsTagList.size()<=epcNum){
            return;
        }
        epcNum = m_curInventoryBuffer.lsTagList.size();
        for (int j = 0; j < m_curInventoryBuffer.lsTagList.size(); j++) {
            String baseEpc = m_curInventoryBuffer.lsTagList.get(j).strEPC.replace(" ","");

            if (!map.containsKey(baseEpc)) {
                String type = null;
                String name = null;
                double money1 = 0;
                for (int i = 0; i < baseBean.getData().getFeeRules().size(); i++) {
                    type = baseBean.getData().getFeeRules().get(i).getProductTypeId();
                    if (baseEpc.length()<=type.length()+1){
                        return;
                    }
                    if (type.equals(baseEpc.substring(0,type.length()))){
                        name = baseBean.getData().getFeeRules().get(i).getProductTypeName();
                        money1 = baseBean.getData().getFeeRules().get(i).getDeposit();
                        EPC epc = new EPC();
                        epc.setData1(baseEpc);
                        epc.setMoney(money1);
                        if (!hashMap.containsKey(name)) {
                            ArrayList<EPC> epcs = new ArrayList<>();
                            epcs.add(epc);
                            hashMap.put(name, epcs);
                        } else {
                            hashMap.get(name).add(epc);
                        }
                        map.put(baseEpc, null);
                    }
                }
            }
        }
        epclist.clear();
        Iterator iterator = hashMap.keySet().iterator();
        sum = 0;
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            double money = hashMap.get(key).get(0).getMoney();
            EPC epc1 = new EPC();
            epc1.setEpc(key);
            epc1.setData2(hashMap.get(key).size() + "");
            epc1.setMoney(money);
            epclist.add(epc1);
            sum += money*hashMap.get(key).size();
        }
        tvSum.setText("累计租赁："+map.size()+"个  应付金额："+sum+"元");
        leaseScanadapter.updateDatas(epclist);
    }

    private Handler mHandler = new Handler();
    private Runnable mRefreshRunnable = new Runnable() {
        public void run() {
            refreshList();
            // 循环调用实现界面刷新，延迟一秒发送mRefreshRunnable
            mHandler.postDelayed(mRefreshRunnable, 100);
        }
    };

    private Handler mLoopHandler = new Handler();
    private Runnable mLoopRunnable = new Runnable() {
        public void run() {

            mReaderHelper.runLoopInventroy();
            mLoopHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onResume() {
        if (mReader != null) {
            if (!mReader.IsAlive())
                mReader.StartWait();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (!btnScan.getText().toString()
                .equals(getResources().getString(R.string.lease_next))){
            startstop();
        }
        super.onDestroy();
    }
}
