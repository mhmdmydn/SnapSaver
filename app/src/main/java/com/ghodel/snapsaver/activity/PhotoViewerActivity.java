package com.ghodel.snapsaver.activity;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.ghodel.snapsaver.BuildConfig;
import com.ghodel.snapsaver.R;
import com.ghodel.snapsaver.adapter.ImageAdapter;
import com.ghodel.snapsaver.utils.Constants;
import com.ghodel.snapsaver.utils.MainUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PhotoViewerActivity extends BaseActivity {

    private int Position;
    private String Gambar;
    private ViewPager viewPager;
    private ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    private FloatingActionButton fabDelete, fabSave, fabRepost, fabSetas;
    private File f;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make translucent statusBar on kitkat devices
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_photo_viewer);
        initView();
        initLogic();
        initListener();
    }

    @Override
    public void initView() {
        viewPager = findViewById(R.id.vp_photo);
        fabDelete = findViewById(R.id.fab_delete);
        fabRepost = findViewById(R.id.fab_repost);
        fabSave = findViewById(R.id.fab_save);
        fabSetas = findViewById(R.id.fab_setas);

    }

    @Override
    public void initLogic() {
        Position = Integer.parseInt(getIntent().getStringExtra("position"));
        Gambar = getIntent().getStringExtra("img_data");

        list = new Gson().fromJson(Gambar, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());

//        for delete file
        f = new File(list.get(Position).get("path").toString());

        ImageAdapter imageAdapter = new ImageAdapter(PhotoViewerActivity.this, list);
        viewPager.setAdapter(imageAdapter);
        viewPager.setCurrentItem(Position);

    }

    @Override
    public void initListener() {
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File file = new File(list.get(Position).get("path").toString());
                if (file.delete()){
                    Toast.makeText(PhotoViewerActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(PhotoViewerActivity.this, "Gagal dihapus", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fabRepost.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                Intent intent;
                Parcelable uriForFile;
                if (Build.VERSION.SDK_INT >= 24) {
                    uriForFile = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                            BuildConfig.APPLICATION_ID + ".provider", new File(list.get(Position).get("path").toString()));
                    try {
                        intent = new Intent("android.intent.action.SEND");
                        intent.setType("image/*");
                        intent.setPackage("com.whatsapp");
                        intent.putExtra("android.intent.extra.STREAM", uriForFile);
                        intent.addFlags(Intent.EXTRA_DOCK_STATE_DESK);
                        startActivity(intent);
                        startActivity(intent);
                        return;
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "WhatsApp Not Found on this Phone :(", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                uriForFile = Uri.parse(new StringBuffer().append("file://").append(list.get(Position).get("path").toString()).toString());
                try {
                    intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/*");
                    intent.setPackage("com.whatsapp");
                    intent.putExtra("android.intent.extra.STREAM", uriForFile);
                    startActivity(intent);
                } catch (ActivityNotFoundException e2) {
                    Toast.makeText(getApplicationContext(), "WhatsApp Not Found on this Phone :(", Toast.LENGTH_SHORT).show();
                }

            }
        });

        fabSetas.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                Intent intent;
                Uri uriForFile;
                if(Build.VERSION.SDK_INT >= 24){
                    uriForFile = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                            BuildConfig.APPLICATION_ID + ".provider", new File(list.get(Position).get("path").toString()));
                    intent = new Intent("android.intent.action.ATTACH_DATA");
                    intent.setDataAndType(uriForFile, "image/*");
                    intent.putExtra("mimeType", "image/*");
                    intent.addFlags(1);
                    startActivity(Intent.createChooser(intent, "Set as: "));
                    return;
                }
                uriForFile = Uri.parse(new StringBuffer().append("file://").append(list.get(Position).get("path").toString()).toString());
                intent = new Intent("android.intent.action.ATTACH_DATA");
                intent.setDataAndType(uriForFile, "image/*");
                intent.putExtra("mimeType", "image/*");
                intent.addFlags(Intent.EXTRA_DOCK_STATE_DESK);
                startActivity(Intent.createChooser(intent, "Set as: "));

            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainUtil.exportFile(new File(list.get(Position).get("path").toString()), new File(Constants.SnapSaverPath));
                    Toast.makeText(PhotoViewerActivity.this,MainUtil.exportFile(new File(list.get(Position).get("path").toString()), new File(Constants.SnapSaverPath)).getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}