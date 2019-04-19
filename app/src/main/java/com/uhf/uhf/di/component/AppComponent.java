package com.uhf.uhf.di.component;

import android.app.Application;

import com.google.gson.Gson;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.di.module.AppModule;
import com.uhf.uhf.di.module.HttpModule;

import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;


@Singleton
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent
{

    public Application getApplication();

    public ExecutorService getExecutorService();

    public Gson getGson();

    public ApiService getApiService();

    public OkHttpClient getOkHttpClient();

}
