package com.ghodel.snapsaver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ghodel.snapsaver.R;

public class Splashscreen extends BaseActivity {

    private static final int LOADING = 3500;
    private ImageView imgLogo;
    private ProgressBar progressSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        Untuk Menghilangkan Status Bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);


        initView();
        initLogic();
        initListener();
    }

    @Override
    public void initView() {
        imgLogo = findViewById(R.id.img_logo);
        progressSplash = findViewById(R.id.progress_splash);
    }

    @Override
    public void initLogic() {


//      Loading pindah activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent();
                in.setAction(Intent.ACTION_VIEW);
                in.setClass(Splashscreen.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        }, LOADING);

    }

    @Override
    public void initListener() {

    }
}