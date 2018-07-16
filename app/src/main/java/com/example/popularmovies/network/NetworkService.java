package com.example.popularmovies.network;

import com.example.popularmovies.movie_detail.entity.MovieDetailsReviewResult;
import com.example.popularmovies.movie_detail.entity.MovieDetailsVideoResult;
import com.example.popularmovies.movie_list.entity.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NetworkService {
    @GET()
    Call<MovieResult> getMoviesList(@Url String url);
    @GET()
    Call<MovieDetailsVideoResult> getVideosList(@Url String url);
    @GET()
    Call<MovieDetailsReviewResult> getReviesList(@Url String url);
}
