package com.ghodel.snapsaver;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ghodel.snapsaver.utils.CrashHandler;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

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
        MobileAds.initialize(singleton, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull @NotNull InitializationStatus initializationStatus) {
                Log.d("Ads Status : ", initializationStatus.toString());
            }
        });
        CrashHandler.init(singleton);
        Hawk.init(singleton).build();
    }
}
