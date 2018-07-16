package com.example.popularmovies.movie_detail.entity;

import java.util.List;

public class MovieDetailsVideoResult {
    private List<MovieDetailsReviewEntity> results;

    public List<MovieDetailsReviewEntity> getResults() {
        return results;
    }

    public void setResults(List<MovieDetailsReviewEntity> results) {
        this.results = results;
    }
}
