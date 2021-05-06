package com.ghodel.snapsaver;

import android.app.Application;
import android.view.Window;
import android.view.WindowManager;

import com.ghodel.snapsaver.utils.CrashHandler;

public class App extends Application {

    private static App singleton = null;

    public static App getInstance(){
        if(singleton == null){
            singleton = new App();
        }

        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        CrashHandler.init(singleton);
    }
}
