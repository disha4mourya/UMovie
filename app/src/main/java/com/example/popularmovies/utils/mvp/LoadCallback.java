package com.example.popularmovies.utils.mvp;

public interface LoadCallback<T> {

    void onSuccess(T response);

    void onFailure(Throwable throwable);
}
