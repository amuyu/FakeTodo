package com.amuyu.todomimic;

import android.app.Application;

import com.amuyu.logger.DefaultLogPrinter;
import com.amuyu.logger.Logger;
import com.amuyu.todomimic.di.components.DaggerMainComponent;
import com.amuyu.todomimic.di.components.MainComponent;
import com.amuyu.todomimic.di.modules.MainModule;


public class TodoApp extends Application {

    private MainComponent mainComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogPrinter(new DefaultLogPrinter(this));
        initializeInjector();
    }

    private void initializeInjector() {
        mainComponent = DaggerMainComponent.builder()
                .mainModule(new MainModule(this)).build();
    }

    public MainComponent getMainComponent() {
        return mainComponent;
    }
}
