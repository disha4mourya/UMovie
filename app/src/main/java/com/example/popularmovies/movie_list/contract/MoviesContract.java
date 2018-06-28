package com.example.popularmovies.movie_list.contract;

import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.utils.mvp.LoadCallback;

import java.util.List;

import retrofit2.Response;

public interface MoviesContract {

    interface View {
        void showProgress(Boolean show);

        void showMovieList(Boolean show);

        void showError(Boolean show, Boolean error,String errorMsg);

        void showMovieDetails(MoviesEntity moviesEntity);

        void setDataOnAdapter(List<MoviesEntity> moviesEntities);

        void notifyMovieData();
    }

    interface Presenter {

        void getMovies(String type);

        void onMovieClick(int position);
    }

    interface Model {

        void fetchMovies(String type,LoadCallback<Response> loadCallback);

        void setMoviesEntityList(List<MoviesEntity> moviesEntityList);

        List<MoviesEntity> getMoviesEntityList();
    }
}
