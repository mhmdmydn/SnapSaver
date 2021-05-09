package com.ghodel.snapsaver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.ghodel.snapsaver.BuildConfig;
import com.ghodel.snapsaver.R;
import com.ghodel.snapsaver.adapter.VideoAdapter;
import com.ghodel.snapsaver.utils.Constants;
import com.ghodel.snapsaver.utils.MainUtil;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class VideoViewerActivity extends BaseActivity {

    private int position;
    private String video;
    private ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    private FloatingActionButton fabDelete, fabSave, fabRepost;
    private File f;
    private VideoAdapter videoAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewer);

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
    }

    @Override
    public void initLogic() {
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        video = getIntent().getStringExtra("img_data");

        list = new Gson().fromJson(video, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());

        f = new File(list.get(position).get("path").toString());
        videoAdapter = new VideoAdapter(VideoViewerActivity.this, list);
        viewPager.setAdapter(videoAdapter);
        viewPager.setCurrentItem(position);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                videoAdapter.releasePlayer();
            }

            @Override
            public void onPageSelected(int position) {
                videoAdapter.initializePlayer(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                videoAdapter.releasePlayer();
            }
        });

    }

    @Override
    public void initListener() {
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File file = new File(list.get(position).get("path").toString());
                if (file.delete()){
                    Toast.makeText(VideoViewerActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(VideoViewerActivity.this, "Gagal dihapus", Toast.LENGTH_SHORT).show();
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
                            BuildConfig.APPLICATION_ID + ".provider", new File(list.get(position).get("path").toString()));
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
                uriForFile = Uri.parse(new StringBuffer().append("file://").append(list.get(position).get("path").toString()).toString());
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


        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainUtil.exportFile(new File(list.get(position).get("path").toString()), new File(Constants.SnapSaverPath));
                    Toast.makeText(VideoViewerActivity.this,MainUtil.exportFile(new File(list.get(position).get("path").toString()), new File(Constants.SnapSaverPath)).getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        videoAdapter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoAdapter.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        videoAdapter.stop();
    }

}