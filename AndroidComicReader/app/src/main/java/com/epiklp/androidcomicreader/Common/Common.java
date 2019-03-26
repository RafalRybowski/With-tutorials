package com.epiklp.androidcomicreader.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.epiklp.androidcomicreader.Model.Categories;
import com.epiklp.androidcomicreader.Model.Chapter;
import com.epiklp.androidcomicreader.Model.Comic;
import com.epiklp.androidcomicreader.Retrofit.IComicApi;
import com.epiklp.androidcomicreader.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;


public class Common {

    public static Comic selected_comic;
    public static Chapter selected_chapter;
    public static int chapter_index = -1;
    public static List<Chapter> chapterList = new ArrayList<>();
    public static List<Categories> categories = new ArrayList<>();

    public static IComicApi getAPI(){
        return RetrofitClient.getInstance().create(IComicApi.class);
    }

    public static String formatString(String name) {
        return (name.length() > 15 ? name.substring(0, 15) + "..." : name);
    }

    public static boolean isConnectedToInternet(Context baseContext) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) baseContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
