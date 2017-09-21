package com.amuyu.todomimic;

import android.app.Application;

import com.amuyu.logger.DefaultLogPrinter;
import com.amuyu.logger.Logger;

/**
 * Created by amuyu on 2017. 9. 21..
 */

public class TodoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogPrinter(new DefaultLogPrinter(this));
    }
}
