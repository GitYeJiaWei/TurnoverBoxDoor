package com.uhf.uhf.di.component;

import com.uhf.uhf.di.ActivityScope;
import com.uhf.uhf.di.module.AppModule;
import com.uhf.uhf.di.module.LeaseidModule;
import com.uhf.uhf.ui.activity.PickActivity;
import com.uhf.uhf.ui.fragment.LeaseFragment;
import com.uhf.uhf.ui.fragment.ReturnFragment;

import dagger.Component;

@ActivityScope
@Component(modules = LeaseidModule.class,dependencies = AppComponent.class)
public interface LeaseComponent {
    void inject(LeaseFragment leaseFragment);
    void inject(ReturnFragment returnFragment);
}
