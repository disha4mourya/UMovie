package com.example.popularmovies.movie_detail.model;

import android.app.Activity;
import android.util.Log;

import com.example.popularmovies.movie_detail.contract.MovieDetailsContract;
import com.example.popularmovies.movie_detail.entity.MovieDetailsReviewEntity;
import com.example.popularmovies.movie_detail.entity.MovieDetailsReviewResult;
import com.example.popularmovies.movie_detail.entity.MovieDetailsVideoEntity;
import com.example.popularmovies.movie_detail.entity.MovieDetailsVideoResult;
import com.example.popularmovies.network.ApiClient;
import com.example.popularmovies.utils.mvp.LoadCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.popularmovies.utils.Constants.POST_REVIEW;
import static com.example.popularmovies.utils.Constants.POST_VIDEO;
import static com.example.popularmovies.utils.Constants.PRE_VIDEO_REVIEW;

public class MovieDetailsModel implements MovieDetailsContract.Model {

    private Activity activity;
    private List<MovieDetailsReviewEntity> movieDetailsReviewEntityList;
    private List<MovieDetailsVideoEntity> movieDetailsVideoEntityList;

    public MovieDetailsModel(Activity activity) {
        this.activity = activity;
        movieDetailsReviewEntityList = new ArrayList<>();
        movieDetailsVideoEntityList = new ArrayList<>();
    }

    @Override
    public void fetchVideos(String id, final LoadCallback<Response> loadCallback) {
        final Call<MovieDetailsVideoResult> call;

        call = ApiClient.getNetworkService().getVideosList(PRE_VIDEO_REVIEW + id + POST_VIDEO);


        call.enqueue(new Callback<MovieDetailsVideoResult>() {
            @Override
            public void onResponse(Call<MovieDetailsVideoResult> call, Response<MovieDetailsVideoResult> response) {
                loadCallback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<MovieDetailsVideoResult> call, Throwable t) {
                t.printStackTrace();
                loadCallback.onFailure(t);
            }
        });
    }

    @Override
    public void setVideosEntityList(List<MovieDetailsVideoEntity> moviesEntityList) {
        this.movieDetailsVideoEntityList = moviesEntityList;
    }

    @Override
    public List<MovieDetailsVideoEntity> getVideosEntityList() {
        return movieDetailsVideoEntityList;
    }

    @Override
    public void fetchReviews(String id, final LoadCallback<Response> loadCallback) {
        final Call<MovieDetailsReviewResult> call;

        call = ApiClient.getNetworkService().getReviesList(PRE_VIDEO_REVIEW + id + POST_REVIEW);


        call.enqueue(new Callback<MovieDetailsReviewResult>() {
            @Override
            public void onResponse(Call<MovieDetailsReviewResult> call, Response<MovieDetailsReviewResult> response) {
                loadCallback.onSuccess(response);
                Log.d("responseReviesIs",response.body().getResults().toString());
            }

            @Override
            public void onFailure(Call<MovieDetailsReviewResult> call, Throwable t) {
                t.printStackTrace();
                loadCallback.onFailure(t);
            }
        });
    }

    @Override
    public void setReviewsEntityList(List<MovieDetailsReviewEntity> moviesEntityList) {
        this.movieDetailsReviewEntityList = moviesEntityList;
    }

}
