package com.uhf.uhf.di.component;

import com.uhf.uhf.di.ActivityScope;
import com.uhf.uhf.di.module.RuleListModule;
import com.uhf.uhf.ui.activity.MainActivity;

import dagger.Component;

@ActivityScope
@Component(modules = RuleListModule.class,dependencies = AppComponent.class)
public interface RuleListComponent {
    void inject(MainActivity mainActivity);
}