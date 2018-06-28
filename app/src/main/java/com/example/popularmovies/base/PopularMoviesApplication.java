package com.example.popularmovies.base;

import android.app.Application;
import android.content.Context;

public class PopularMoviesApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }
    public static Context getContext(){
        return context;
    }
}
