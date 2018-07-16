package com.example.popularmovies.movie_detail.contract;

import com.example.popularmovies.movie_detail.entity.MovieDetailsReviewEntity;
import com.example.popularmovies.movie_detail.entity.MovieDetailsVideoEntity;
import com.example.popularmovies.utils.mvp.LoadCallback;

import java.util.List;

import retrofit2.Response;

public interface MovieDetailsContract {

    interface View {
        void showVideoProgress(Boolean show);

        void showVideoList(Boolean show);

        void showVideoError(Boolean show, Boolean error, String errorMsg, Boolean favorite);

        void showTrailer(MovieDetailsVideoEntity moviesEntity);

        void setThumbnailsOnAdapter(List<MovieDetailsVideoEntity> moviesEntities);

        void showReviewProgress(Boolean show);

        void showReviewList(Boolean show);

        void showReviewError(Boolean show, Boolean error, String errorMsg, Boolean favorite);

        void setReviewOnAdapter(List<MovieDetailsReviewEntity> moviesEntities);

    }

    interface Presenter {
        void getVideos();

        void getReviews();

        void onThumbnailClick(int position);
    }

    interface Model {

        void fetchVideos(String type, LoadCallback<Response> loadCallback);

        void setVideosEntityList(List<MovieDetailsVideoEntity> moviesEntityList);

        List<MovieDetailsVideoEntity> getVideosEntityList();

        void fetchReviews(String type, LoadCallback<Response> loadCallback);

        void setReviewsEntityList(List<MovieDetailsReviewEntity> moviesEntityList);

        List<MovieDetailsReviewEntity> getReviewsEntityList();
    }
}
