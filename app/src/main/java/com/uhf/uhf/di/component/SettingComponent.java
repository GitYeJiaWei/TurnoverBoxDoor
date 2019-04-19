package com.uhf.uhf.di.component;

import com.uhf.uhf.di.ActivityScope;
import com.uhf.uhf.di.module.SettingModule;
import com.uhf.uhf.ui.activity.UserActivity;

import dagger.Component;

@ActivityScope
@Component(modules = SettingModule.class,dependencies = AppComponent.class)
public interface SettingComponent {
    void inject(UserActivity userActivity);
}
