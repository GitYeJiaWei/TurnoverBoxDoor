package com.uhf.uhf.di.component;

import com.uhf.uhf.di.ActivityScope;
import com.uhf.uhf.di.module.ReturnModule;
import com.uhf.uhf.ui.activity.ReturnActivity;
import com.uhf.uhf.ui.fragment.ReturnFragment;

import dagger.Component;

@ActivityScope
@Component(modules = ReturnModule.class,dependencies = AppComponent.class)
public interface ReturnComponent {
    void inject(ReturnActivity returnActivity);
}

