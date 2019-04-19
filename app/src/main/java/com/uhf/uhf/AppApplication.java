package com.uhf.uhf;

import android.app.Application;
import android.content.Context;
import com.google.gson.Gson;
import com.uhf.uhf.common.AppCaughtException;
import com.uhf.uhf.di.component.AppComponent;
import com.uhf.uhf.di.component.DaggerAppComponent;
import com.uhf.uhf.di.module.AppModule;
import com.uhf.uhf.reader.base.PreferenceUtil;
import com.uhf.uhf.reader.helper.ReaderHelper;
import com.uhf.uhf.tools.Beeper;

import java.util.concurrent.ExecutorService;


public class AppApplication extends Application
{

    private AppComponent mAppComponent;

    private static ExecutorService mThreadPool;

    private static AppApplication mApplication;

    public static AppApplication getApplication()
    {
        return mApplication;
    }

    public AppComponent getAppComponent()
    {
        return mAppComponent;
    }

    public static ExecutorService getExecutorService()
    {
        return mThreadPool;
    }

    public static Gson mGson;

    public static Gson getGson()
    {
        return mGson;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this))
                .build();
        mApplication = (AppApplication) mAppComponent.getApplication();
        mGson = mAppComponent.getGson();
        mThreadPool = mAppComponent.getExecutorService();


        try {
            ReaderHelper.setContext(mApplication);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // add by lei.li support OTG
        //OtgStreamManage.newInstance().init(mContext);
        PreferenceUtil.init(mApplication);
        try {
            Beeper.init(mApplication);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        Thread.setDefaultUncaughtExceptionHandler(new AppCaughtException());// 注册全局异常捕获
    }

}
