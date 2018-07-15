package com.example.popularmovies.utils;

import static com.example.popularmovies.BuildConfig.MOVIE_API_KEY;

public class Constants {
    public static String IMAGE_APPEND = "http://image.tmdb.org/t/p/w185/";
    public static String BIG_IMAGE_APPEND = "http://image.tmdb.org/t/p/w780/";
    public static String POPULAR = "Popular";
    public static String MOVIES = "Movies";
    public static String MOVIE_DETAILS = "movieDetails";
    public static String TOP_RATED = "Top Rated";
    public static String FAVORITE = "Favorite";
    public static String POPULAR_URL = "http://api.themoviedb.org/3/movie/popular?api_key=" + MOVIE_API_KEY;
    public static String TOP_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + MOVIE_API_KEY;

}
