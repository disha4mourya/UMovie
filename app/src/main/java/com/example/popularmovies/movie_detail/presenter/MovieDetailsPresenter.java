package com.example.popularmovies.movie_detail.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.popularmovies.R;
import com.example.popularmovies.base.PopularMoviesApplication;
import com.example.popularmovies.movie_detail.contract.MovieDetailsContract;
import com.example.popularmovies.movie_detail.entity.MovieDetailsReviewResult;
import com.example.popularmovies.movie_detail.entity.MovieDetailsVideoEntity;
import com.example.popularmovies.movie_detail.entity.MovieDetailsVideoResult;
import com.example.popularmovies.movie_detail.model.MovieDetailsModel;
import com.example.popularmovies.network.ServiceManager;
import com.example.popularmovies.utils.ResponseCodes;
import com.example.popularmovies.utils.mvp.LoadCallback;

import retrofit2.Response;

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
        Log.d("consttcall", "one");
    }

    @Override
    public void getVideos(String id) {

        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            view.showVideoProgress(true);
            view.showVideoList(false);
            view.showVideoError(false, false, "", false);
            model.fetchVideos(id, new LoadCallback<Response>() {
                @Override
                public void onSuccess(Response response) {
                    handleResponseCodes(response);
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        } else {
            view.showVideoProgress(false);
            view.showVideoList(false);
            view.showVideoError(true, true, context.getString(R.string.no_internet_connection), false);
        }
    }

    private void handleResponseCodes(Response<MovieDetailsVideoResult> response) {
        switch (response.code()) {

            case ResponseCodes.NOT_FOUND:
                hideListAndProgressAndSetErrorMessage(PopularMoviesApplication.getContext().getString(R.string.not_found));
                break;
            case ResponseCodes.UNAUTHORIZED:
                hideListAndProgressAndSetErrorMessage(PopularMoviesApplication.getContext().getString(R.string.not_authorized));
                break;
            case ResponseCodes.SUCCESS:

                view.showVideoList(true);
                view.showVideoProgress(false);
                view.showVideoError(false, false, "", false);

                model.setVideosEntityList(response.body().getResults());
                view.setThumbnailsOnAdapter(response.body().getResults());
                view.notifyVideoData();

                break;
            default:
                hideListAndProgressAndSetErrorMessage(PopularMoviesApplication.getContext().getString(R.string.something_went_wrong));
                break;
        }
    }

    private void hideListAndProgressAndSetErrorMessage(String msg) {
        view.showVideoList(false);
        view.showVideoProgress(false);
        view.showVideoError(false, false, msg, false);
    }

    @Override
    public void getReviews(String id) {
        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            view.showReviewProgress(true);
            view.showReviewList(false);
            view.showReviewError(false, false, "", false);
            model.fetchReviews(id, new LoadCallback<Response>() {
                @Override
                public void onSuccess(Response response) {
                    handleReviewResponseCodes(response);
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            });
        } else {
            view.showReviewProgress(false);
            view.showReviewList(false);
            view.showReviewError(true, true, context.getString(R.string.no_internet_connection), false);
        }
    }

    private void handleReviewResponseCodes(Response<MovieDetailsReviewResult> response) {
        switch (response.code()) {

            case ResponseCodes.NOT_FOUND:
                hideListAndProgressAndSetErrorReviewMessage(PopularMoviesApplication.getContext().getString(R.string.not_found));
                break;
            case ResponseCodes.UNAUTHORIZED:
                hideListAndProgressAndSetErrorReviewMessage(PopularMoviesApplication.getContext().getString(R.string.not_authorized));
                break;
            case ResponseCodes.SUCCESS:

                view.showReviewList(true);
                view.showReviewProgress(false);
                view.showReviewError(false, false, "", false);

                if (response.body().getResults()!=null&&response.body().getResults().size()>0) {
                    model.setReviewsEntityList(response.body().getResults());
                    view.setReviewOnAdapter(response.body().getResults());
                    view.notifyReviewData();
                }else {
                    view.showReviewError(true, true, context.getString(R.string.no_review_found), true);
                }

                break;
            default:
                hideListAndProgressAndSetErrorReviewMessage(PopularMoviesApplication.getContext().getString(R.string.something_went_wrong));
                break;
        }
    }

    private void hideListAndProgressAndSetErrorReviewMessage(String msg) {
        view.showReviewList(false);
        view.showReviewProgress(false);
        view.showReviewError(false, false, msg, false);
    }

    @Override
    public void onThumbnailClick(int position) {
        MovieDetailsVideoEntity favoriteEntity = model.getVideosEntityList().get(position);
        if (favoriteEntity != null)
            view.showTrailer(favoriteEntity);
    }

    @Override
    public void onShareClick(int position) {
        MovieDetailsVideoEntity favoriteEntity = model.getVideosEntityList().get(position);
        if (favoriteEntity != null)
            view.shareTrailer(favoriteEntity);
    }
}
