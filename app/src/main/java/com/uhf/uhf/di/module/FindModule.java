package com.uhf.uhf.di.module;

import com.uhf.uhf.data.FindModel;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.FindContract;

import dagger.Module;
import dagger.Provides;

@Module
public class FindModule {
    private FindContract.FindView mView;

    public FindModule(FindContract.FindView leaseidView){
        this.mView = leaseidView;
    }

    @Provides
    public FindContract.FindView provideView(){return mView;}

    @Provides
    public FindContract.IFindModel privideModel(ApiService apiService){
        return new FindModel(apiService);
    }

}
