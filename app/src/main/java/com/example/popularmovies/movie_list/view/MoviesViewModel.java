package com.example.popularmovies.movie_list.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.FavoriteEntity;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel{

    private static final String TAG = MoviesViewModel.class.getSimpleName();

    // COMPLETED (2) Add a tasks member variable for a list of TaskEntry objects wrapped in a LiveData
    private LiveData<List<FavoriteEntity>> favoriteEntities;

    public MoviesViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        favoriteEntities = database.favoriteDao().loadAllMovies();
    }

    public LiveData<List<FavoriteEntity>> getFavoriteEntities() {
        return favoriteEntities;
    }
}
