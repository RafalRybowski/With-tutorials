package com.epiklp.androidcomicreader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epiklp.androidcomicreader.Model.Link;
import com.epiklp.androidcomicreader.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class MyViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Link> linkList;
    LayoutInflater inflater;

    public MyViewPagerAdapter(Context context, List<Link> linkList) {
        this.context = context;
        this.linkList = linkList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return linkList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View image_layout = inflater.inflate(R.layout.view_pager_item, container, false);
        PhotoView page_image = image_layout.findViewById(R.id.page_image);
        Picasso.get().load(linkList.get(position).getLink()).into(page_image);

        container.addView(image_layout);
        return image_layout;
    }
}
