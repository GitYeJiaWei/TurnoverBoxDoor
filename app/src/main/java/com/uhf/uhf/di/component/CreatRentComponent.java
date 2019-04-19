package com.uhf.uhf.di.component;

import com.uhf.uhf.di.ActivityScope;
import com.uhf.uhf.di.module.CreatRentModule;
import com.uhf.uhf.ui.activity.LeaseActivity;

import dagger.Component;

@ActivityScope
@Component(modules = CreatRentModule.class,dependencies = AppComponent.class)
public interface CreatRentComponent {
    void inject(LeaseActivity leaseActivity);
}
