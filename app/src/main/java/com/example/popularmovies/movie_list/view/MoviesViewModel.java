package com.example.popularmovies.movie_list.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.FavoriteEntity;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel{

    private LiveData<List<FavoriteEntity>> favoriteEntities;

    public MoviesViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favoriteEntities = database.favoriteDao().loadAllMovies();
    }

    public LiveData<List<FavoriteEntity>> getFavoriteEntities() {
        return favoriteEntities;
    }
}
