package com.example.popularmovies.movie_list.contract;

import com.example.popularmovies.movie_list.entity.MovieResult;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.utils.mvp.LoadCallback;

import java.util.List;

public interface MoviesContract {

    interface View {
        void showProgress(Boolean show);

        void showMovieList(Boolean show);

        void showError(Boolean show, Boolean error);

        void showMovieDetails(MoviesEntity moviesEntity);

        void notifyMovieData();
    }

    interface Presenter {

        void getMovies();

        void setView(MoviesContract.View view);

        void onMovieClick(int position);

    }

    interface AdapterPresenter {

        int getAdapterCount();

        MoviesEntity getAdapterEntity(int position);

    }

    interface Model {

        void fetchMovies(LoadCallback<MovieResult> loadCallback);

        void setMoviesEntityList(List<MoviesEntity> moviesEntityList);

        List<MoviesEntity> getMoviesEntityList();


    }


}
