package com.uhf.uhf.di.module;

import com.uhf.uhf.data.CreatRentModel;
import com.uhf.uhf.data.http.ApiService;
import com.uhf.uhf.presenter.contract.CreateRentContract;

import dagger.Module;
import dagger.Provides;

@Module
public class CreatRentModule {
    private CreateRentContract.CreateRentView mView;

    public CreatRentModule(CreateRentContract.CreateRentView createRentView){
        this.mView = createRentView;
    }

    @Provides
    public CreateRentContract.CreateRentView provideView(){return mView;}

    @Provides
    public CreateRentContract.ICreateRentModel privideModel(ApiService apiService){
        return new CreatRentModel(apiService);
    }

}
