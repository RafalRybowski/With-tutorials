package com.epiklp.androidcomicreader;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epiklp.androidcomicreader.Adapter.MyCommicAdapter;
import com.epiklp.androidcomicreader.Adapter.MySliderAdapter;
import com.epiklp.androidcomicreader.Common.Common;
import com.epiklp.androidcomicreader.Model.Banner;
import com.epiklp.androidcomicreader.Model.Comic;
import com.epiklp.androidcomicreader.Retrofit.IComicApi;
import com.epiklp.androidcomicreader.Service.PicassoImageLoadingService;
import com.pd.chocobar.ChocoBar;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity {

    Slider              slider;
    RecyclerView        recycler_comic;
    TextView            txtView;
    IComicApi           iComicApi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    SwipeRefreshLayout  swipeRefreshLayout;
    ImageView           btn_search;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        iComicApi = Common.getAPI();

        btn_search = findViewById(R.id.img_filter);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CategoryFilter.class));
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Common.isConnectedToInternet(getBaseContext())) {
                    fetchBanner();
                    fetchComic();
                } else {
                    ChocoBar.builder().setActivity(MainActivity.this).setText("Cannot connected to INTERNET").setIcon(R.drawable.ic_bad).setDuration(ChocoBar.LENGTH_SHORT).red().show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(Common.isConnectedToInternet(getBaseContext())) {
                    fetchBanner();
                    fetchComic();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    ChocoBar.builder().setActivity(MainActivity.this).setText("Cannot connected to INTERNET").setIcon(R.drawable.ic_bad).setDuration(ChocoBar.LENGTH_SHORT).red().show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        slider = (Slider) findViewById(R.id.banner_slider);
        Slider.init(new PicassoImageLoadingService());

        recycler_comic = (RecyclerView) findViewById(R.id.recycler_comic);
        recycler_comic.setHasFixedSize(true);
        recycler_comic.setLayoutManager(new GridLayoutManager(this, 2));

        txtView = findViewById(R.id.text_comic);

        fetchBanner();
        fetchComic();

    }

    private void fetchComic() {
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("PLS WAIT....").setCancelable(false).build();
        if(!swipeRefreshLayout.isRefreshing())
            dialog.show();

        compositeDisposable.add(iComicApi.getComicsList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<List<Comic>>() {
                            @Override
                            public void accept(List<Comic> comics) throws Exception {
                                recycler_comic.setAdapter(new MyCommicAdapter(getBaseContext(), comics));
                                txtView.setText(new StringBuilder("NEW COMICS (").append(comics.size()).append(")"));
                                if(!swipeRefreshLayout.isRefreshing())
                                    dialog.dismiss();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if(!swipeRefreshLayout.isRefreshing())
                                    dialog.dismiss();
                                ChocoBar.builder().setActivity(MainActivity.this).setText("Error While Loading Comics").setIcon(R.drawable.ic_bad).setDuration(ChocoBar.LENGTH_SHORT).red().show();
                            }
                        })
        );
    }

    private void fetchBanner(){
        compositeDisposable.add(iComicApi.getBannerList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<List<Banner>>() {
                                      @Override
                                      public void accept(List<Banner> banners) throws Exception {
                                            slider.setAdapter(new MySliderAdapter(banners));
                                      }
                                  }, new Consumer<Throwable>() {
                                      @Override
                                      public void accept(Throwable throwable) throws Exception {
                                          ChocoBar.builder().setActivity(MainActivity.this).setText("Error While Loading Comics").setIcon(R.drawable.ic_bad).setDuration(ChocoBar.LENGTH_SHORT).red().show();
                                      }
                                  }
                        )
        );
    }
    
    
}
