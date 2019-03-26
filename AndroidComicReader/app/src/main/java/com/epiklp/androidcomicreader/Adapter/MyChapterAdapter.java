package com.epiklp.androidcomicreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epiklp.androidcomicreader.ChapterActivity;
import com.epiklp.androidcomicreader.Common.Common;
import com.epiklp.androidcomicreader.Interface.IRecycleOnClick;
import com.epiklp.androidcomicreader.Model.Chapter;
import com.epiklp.androidcomicreader.R;
import com.epiklp.androidcomicreader.ViewDetail;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyChapterAdapter extends RecyclerView.Adapter<MyChapterAdapter.MyViewHolder> {

    private Context context;
    private List<Chapter> chapterList;

    public MyChapterAdapter(Context context, List<Chapter> chapterList) {
        this.context = context;
        this.chapterList = chapterList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.chapter_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text_chapter.setText(new StringBuilder("CHAPTER (").append(chapterList.get(position).getName()).append(")"));
        holder.setIRecycleOnClick(new IRecycleOnClick() {
            @Override
            public void onClick(View view, int position) {
                Common.selected_chapter = chapterList.get(position);
                Common.chapter_index = position;

                context.startActivity(new Intent(context, ViewDetail.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView text_chapter;
        IRecycleOnClick iRecycleOnClick;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_chapter = itemView.findViewById(R.id.text_chapter);
            itemView.setOnClickListener(this);
        }

        public void setIRecycleOnClick(IRecycleOnClick iRecycleOnClick) {
            this.iRecycleOnClick = iRecycleOnClick;
        }

        @Override
        public void onClick(View view) {
            iRecycleOnClick.onClick(view, getAdapterPosition());
        }
    }
}
