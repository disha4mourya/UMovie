package com.example.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorite ORDER BY id desc")
    LiveData<List<FavoriteEntity>> loadAllMovies();

    @Insert
    void insertMovie(FavoriteEntity taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavoriteEntity taskEntry);

    @Query("DELETE FROM favorite WHERE id =:id")
        /*void deleteMovie(String id);*/
    void deleteMovie(String id);

    @Query("SELECT * FROM favorite WHERE id = :id")
    FavoriteEntity loadMovieById(String id);
}


