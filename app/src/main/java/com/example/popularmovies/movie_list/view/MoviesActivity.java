package com.example.popularmovies.movie_list.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.popularmovies.R;
import com.example.popularmovies.movie_list.contract.MoviesContract;
import com.example.popularmovies.movie_list.entity.MoviesEntity;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
    }

    @Override
    public void showProgress(Boolean show) {

    }

    @Override
    public void showMovieList(Boolean show) {

    }

    @Override
    public void showError(Boolean show, Boolean error) {

    }

    @Override
    public void showMovieDetails(MoviesEntity moviesEntity) {

    }

    @Override
    public void notifyMovieData() {

    }
}
