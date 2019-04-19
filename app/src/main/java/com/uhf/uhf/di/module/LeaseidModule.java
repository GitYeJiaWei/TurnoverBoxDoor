package com.uhf.uhf.di.module;

import com.uhf.uhf.data.LeaseidModel;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.LeaseidContract;

import dagger.Module;
import dagger.Provides;

@Module
public class LeaseidModule {
    private LeaseidContract.LeaseidView mView;

    public LeaseidModule(LeaseidContract.LeaseidView leaseidView){
        this.mView = leaseidView;
    }

    @Provides
    public LeaseidContract.LeaseidView provideView(){return mView;}

    @Provides
    public LeaseidContract.ILeaseidModel privideModel(ApiService apiService){
        return new LeaseidModel(apiService);
    }
}
