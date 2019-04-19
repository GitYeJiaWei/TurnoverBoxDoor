package com.uhf.uhf.di.module;

import com.uhf.uhf.data.CreateDamageModel;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.CreateDamageContract;

import dagger.Module;
import dagger.Provides;

@Module
public class CreateDamageModule {
    private CreateDamageContract.CreateDamageView mView;

    public CreateDamageModule(CreateDamageContract.CreateDamageView createRentView){
        this.mView = createRentView;
    }

    @Provides
    public CreateDamageContract.CreateDamageView provideView(){return mView;}

    @Provides
    public CreateDamageContract.ICreateDamageModel privideModel(ApiService apiService){
        return new CreateDamageModel(apiService);
    }
}
