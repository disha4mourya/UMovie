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

        void shareTrailer(MovieDetailsVideoEntity moviesEntity);

        void setThumbnailsOnAdapter(List<MovieDetailsVideoEntity> moviesEntities);

        void notifyVideoData();

        void showReviewProgress(Boolean show);

        void showReviewList(Boolean show);

        void showReviewError(Boolean show, Boolean error, String errorMsg, Boolean favorite);

        void setReviewOnAdapter(List<MovieDetailsReviewEntity> moviesEntities);

        void notifyReviewData();

    }

    interface Presenter {
        void getVideos(String id);

        void getReviews(String id);

        void onThumbnailClick(int position);
        void onShareClick(int position);
    }

    interface Model {

        void fetchVideos(String id, LoadCallback<Response> loadCallback);

        void setVideosEntityList(List<MovieDetailsVideoEntity> moviesEntityList);

        List<MovieDetailsVideoEntity> getVideosEntityList();

        void fetchReviews(String id, LoadCallback<Response> loadCallback);

        void setReviewsEntityList(List<MovieDetailsReviewEntity> moviesEntityList);

        List<MovieDetailsReviewEntity> getReviewsEntityList();
    }
}
