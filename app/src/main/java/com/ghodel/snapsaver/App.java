package com.ghodel.snapsaver;

import android.app.Application;
import com.ghodel.snapsaver.utils.CrashHandler;
import com.orhanobut.hawk.Hawk;

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
        Hawk.init(singleton).build();
    }
}
