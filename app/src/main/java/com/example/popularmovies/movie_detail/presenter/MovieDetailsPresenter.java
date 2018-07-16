package com.example.popularmovies.movie_detail.presenter;

import android.app.Activity;
import android.content.Context;

import com.example.popularmovies.movie_detail.contract.MovieDetailsContract;
import com.example.popularmovies.movie_detail.model.MovieDetailsModel;

public class MovieDetailsPresenter implements MovieDetailsContract.Presenter {

    private MovieDetailsContract.View view = null;
    private MovieDetailsContract.Model model;
    private Context context;

    private MovieDetailsPresenter(MovieDetailsContract.View view, MovieDetailsContract.Model model) {
        this.view = view;
        this.model = model;
    }

    public MovieDetailsPresenter(Activity activity, MovieDetailsContract.View view) {
        this(view, new MovieDetailsModel(activity));
        this.context = activity;
    }

    @Override
    public void getVideos() {

    }

    @Override
    public void getReviews() {

    }

    @Override
    public void onThumbnailClick(int position) {

    }
}
