package com.ghodel.snapsaver.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.ghodel.snapsaver.whatsapp_code.Sticker;
import com.ghodel.snapsaver.whatsapp_code.StickerPack;
import com.ghodel.snapsaver.whatsapp_code.adapter.StickerAdapter;
import com.ghodel.snapsaver.whatsapp_code.model.StickerModel;
import com.ghodel.snapsaver.whatsapp_code.task.GetStickers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.ghodel.snapsaver.R;

import java.util.ArrayList;
import java.util.List;

public class StickerMakerActivity extends BaseActivity {
    private Toolbar toolbar;

    public static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
    public static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
    public static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";
    public static final String EXTRA_STICKERPACK = "stickerpack";
    private static final String TAG = MainActivity.class.getSimpleName();
    private final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static String path;
    private ArrayList<String> strings;
    private StickerAdapter adapter;
    private ArrayList<StickerPack> stickerPacks = new ArrayList<>();
    private List<Sticker> mStickers;
    private ArrayList<StickerModel> stickerModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private List<String> mEmojis,mDownloadFiles;
    private String android_play_store_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_maker);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initView();
        initLogic();
        initListener();
    }

    @Override
    public void initView() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public void initLogic() {
        stickerPacks = new ArrayList<>();

        path = Environment.getExternalStorageDirectory() + "/" + "stickers_asset";
        mStickers = new ArrayList<>();
        stickerModels = new ArrayList<>();
        mEmojis = new ArrayList<>();
        mDownloadFiles = new ArrayList<>();
        mEmojis.add("");
        adapter = new StickerAdapter(this, stickerPacks);
        setContentView(R.layout.activity_main);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        new GetStickers(this, this, getResources().getString(R.string.json_link)).execute();
    }

    @Override
    public void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            if (resultCode == Activity.RESULT_OK){

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}