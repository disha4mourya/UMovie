package com.example.popularmovies.movie_list.model;

import android.app.Activity;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.FavoriteEntity;
import com.example.popularmovies.movie_list.contract.MoviesContract;
import com.example.popularmovies.movie_list.entity.MovieResult;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.network.ApiClient;
import com.example.popularmovies.utils.mvp.LoadCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.popularmovies.utils.Constants.POPULAR;
import static com.example.popularmovies.utils.Constants.POPULAR_URL;
import static com.example.popularmovies.utils.Constants.TOP_RATED;
import static com.example.popularmovies.utils.Constants.TOP_RATED_URL;

public class MoviesModel implements MoviesContract.Model {

    private List<MoviesEntity> moviesEntityList;
    private List<FavoriteEntity> favoriteEntityList;
    private AppDatabase mDb;
    private Activity activity;

    public MoviesModel(Activity activity) {
        this.activity = activity;
        moviesEntityList = new ArrayList<>();
        mDb = AppDatabase.getInstance(activity);
    }

    @Override
    public void fetchMovies(String type, final LoadCallback<Response> loadCallback) {

        final Call<MovieResult> call;
        if (type.equals(POPULAR)) {
            call = ApiClient.getNetworkService().getMoviesList(POPULAR_URL);
        } else if (type.equals(TOP_RATED)) {
            call = ApiClient.getNetworkService().getMoviesList(TOP_RATED_URL);
        } else {
            call = ApiClient.getNetworkService().getMoviesList(POPULAR_URL);
        }

        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                loadCallback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                t.printStackTrace();
                loadCallback.onFailure(t);
            }
        });

    }

    @Override
    public void setMoviesEntityList(List<MoviesEntity> moviesEntityList) {
        this.moviesEntityList = moviesEntityList;
    }

    @Override
    public List<MoviesEntity> getMoviesEntityList() {
        return moviesEntityList;
    }

}
