package com.ghodel.snapsaver.activity;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.ghodel.snapsaver.BuildConfig;
import com.ghodel.snapsaver.R;
import com.ghodel.snapsaver.utils.Constants;
import com.ghodel.snapsaver.utils.MainUtil;
import com.ghodel.snapsaver.utils.VideoWallpaper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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
    private ArrayList<HashMap<String, Object>> list;
    private FloatingActionButton fabDelete, fabSave, fabRepost, fabLiveWallpaper;
    private File f;
    private VideoView videoView;
    private Uri nextUri, previousUri;

    private InterstitialAd mInterstitialAd;

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
        videoView = findViewById(R.id.videoView);
        fabDelete = findViewById(R.id.fab_delete);
        fabRepost = findViewById(R.id.fab_repost);
        fabSave = findViewById(R.id.fab_save);
        fabLiveWallpaper = findViewById(R.id.fab_set_livewallpaper);
    }

    @Override
    public void initLogic() {
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        video = getIntent().getStringExtra("img_data");

        list = new ArrayList<>();
        list = new Gson().fromJson(video, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());

        f = new File(list.get(position).get("path").toString());
        //specify the location of media file
        Uri uri=Uri.parse(f.getAbsolutePath());

        try {
            //Creating MediaController
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
//            mediaController.setPrevNextListeners(onClickListenerNext, onClickListenerPrevious);

            //Setting MediaController and URI, then starting the videoView
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.start();
        } catch (Exception e){
            Toast.makeText(VideoViewerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(!(position < list.size())){
                    return;
                }
                Uri nextUri =
                        Uri.parse(list.get(position++).get("path").toString());
                videoView.setVideoURI(nextUri);
                videoView.start();
            }
        });



        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.interstisial_ads), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("Ads Load", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("Ads Error", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

    }

    private View.OnClickListener onClickListenerNext = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!(position < list.size())){
                return;
            }
            nextUri = Uri.parse(list.get(position++).get("path").toString());
            videoView.setVideoURI(nextUri);
            videoView.start();
        }
    };
    private View.OnClickListener onClickListenerPrevious = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!(position < list.size())){
                return;
            }
            previousUri = Uri.parse(list.get(position--).get("path").toString());
            videoView.setVideoURI(previousUri);
            videoView.start();
        }
    };

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

        fabLiveWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File save = MainUtil.exportFile(new File(list.get(position).get("path").toString()), new File(Constants.SnapSaverPath));

                    VideoWallpaper.setToWallPaper(getApplicationContext(), save.getAbsolutePath());
                    VideoWallpaper.setVoiceSilence(getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd != null) {
            mInterstitialAd.show(VideoViewerActivity.this);
        } else {
            finish();
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

}