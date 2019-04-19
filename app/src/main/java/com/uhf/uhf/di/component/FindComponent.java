package com.uhf.uhf.di.component;

import com.uhf.uhf.di.ActivityScope;
import com.uhf.uhf.di.module.FindModule;
import com.uhf.uhf.ui.activity.FindActivity;

import dagger.Component;

@ActivityScope
@Component(modules = FindModule.class,dependencies = AppComponent.class)
public interface FindComponent {
    void inject(FindActivity findActivity);
}
