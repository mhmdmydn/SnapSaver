package com.ghodel.snapsaver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ghodel.snapsaver.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> list;
    private LayoutInflater layoutInflater;
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private int pos;

    public VideoAdapter(Context context, ArrayList<HashMap<String, Object>> list){
        this.context = context;
        this.list = list;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View itemView = layoutInflater.inflate(R.layout.video_view_pager, container, false);
        pos = position;
        playerView = (PlayerView) itemView.findViewById(R.id.video_view);
        initializePlayer(position);

        ((ViewPager)container).addView(itemView, 0);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public void initializePlayer(int position) {
        player = new SimpleExoPlayer.Builder(context).build();
        playerView.setPlayer(player);
        MediaItem currentItem = MediaItem.fromUri(list.get(position).get("path").toString());
        player.setMediaItem(currentItem);
    }

    public void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    public void start(){
        if (Util.SDK_INT >= 24) {
            initializePlayer(pos);
        }
    }

    public void stop(){
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    public void resume(){
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer(pos);
        }
    }

    public void pause(){
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

}
