package com.example.popularmovies.movie_list.presenter;

import android.util.Log;

import com.example.popularmovies.R;
import com.example.popularmovies.base.PopularMoviesApplication;
import com.example.popularmovies.movie_list.contract.MoviesContract;
import com.example.popularmovies.movie_list.entity.MovieResult;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.movie_list.model.MoviesModel;
import com.example.popularmovies.utils.ResponseCodes;
import com.example.popularmovies.utils.mvp.LoadCallback;

import retrofit2.Response;

public class MoviesPresenter implements MoviesContract.Presenter, MoviesContract.AdapterPresenter {

    private MoviesContract.View view = null;
    private MoviesContract.Model model;

    private MoviesPresenter(MoviesContract.View view, MoviesContract.Model model) {
        this.view = view;
        this.model = model;
    }

    public MoviesPresenter(MoviesContract.View view) {
        this(view, new MoviesModel());
    }

    @Override
    public void getMovies(String type) {

        view.showProgress(true);
        view.showMovieList(false);
        view.showError(false, false, "");
        model.fetchMovies(type,new LoadCallback<Response>() {
            @Override
            public void onSuccess(Response response) {
                Log.d("onSuccess", "Reached");
                handleResponseCodes(response);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void hideListAndProgressAndSetErrorMessage(String msg) {
        view.showMovieList(false);
        view.showProgress(false);
        view.showError(false, false, msg);
    }

    private void handleResponseCodes(Response<MovieResult> response) {
        switch (response.code()) {

            case ResponseCodes.NOT_FOUND:
                hideListAndProgressAndSetErrorMessage(PopularMoviesApplication.getContext().getString(R.string.not_found));
                break;
            case ResponseCodes.UNAUTHORIZED:
                hideListAndProgressAndSetErrorMessage(PopularMoviesApplication.getContext().getString(R.string.not_authorized));
                break;
            case ResponseCodes.SUCCESS:

                view.showMovieList(true);
                view.showProgress(false);
                view.showError(false, false, "");
                model.setMoviesEntityList(response.body().getResult());
                view.setDataOnAdapter(response.body().getResult());
                view.notifyMovieData();

                break;
            default:
                hideListAndProgressAndSetErrorMessage(PopularMoviesApplication.getContext().getString(R.string.something_went_wrong));
                break;
        }
    }

    @Override
    public void onMovieClick(int position) {
        MoviesEntity songsEntity = model.getMoviesEntityList().get(position);
        if (songsEntity != null) {
            view.showMovieDetails(songsEntity);
        }
    }

    @Override
    public int getAdapterCount() {
        return model.getMoviesEntityList().size();
    }

    @Override
    public MoviesEntity getAdapterEntity(int position) {
        return model.getMoviesEntityList().get(position);
    }
}
