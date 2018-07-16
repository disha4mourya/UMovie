package com.example.popularmovies.movie_detail.model;

import com.example.popularmovies.movie_detail.contract.MovieDetailsContract;
import com.example.popularmovies.movie_detail.entity.MovieDetailsReviewEntity;
import com.example.popularmovies.movie_detail.entity.MovieDetailsVideoEntity;
import com.example.popularmovies.utils.mvp.LoadCallback;

import java.util.List;

import retrofit2.Response;

public class MovieDetailsModel implements MovieDetailsContract.Model{

    @Override
    public void fetchVideos(String type, LoadCallback<Response> loadCallback) {

    }

    @Override
    public void setVideosEntityList(List<MovieDetailsVideoEntity> moviesEntityList) {

    }

    @Override
    public List<MovieDetailsVideoEntity> getVideosEntityList() {
        return null;
    }

    @Override
    public void fetchReviews(String type, LoadCallback<Response> loadCallback) {

    }

    @Override
    public void setReviewsEntityList(List<MovieDetailsReviewEntity> moviesEntityList) {

    }

    @Override
    public List<MovieDetailsReviewEntity> getReviewsEntityList() {
        return null;
    }
}
