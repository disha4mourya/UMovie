package com.example.popularmovies.movie_list.presenter;

import android.content.Context;

import com.example.popularmovies.R;
import com.example.popularmovies.base.PopularMoviesApplication;
import com.example.popularmovies.movie_list.contract.MoviesContract;
import com.example.popularmovies.movie_list.entity.MovieResult;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.movie_list.model.MoviesModel;
import com.example.popularmovies.network.ServiceManager;
import com.example.popularmovies.utils.ResponseCodes;
import com.example.popularmovies.utils.mvp.LoadCallback;

import retrofit2.Response;

public class MoviesPresenter implements MoviesContract.Presenter {

    private MoviesContract.View view = null;
    private MoviesContract.Model model;
    private Context context;

    private MoviesPresenter(MoviesContract.View view, MoviesContract.Model model) {
        this.view = view;
        this.model = model;
    }

    public MoviesPresenter(Context context, MoviesContract.View view) {
        this(view, new MoviesModel());
        this.context = context;
    }

    @Override
    public void getMovies(String type) {

        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            view.showProgress(true);
            view.showMovieList(false);
            view.showError(false, false, "");
            model.fetchMovies(type, new LoadCallback<Response>() {
                @Override
                public void onSuccess(Response response) {
                    handleResponseCodes(response);
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        } else {
            view.showProgress(false);
            view.showMovieList(false);
            view.showError(true, true, context.getString(R.string.no_internet_connection));
        }
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
}
