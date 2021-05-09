package com.ghodel.snapsaver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ghodel.snapsaver.R;
import com.ghodel.snapsaver.utils.TouchImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public ImageAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
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

        View itemView = layoutInflater.inflate(R.layout.photo_view_pager, container, false);
        ImageView imgItem = (ImageView) itemView.findViewById(R.id.img_item);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(50f);
        circularProgressDrawable.start();

        Glide.with(context).load(list.get(position).get("path").toString()).placeholder(circularProgressDrawable).into(imgItem);

        ((ViewPager)container).addView(itemView, 0);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View)object);
    }
}
