package com.epiklp.androidcomicreader.Adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.epiklp.androidcomicreader.Model.Categories;
import com.epiklp.androidcomicreader.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyFilterAdapter extends RecyclerView.Adapter<MyFilterAdapter.MyViewHolder> {

    Context context;
    List<Categories> categoriesList;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    List<Categories> selected_categories = new ArrayList<>();

    public MyFilterAdapter(Context context, List<Categories> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.check_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.checkBox.setText(categoriesList.get(position).getName());
        holder.checkBox.setChecked(itemStateArray.get(position));
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public String getFilterArray() {
        List<Integer> id_selected = new ArrayList<>();
        for(Categories categories:selected_categories)
            id_selected.add(categories.getID());
        return new Gson().toJson(id_selected);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.check_option);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int adapter = getAdapterPosition();
                    itemStateArray.put(adapter, isChecked);
                    if(isChecked){
                        selected_categories.add(categoriesList.get(adapter));
                    } else {
                        selected_categories.remove(categoriesList.get(adapter));
                    }
                }
            });

        }
    }
}
