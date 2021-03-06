package com.uhf.uhf.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uhf.uhf.AppApplication;
import com.uhf.uhf.R;
import com.uhf.uhf.bean.BaseBean;
import com.uhf.uhf.bean.EPC;
import com.uhf.uhf.bean.FeeRule;
import com.uhf.uhf.bean.LeaseBean;
import com.uhf.uhf.bean.ReturnBean;
import com.uhf.uhf.common.util.ACache;
import com.uhf.uhf.common.util.SoundManage;
import com.uhf.uhf.common.util.ToastUtil;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.di.component.AppComponent;
import com.uhf.uhf.di.component.DaggerReturnComponent;
import com.uhf.uhf.di.module.ReturnModule;
import com.uhf.uhf.presenter.ReturnPresenter;
import com.uhf.uhf.presenter.contract.ReturnContract;
import com.uhf.uhf.reader.base.ReaderBase;
import com.uhf.uhf.reader.helper.InventoryBuffer;
import com.uhf.uhf.reader.helper.ReaderHelper;
import com.uhf.uhf.reader.helper.ReaderSetting;
import com.uhf.uhf.ui.adapter.LeaseScanadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReturnActivity extends BaseActivity<ReturnPresenter> implements ReturnContract.ReturnView {

    @BindView(R.id.tv_tid)
    TextView tvTid;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.list_lease)
    ListView listLease;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.lin_lease)
    LinearLayout linLease;
    @BindView(R.id.et_back)
    EditText etBack;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.btn_clear)
    Button btnClear;
    private ArrayList<EPC> epclist = new ArrayList<>();
    private ConcurrentHashMap<String, List<EPC>> hashMap = new ConcurrentHashMap<>();
    private HashMap<String, String> map = new HashMap<>();
    LeaseScanadapter leaseScanadapter = null;
    private ArrayList<String> arrayList = new ArrayList<>();
    private BaseBean<FeeRule> baseBean = null;
    private ReaderBase mReader;
    private ReaderHelper mReaderHelper;
    private static InventoryBuffer m_curInventoryBuffer;
    private static ReaderSetting m_curReaderSetting;
    private int epcNum = 0;
    String cardCode;
    String Tid;

    @Override
    public int setLayout() {
        return R.layout.activity_return;
    }

    @Override
    public void setupAcitivtyComponent(AppComponent appComponent) {
        DaggerReturnComponent.builder().appComponent(appComponent).returnModule(new ReturnModule(this))
                .build().inject(this);
    }

    @Override
    public void init() {
        etBack.setInputType(InputType.TYPE_NULL);

        baseBean = (BaseBean<FeeRule>) ACache.get(AppApplication.getApplication()).getAsObject("feeRule");
        if (baseBean == null) {
            finish();
        }

        setTitle("扫描入库");
        linLease.setVisibility(View.GONE);
        hashMap.clear();
        map.clear();
        leaseScanadapter = new LeaseScanadapter(this, "return");
        listLease.setAdapter(leaseScanadapter);

        try {
            mReaderHelper = ReaderHelper.getDefaultHelper();
            mReader = mReaderHelper.getReader();
        } catch (Exception e) {
            return;
        }

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

    @Override
    public void returnResult(BaseBean<List<ReturnBean>> baseBean) {
        if (baseBean == null) {
            ToastUtil.toast("获取费用小计失败");
            return;
        }
        if (baseBean.getCode() == 0 && baseBean.getData().size() > 0) {
            String TID = tvTid.getText().toString();
            String Name = tvName.getText().toString();
            Intent intent = new Intent(ReturnActivity.this, ReturnCommitActivity.class);
            intent.putExtra("Return", baseBean);
            intent.putExtra("TID", TID);
            intent.putExtra("Name", Name);
            intent.putExtra("ID", Tid);
            intent.putExtra("listEpcJson", AppApplication.getGson().toJson(arrayList));
            startActivity(intent);
        } else {
            ToastUtil.toast(baseBean.getMessage());
        }
    }

    @Override
    public void showError(String msg) {
        ToastUtil.toast("操作失败,请退出重新登录");
    }

    @OnClick({R.id.btn_scan, R.id.btn_commit,R.id.btn_sure,R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                linLease.setVisibility(View.VISIBLE);
                startstop();
                break;
            case R.id.btn_commit:
                if (!btnScan.getText().toString()
                        .equals(getResources().getString(R.string.lease_next))) {
                    startstop();
                }
                String name = tvName.getText().toString();
                String id = tvTid.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(id)){
                    ToastUtil.toast("请扫描退还卡再提交");
                    return;
                }
                Iterator it = hashMap.keySet().iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    for (int i = 0; i < hashMap.get(key).size(); i++) {
                        arrayList.add(hashMap.get(key).get(i).getData1());
                    }
                }
                mPresenter.Return(AppApplication.getGson().toJson(arrayList)); //获取超时数量
                break;
            case R.id.btn_sure:
                if (!btnScan.getText().toString()
                        .equals(getResources().getString(R.string.lease_next))) {
                    startstop();
                }
                readCard();
                break;
            case R.id.btn_clear:
                if (!btnScan.getText().toString()
                        .equals(getResources().getString(R.string.lease_next))) {
                    startstop();
                }
                etBack.setText("");
                break;
        }
    }

    private void readCard() {
        String entity = etBack.getText().toString().trim();
        if (!TextUtils.isEmpty(entity)) {
            SoundManage.PlaySound(AppApplication.getApplication(), SoundManage.SoundType.SUCCESS);
            cardCode = entity;

            Map<String, String> map = new HashMap<>();
            map.put("cardCode", cardCode);
            map.put("cardType", "2");
            Retrofit retrofit = new Retrofit.Builder()
                    //设置基础的URL
                    .baseUrl(ApiService.BASE_URL)
                    //设置内容格式,这种对应的数据返回值是Gson类型，需要导包
                    .addConverterFactory(GsonConverterFactory.create())
                    //设置支持RxJava，应用observable观察者，需要导包
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(new OkHttpClient())
                    .build();

            ApiService apIservice = retrofit.create(ApiService.class);
            Observable<BaseBean<LeaseBean>> qqDataCall = apIservice.leaseid(map);
            qqDataCall.subscribeOn(Schedulers.io())//请求数据的事件发生在io线程
                    .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程更新UI
                    .subscribe(new Observer<BaseBean<LeaseBean>>() {
                                   @Override
                                   public void onSubscribe(Disposable d) {

                                   }

                                   @Override
                                   public void onNext(BaseBean<LeaseBean> baseBean) {
                                       if (baseBean == null) {
                                           ToastUtil.toast("扫描退还卡失败");
                                           return;
                                       }
                                       if (baseBean.getCode() == 0) {
                                           ToastUtil.toast("扫描退还卡成功");
                                           tvName.setText(baseBean.getData().getContactName());
                                           tvTid.setText(cardCode);
                                           Tid = baseBean.getData().getId();
                                       } else {
                                           ToastUtil.toast(baseBean.getMessage());
                                       }
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       ToastUtil.toast("操作失败,请退出重新登录");
                                   }

                                   @Override
                                   public void onComplete() {
                                   }//订阅
                               }

                    );
        } else {
            SoundManage.PlaySound(this, SoundManage.SoundType.FAILURE);
            ToastUtil.toast("退还卡扫描失败,请将感应模块贴近卡片重新扫描");
        }
    }

    private void startstop() {
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

        mLoopHandler.removeCallbacks(mLoopRunnable);
        mLoopHandler.postDelayed(mLoopRunnable, 100);
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
        if (m_curInventoryBuffer.lsTagList.size() <= epcNum) {
            return;
        }
        epcNum = m_curInventoryBuffer.lsTagList.size();
        for (int j = 0; j < m_curInventoryBuffer.lsTagList.size(); j++) {
            String baseEpc = m_curInventoryBuffer.lsTagList.get(j).strEPC.replace(" ", "");

            if (!map.containsKey(baseEpc)) {
                String type = null;
                String name = null;
                for (int i = 0; i < baseBean.getData().getFeeRules().size(); i++) {
                    type = baseBean.getData().getFeeRules().get(i).getProductTypeId();
                    name = baseBean.getData().getFeeRules().get(i).getProductTypeName();
                    if (baseEpc.length() <= type.length() + 1) {
                        return;
                    }
                    if (type.equals(baseEpc.substring(0, type.length()))) {
                        EPC epc = new EPC();
                        epc.setData1(baseEpc);
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
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            EPC epc1 = new EPC();
            epc1.setEpc(key);
            epc1.setData2(hashMap.get(key).size() + "");
            epclist.add(epc1);
        }
        leaseScanadapter.updateDatas(epclist);
    }

    private Handler mHandler = new Handler();
    private Runnable mRefreshRunnable = new Runnable() {
        public void run() {
            refreshList();
            mHandler.postDelayed(this, 100);
        }
    };
    private Handler mLoopHandler = new Handler();
    private Runnable mLoopRunnable = new Runnable() {
        public void run() {
            /*
             * byte btWorkAntenna =
             * m_curInventoryBuffer.lAntenna.get(m_curInventoryBuffer
             * .nIndexAntenna); if (btWorkAntenna < 0) btWorkAntenna = 0;
             * mReader.setWorkAntenna(m_curReaderSetting.btReadId,
             * btWorkAntenna);
             */
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
                .equals(getResources().getString(R.string.lease_next))) {
            startstop();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
