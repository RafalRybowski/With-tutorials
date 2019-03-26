package com.epiklp.androidcomicreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epiklp.androidcomicreader.Adapter.MyCommicAdapter;
import com.epiklp.androidcomicreader.Adapter.MyFilterAdapter;
import com.epiklp.androidcomicreader.Common.Common;
import com.epiklp.androidcomicreader.Model.Categories;
import com.epiklp.androidcomicreader.Model.Comic;
import com.epiklp.androidcomicreader.Retrofit.IComicApi;
import com.pd.chocobar.ChocoBar;

import java.util.List;

public class CategoryFilter extends AppCompatActivity {

    Button btn_filter, btn_search;
    IComicApi iComicApi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recycler_filter;

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_filter);

        iComicApi = Common.getAPI();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fetchCategory();
            }
        });

        btn_filter = findViewById(R.id.btn_filter);
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionDialog();
            }
        });

        btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });

        recycler_filter = findViewById(R.id.recycler_filter);
        recycler_filter.setHasFixedSize(true);
        recycler_filter.setLayoutManager(new GridLayoutManager(this, 2));

    }

    private void showSearchDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CategoryFilter.this);
        dialog.setTitle("Search");
        LayoutInflater inflater = this.getLayoutInflater();
        View fielter_layout = inflater.inflate(R.layout.dialog_search, null);

        final EditText editText = fielter_layout.findViewById(R.id.recycler_search);

        dialog.setView(fielter_layout);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fetchSearchComic(editText.getText().toString());
            }
        });

        dialog.show();
    }

    private void fetchSearchComic(String search) {
        compositeDisposable.add(
                iComicApi.getSearchComic(search)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<List<Comic>>() {
                            @Override
                            public void accept(List<Comic> comics) throws Exception {
                                recycler_filter.setVisibility(View.VISIBLE);
                                recycler_filter.setAdapter(new MyCommicAdapter(getBaseContext(), comics));
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                recycler_filter.setVisibility(View.INVISIBLE);
                                ChocoBar.builder().setActivity(CategoryFilter.this).setText("No Comic found").setIcon(R.drawable.ic_bad).setDuration(ChocoBar.LENGTH_SHORT).red().show();
                            }
                        })
        );
    }

    private void showOptionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CategoryFilter.this);
        dialog.setTitle("Categories");

        LayoutInflater inflater = this.getLayoutInflater();
        View fielter_layout = inflater.inflate(R.layout.dialog_options, null);

        RecyclerView recyclerView = fielter_layout.findViewById(R.id.recycler_options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final MyFilterAdapter adapter = new MyFilterAdapter(getBaseContext(), Common.categories);
        recyclerView.setAdapter(adapter);
        dialog.setView(fielter_layout);
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fetchFilterCategory(adapter.getFilterArray());
            }
        });

        dialog.show();
    }

    private void fetchFilterCategory(String filterArray) {
        compositeDisposable.add(
                iComicApi.getFilteredComic(filterArray)
                        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<List<Comic>>() {
                    @Override
                    public void accept(List<Comic> comics) throws Exception {
                        recycler_filter.setVisibility(View.VISIBLE);
                        recycler_filter.setAdapter(new MyCommicAdapter(getBaseContext(), comics));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        recycler_filter.setVisibility(View.INVISIBLE);
                        ChocoBar.builder().setActivity(CategoryFilter.this).setText("No Comic found").setIcon(R.drawable.ic_bad).setDuration(ChocoBar.LENGTH_SHORT).red().show();
                    }
                })
        );
    }

    private void fetchCategory() {

        compositeDisposable.add(iComicApi.getCategoriesList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<List<Categories>>() {
                            @Override
                            public void accept(List<Categories> categories) throws Exception {
                                Common.categories = categories;
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                ChocoBar.builder().setActivity(CategoryFilter.this).setText("Error While Loading categories").setIcon(R.drawable.ic_bad).setDuration(ChocoBar.LENGTH_SHORT).red().show();
                            }
                        }));
    }
}
