package com.example.popularmovies.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String API_URL = "http://api.themoviedb.org/";
    private static Retrofit retrofit = null;
    private static NetworkService networkService = null;

    public static NetworkService getNetworkService() {
        if (networkService == null) {
            networkService = getMovies().create(NetworkService.class);
        }
        return networkService;
    }

    private static Retrofit getMovies() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
