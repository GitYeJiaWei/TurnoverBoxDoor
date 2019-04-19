package com.uhf.uhf.di.component;

import com.uhf.uhf.di.ActivityScope;
import com.uhf.uhf.di.module.CreateReturnModule;
import com.uhf.uhf.ui.activity.ReturnCommitActivity;

import dagger.Component;

@ActivityScope
@Component(modules = CreateReturnModule.class,dependencies = AppComponent.class)
public interface CreateReturnComponent {
    void inject(ReturnCommitActivity returnCommitActivity);
}