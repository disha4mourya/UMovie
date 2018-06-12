package com.example.popularmovies.movie_list.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.example.popularmovies.R;
import com.example.popularmovies.databinding.ActivityMoviesBinding;
import com.example.popularmovies.movie_detail.view.MovieDetailActivity;
import com.example.popularmovies.movie_list.contract.MoviesContract;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.movie_list.presenter.MoviesPresenter;
import com.example.popularmovies.utils.Constants;
import com.example.popularmovies.utils.ItemClickListner;

import java.util.List;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View, ItemClickListner {

    private ActivityMoviesBinding binding;
    private Context context;
    private MoviesPresenter presenter;
    private MoviesAdapter adapter;
    GridLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        context = this;

        presenter = createPresenter();
        adapter = new MoviesAdapter(this);
        mLayoutManager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
        binding.rvMovies.setLayoutManager(mLayoutManager);
        binding.rvMovies.setHasFixedSize(true);

    }

    @Override
    public void showProgress(Boolean show) {
        binding.pbLoading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMovieList(Boolean show) {
        binding.rvMovies.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(Boolean show,Boolean error, String errorMsg) {
        binding.rlError.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.tvError.setVisibility(error ? View.VISIBLE : View.GONE);
        binding.tvEmpty.setVisibility(error ? View.GONE : View.VISIBLE);
        binding.tvError.setText(errorMsg);
    }

    @Override
    public void showMovieDetails(MoviesEntity moviesEntity) {

        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(Constants.VOTE_COUNT, moviesEntity.getVote_count());
        intent.putExtra(Constants.ID, moviesEntity.getId());
        intent.putExtra(Constants.VIDEO, moviesEntity.getVideo());
        intent.putExtra(Constants.VOTE_AVERAGE, moviesEntity.getVote_average());
        intent.putExtra(Constants.TITLE, moviesEntity.getTitle());
        intent.putExtra(Constants.POPULARITY, moviesEntity.getPopularity());
        intent.putExtra(Constants.POSTER_PATH, moviesEntity.getPoster_path());
        intent.putExtra(Constants.ORIGINAL_LANGUAGE, moviesEntity.getOriginal_language());
        intent.putExtra(Constants.BACKDROP_PATH, moviesEntity.getBackdrop_path());
        intent.putExtra(Constants.ADULT, moviesEntity.getAdult());
        intent.putExtra(Constants.OVERVIEW, moviesEntity.getOverview());
        intent.putExtra(Constants.RELEASE_DATE, moviesEntity.getRelease_date());

        startActivity(intent);
    }

    @Override
    public void setDataOnAdapter(List<MoviesEntity> moviesEntities) {

        Log.d("setDataOnAdapter","setting");
        adapter.setData(moviesEntities);
        binding.rvMovies.setAdapter(adapter);
        adapter.setOnClickListener(this);
    }

    @Override
    public void notifyMovieData() {

        adapter.notifyDataSetChanged();
    }

    private MoviesPresenter createPresenter() {
        presenter = new MoviesPresenter(this);
        presenter.getMovies();
        return presenter;
    }

    @Override
    public void onClick(View view, int position) {
        presenter.onMovieClick(position);
    }
}
