package com.epiklp.androidcomicreader.Retrofit;

import com.epiklp.androidcomicreader.Model.Banner;
import com.epiklp.androidcomicreader.Model.Categories;
import com.epiklp.androidcomicreader.Model.Chapter;
import com.epiklp.androidcomicreader.Model.Comic;
import com.epiklp.androidcomicreader.Model.Link;

import java.util.List;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IComicApi {
    @GET("banner")
    Observable<List<Banner>> getBannerList();

    @GET("comics")
    Observable<List<Comic>> getComicsList();

    @GET("chapter/{comicid}")
    Observable<List<Chapter>> getChapterList(@Path("comicid") int comicid);

    @GET("links/{chapterid}")
    Observable<List<Link>> getImageList(@Path("chapterid") int chapterid);

    @GET("categories")
    Observable<List<Categories>>  getCategoriesList();

    @POST("filter")
    @FormUrlEncoded
    Observable<List<Comic>>  getFilteredComic(@Field("data") String data);

    @POST("search")
    @FormUrlEncoded
    Observable<List<Comic>> getSearchComic(@Field("search") String search);
}
