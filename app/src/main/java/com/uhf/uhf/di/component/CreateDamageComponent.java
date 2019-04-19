package com.uhf.uhf.di.component;

import com.uhf.uhf.di.ActivityScope;
import com.uhf.uhf.di.module.CreateDamageModule;
import com.uhf.uhf.ui.activity.PickActivity;

import dagger.Component;

@ActivityScope
@Component(modules = CreateDamageModule.class,dependencies = AppComponent.class)
public interface CreateDamageComponent {
    void inject(PickActivity pickActivity);
}