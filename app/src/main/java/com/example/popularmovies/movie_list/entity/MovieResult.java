package com.example.popularmovies.movie_list.entity;

import java.util.List;

public class MovieResult {

    private List<MoviesEntity> results;

    public List<MoviesEntity> getResult() {
        return results;
    }

    public void setResult(List<MoviesEntity> result) {
        this.results = result;
    }
}
