package com.epiklp.androidcomicreader.Adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epiklp.androidcomicreader.ChapterActivity;
import com.epiklp.androidcomicreader.Common.Common;
import com.epiklp.androidcomicreader.Interface.IRecycleOnClick;
import com.epiklp.androidcomicreader.Model.Comic;
import com.epiklp.androidcomicreader.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyCommicAdapter extends RecyclerView.Adapter<MyCommicAdapter.MyViewHolder> {


    private Context context;
    private List<Comic> comicList;


    public MyCommicAdapter(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.comic_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(comicList.get(position).getImage()).into(holder.imageView);
        holder.textView.setText(comicList.get(position).getName());

        holder.setIRecycleOnClick(new IRecycleOnClick() {
            @Override
            public void onClick(View view, int position) {
                context.startActivity(new Intent(context, ChapterActivity.class));
                Common.selected_comic = comicList.get(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView;
        IRecycleOnClick iRecycleOnClick;

        public void setIRecycleOnClick(IRecycleOnClick iRecycleOnClick) {
            this.iRecycleOnClick = iRecycleOnClick;
        }

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.manga_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecycleOnClick.onClick(view, getAdapterPosition());
        }
    }
}
