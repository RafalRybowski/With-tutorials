package com.epiklp.androidcomicreader;

import android.app.AlertDialog;
import android.os.Bundle;

import com.epiklp.androidcomicreader.Adapter.MyChapterAdapter;
import com.epiklp.androidcomicreader.Common.Common;
import com.epiklp.androidcomicreader.Model.Chapter;
import com.epiklp.androidcomicreader.Model.Comic;
import com.epiklp.androidcomicreader.Retrofit.IComicApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.pd.chocobar.ChocoBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ChapterActivity extends AppCompatActivity {

    RecyclerView        recycler_chapter;
    TextView            txtView;
    IComicApi           iComicApi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        iComicApi =  Common.getAPI();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Common.selected_comic.getName());
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recycler_chapter = findViewById(R.id.recycler_chapter);
        recycler_chapter.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_chapter.setLayoutManager(layoutManager);
        recycler_chapter.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        txtView = findViewById(R.id.text_chapter_number);

        fetchChapter(Common.selected_comic.getID());
    }

    private void fetchChapter(int comicId) {
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("PLS WAIT....").setCancelable(false).build();
        dialog.show();

        compositeDisposable.add(iComicApi.getChapterList(comicId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<List<Chapter>>(){
                            @Override
                            public void accept(List<Chapter> chapters) throws Exception {
                                Common.chapterList = chapters;
                                recycler_chapter.setAdapter(new MyChapterAdapter(getBaseContext(), chapters));
                                txtView.setText(new StringBuilder("CHAPTER (").append(chapters.size()).append(")"));
                                dialog.dismiss();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                dialog.dismiss();
                                ChocoBar.builder().setActivity(ChapterActivity.this).setText("Error While Loading Chapter").setIcon(R.drawable.ic_bad).setDuration(ChocoBar.LENGTH_SHORT).red().show();
                            }
                        })
        );

    }

}
