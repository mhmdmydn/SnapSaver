package com.ghodel.snapsaver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.ghodel.snapsaver.R;
import com.ghodel.snapsaver.activity.PhotoViewerActivity;
import com.ghodel.snapsaver.activity.VideoViewerActivity;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class SnapSaverAdapter extends RecyclerView.Adapter<SnapSaverAdapter.SnapSaverViewHolder> {

    private ArrayList<File> list;
    private Context context;

    public SnapSaverAdapter(ArrayList<File> list, Context context){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SnapSaverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new SnapSaverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SnapSaverViewHolder holder, int position) {
        final File file = list.get(position);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(50f);
        circularProgressDrawable.start();

        Glide.with(context).load(file.getAbsoluteFile()).placeholder(circularProgressDrawable).into(holder.imgItem);

        holder.bgCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Berpindah

                Intent goViewer = new Intent(Intent.ACTION_VIEW);

                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".jpeg")) {
                    goViewer.setClass(context, PhotoViewerActivity.class);
                } else {
                    goViewer.setClass(context, VideoViewerActivity.class);
                }

                goViewer.putExtra("position", String.valueOf(position));
                goViewer.putExtra("img_data", new Gson().toJson(list));
                context.startActivity(goViewer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SnapSaverViewHolder extends RecyclerView.ViewHolder{

        ImageView imgItem;
        CardView bgCard;

        public SnapSaverViewHolder(@NonNull View itemView) {
            super(itemView);
            bgCard = itemView.findViewById(R.id.bg_card);
            imgItem = itemView.findViewById(R.id.img_item);
        }
    }


}
