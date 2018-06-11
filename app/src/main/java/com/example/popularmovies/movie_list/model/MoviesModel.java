package com.example.popularmovies.movie_list.model;

import com.example.popularmovies.movie_list.contract.MoviesContract;
import com.example.popularmovies.movie_list.entity.MovieResult;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.utils.mvp.LoadCallback;

import java.util.List;

public class MoviesModel implements MoviesContract.Model {
    @Override
    public void fetchMovies(LoadCallback<MovieResult> loadCallback) {

    }

    @Override
    public void setMoviesEntityList(List<MoviesEntity> moviesEntityList) {

    }

    @Override
    public List<MoviesEntity> getMoviesEntityList() {
        return null;
    }
}
