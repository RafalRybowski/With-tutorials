package com.epiklp.androidcomicreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.epiklp.androidcomicreader.Adapter.MyChapterAdapter;
import com.epiklp.androidcomicreader.Adapter.MyViewPagerAdapter;
import com.epiklp.androidcomicreader.Common.Common;
import com.epiklp.androidcomicreader.Model.Chapter;
import com.epiklp.androidcomicreader.Model.Link;
import com.epiklp.androidcomicreader.Retrofit.IComicApi;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.util.List;

public class ViewDetail extends AppCompatActivity {

    IComicApi iComicApi;
    ViewPager myViewPager;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView txt_chapter_name;
    View back, next;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);

        iComicApi = Common.getAPI();
        myViewPager = findViewById(R.id.view_pager);
        txt_chapter_name = findViewById(R.id.txt_chapter_name);
        back = findViewById(R.id.chapter_back);
        next = findViewById(R.id.chapter_right);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.chapter_index == 0){
                    Toast.makeText(ViewDetail.this, "You are reading first chapter", Toast.LENGTH_SHORT).show();
                } else {
                    Common.chapter_index--;
                    fetchLinks(Common.chapterList.get(Common.chapter_index).getID());
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.chapter_index == Common.chapterList.size() - 1){
                    Toast.makeText(ViewDetail.this, "You are reading last chapter", Toast.LENGTH_SHORT).show();
                } else {
                    Common.chapter_index++;
                    fetchLinks(Common.chapterList.get(Common.chapter_index).getID());
                }
            }
        });

        fetchLinks(Common.selected_chapter.getID());
    }

    public void fetchLinks(int id){
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("PLS WAIT....").setCancelable(false).build();
        dialog.show();

        compositeDisposable.add(iComicApi.getImageList(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<List<Link>>(){
                            @Override
                            public void accept(List<Link> links) throws Exception {
                                myViewPager.setAdapter(new MyViewPagerAdapter(getBaseContext(), links));

                                txt_chapter_name.setText(Common.formatString(Common.selected_chapter.getName()));
                                dialog.dismiss();

                                BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();
                                bookFlipPageTransformer.setScaleAmountPercent(10f);

                                myViewPager.setPageTransformer(true, bookFlipPageTransformer);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                dialog.dismiss();
                                Toast.makeText(ViewDetail.this, "Error While Loading Image", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }
}
