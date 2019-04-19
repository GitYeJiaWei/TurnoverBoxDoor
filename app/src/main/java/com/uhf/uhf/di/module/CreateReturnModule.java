package com.uhf.uhf.di.module;

import com.uhf.uhf.data.CreateReturnModel;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.CreateReturnContract;

import dagger.Module;
import dagger.Provides;

@Module
public class CreateReturnModule {
    private CreateReturnContract.CreateReturnView mView;

    //Module的构造函数，传入一个view，提供给Component
    public CreateReturnModule(CreateReturnContract.CreateReturnView returnView){
        this.mView = returnView;
    }

    //Provides注解代表提供的参数，为构造器传进来的
    @Provides
    public  CreateReturnContract.CreateReturnView provideView(){
        return mView;
    }

    @Provides
    public CreateReturnContract.ICreateReturnModel provideModel(ApiService apiService){
        return new CreateReturnModel(apiService);
    }
}
