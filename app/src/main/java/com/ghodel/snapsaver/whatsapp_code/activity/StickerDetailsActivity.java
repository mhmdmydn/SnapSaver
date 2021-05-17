package com.ghodel.snapsaver.whatsapp_code.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ghodel.snapsaver.BuildConfig;
import com.ghodel.snapsaver.R;
import com.ghodel.snapsaver.activity.StickerMakerActivity;
import com.ghodel.snapsaver.whatsapp_code.Sticker;
import com.ghodel.snapsaver.whatsapp_code.StickerPack;
import com.ghodel.snapsaver.whatsapp_code.adapter.StickerDetailsAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ghodel.snapsaver.activity.StickerMakerActivity.EXTRA_STICKER_PACK_AUTHORITY;
import static com.ghodel.snapsaver.activity.StickerMakerActivity.EXTRA_STICKER_PACK_ID;
import static com.ghodel.snapsaver.activity.StickerMakerActivity.EXTRA_STICKER_PACK_NAME;

public class StickerDetailsActivity extends AppCompatActivity {

    private static final int ADD_PACK = 200;
    private static final String TAG = StickerDetailsActivity.class.getSimpleName();
    StickerPack stickerPack;
    StickerDetailsAdapter adapter;
    RecyclerView recyclerView;
    List<Sticker> stickers;
    ArrayList<String> strings;
    public static String path;
    Button addtowhatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_details);
        stickerPack = getIntent().getParcelableExtra(StickerMakerActivity.EXTRA_STICKERPACK);
        addtowhatsapp = findViewById(R.id.add_to_whatsapp);
        getSupportActionBar().setTitle(stickerPack.name);
        getSupportActionBar().setSubtitle(stickerPack.publisher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);
        stickers = stickerPack.getStickers();
        strings = new ArrayList<>();
        path = Environment.getExternalStorageDirectory() + "/" + "snapsaver/sticker" + "/" + stickerPack.identifier + "/";
        File file = new File(path + stickers.get(0).imageFileName);
        if(!file.exists()) {
            file.mkdir();
        }
        Log.d(TAG, "onCreate: " +path + stickers.get(0).imageFileName);
        for (com.ghodel.snapsaver.whatsapp_code.Sticker s : stickers) {
            if (!file.exists()) {
                strings.add(s.imageFileName);
            } else {
                strings.add(path + s.imageFileName);
            }
        }
        adapter = new StickerDetailsAdapter(strings, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);


        addtowhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
                intent.putExtra(EXTRA_STICKER_PACK_ID, stickerPack.identifier);
                intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
                intent.putExtra(EXTRA_STICKER_PACK_NAME, stickerPack.name);
                try {
                    startActivityForResult(intent, ADD_PACK);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(StickerDetailsActivity.this, "error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
