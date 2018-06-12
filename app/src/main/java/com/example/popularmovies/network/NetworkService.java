package com.example.popularmovies.network;

import com.example.popularmovies.movie_list.entity.MovieResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NetworkService {

    @GET()
    Call<MovieResult> getMoviesList(@Url String url);
}
