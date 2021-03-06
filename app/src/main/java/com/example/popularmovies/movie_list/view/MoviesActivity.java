package com.example.popularmovies.movie_list.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.popularmovies.R;
import com.example.popularmovies.database.FavoriteEntity;
import com.example.popularmovies.databinding.ActivityMoviesBinding;
import com.example.popularmovies.movie_detail.view.MovieDetailActivity;
import com.example.popularmovies.movie_list.contract.MoviesContract;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.movie_list.presenter.MoviesPresenter;
import com.example.popularmovies.utils.ItemClickListner;

import java.util.List;

import static com.example.popularmovies.utils.Constants.CAMEFROM;
import static com.example.popularmovies.utils.Constants.FAVORITE;
import static com.example.popularmovies.utils.Constants.MOVIES;
import static com.example.popularmovies.utils.Constants.MOVIE_DETAILS;
import static com.example.popularmovies.utils.Constants.POPULAR;
import static com.example.popularmovies.utils.Constants.TOP_RATED;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View, ItemClickListner {

    private ActivityMoviesBinding binding;
    private Context context;
    private MoviesPresenter presenter;
    private MoviesAdapter adapter;
    private FavoriteMoviesAdapter favoriteMoviesAdapter;
    private String selectedType = POPULAR;
    private MenuItem item_popular, item_top_rated, item_favorite;
    private List<FavoriteEntity> favoriteEntityList;

    GridLayoutManager mLayoutManager;
    private int mScrollPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        context = this;
        presenter = createPresenter();

        adapter = new MoviesAdapter(this);
        favoriteMoviesAdapter = new FavoriteMoviesAdapter(this);
        binding.tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getMovies(selectedType);
            }
        });

        if (savedInstanceState != null) {
            selectedType = savedInstanceState.getString(getString(R.string.selected_type));
            mScrollPosition = savedInstanceState.getInt(getString(R.string.scroll_position));
        }

        assert selectedType != null;
        if (selectedType.equals(FAVORITE)) {
            setupViewModel();
        } else {
            presenter.getMovies(selectedType);
        }

        setNameOnToolbar();
    }

    private void setupViewModel() {
        MoviesViewModel viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        viewModel.getFavoriteEntities().observe(this, new Observer<List<FavoriteEntity>>() {

            @Override
            public void onChanged(@Nullable List<FavoriteEntity> favoriteEntities) {
                if (favoriteEntities != null && favoriteEntities.size() > 0) {
                    favoriteEntityList = favoriteEntities;
                    setDataOnFavoriteAdapter(favoriteEntities);
                    showMovieList(true);
                    showProgress(false);
                    showError(false, false, "", true);
                    binding.rvMovies.setAdapter(favoriteMoviesAdapter);
                } else {
                    showProgress(false);
                    showMovieList(false);
                    showError(true, true, context.getString(R.string.movie_list_is_empty), true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        int orientation = this.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
        } else {
            mLayoutManager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
        }
        binding.rvMovies.setLayoutManager(mLayoutManager);
        binding.rvMovies.setHasFixedSize(true);


        if (!selectedType.equals(FAVORITE)) {
            presenter.getMovies(selectedType);
        }
        setNameOnToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        item_popular = menu.findItem(R.id.menu_popular);
        item_top_rated = menu.findItem(R.id.menu_top_rated);
        item_favorite = menu.findItem(R.id.menu_favorite);
        setMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_popular:
                selectedType = POPULAR;
                presenter.getMovies(POPULAR);
                break;
            case R.id.menu_top_rated:
                selectedType = TOP_RATED;
                presenter.getMovies(TOP_RATED);
                break;

            case R.id.menu_favorite:
                selectedType = FAVORITE;
                presenter = new MoviesPresenter(this, this);
                setupViewModel();
                //setFavoriteDataOnAdapter();
                break;
        }
        setMenuItems();
        setNameOnToolbar();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        int scrollPosition = ((GridLayoutManager)
                binding.rvMovies.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition();
        outState.putInt(getString(R.string.scroll_position), scrollPosition);
        outState.putString(getString(R.string.selected_type), selectedType);
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
    public void showError(Boolean show, Boolean error, String errorMsg, Boolean favorite) {
        binding.rlError.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.tvError.setVisibility(error ? View.VISIBLE : View.GONE);
        binding.tvRetry.setVisibility(favorite ? View.GONE : View.VISIBLE);
        binding.tvEmpty.setVisibility(error ? View.GONE : View.VISIBLE);
        binding.tvError.setText(errorMsg);
    }

    @Override
    public void showMovieDetails(MoviesEntity moviesEntity) {

        Intent yourIntent = new Intent(this, MovieDetailActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(MOVIE_DETAILS, moviesEntity);
        b.putString(CAMEFROM, selectedType);
        yourIntent.putExtras(b);
        startActivity(yourIntent);
    }

    @Override
    public void showFavoriteMovieDetails(FavoriteEntity favoriteEntity) {

        MoviesEntity moviesEntity = new MoviesEntity();
        moviesEntity.setId(favoriteEntity.getId());
        moviesEntity.setAdult(favoriteEntity.getAdult());
        moviesEntity.setBackdrop_path(favoriteEntity.getBackdrop_path());
        moviesEntity.setOriginal_language(favoriteEntity.getOriginal_language());
        moviesEntity.setOriginal_title(favoriteEntity.getOriginal_title());
        moviesEntity.setOverview(favoriteEntity.getOverview());
        moviesEntity.setPopularity(favoriteEntity.getPopularity());
        moviesEntity.setPoster_path(favoriteEntity.getPoster_path());
        moviesEntity.setRelease_date(favoriteEntity.getRelease_date());
        moviesEntity.setTitle(favoriteEntity.getTitle());
        moviesEntity.setVideo(favoriteEntity.getVideo());
        moviesEntity.setVote_average(favoriteEntity.getVote_average());
        moviesEntity.setVote_count(favoriteEntity.getVote_count());

        Intent yourIntent = new Intent(this, MovieDetailActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(MOVIE_DETAILS, moviesEntity);
        yourIntent.putExtras(b);
        startActivity(yourIntent);
    }

    @Override
    public void setDataOnAdapter(List<MoviesEntity> moviesEntities) {
        adapter.setData(moviesEntities);
        binding.rvMovies.setAdapter(adapter);
        adapter.setOnClickListener(this);
        binding.rvMovies.scrollToPosition(mScrollPosition);

        setMenuItems();
    }

    @Override
    public void notifyMovieData() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public void setDataOnFavoriteAdapter(List<FavoriteEntity> favoriteEntities) {
        favoriteMoviesAdapter.setData(favoriteEntities);
        binding.rvMovies.setAdapter(favoriteMoviesAdapter);
        favoriteMoviesAdapter.setOnClickListener(this);
    }

    private MoviesPresenter createPresenter() {
        presenter = new MoviesPresenter(this, this);
        return presenter;
    }

    private void setMenuItems() {
        if (selectedType.equals(POPULAR)) {
            item_popular.setVisible(false);
            item_top_rated.setVisible(true);
            item_favorite.setVisible(true);
        } else if (selectedType.equals(TOP_RATED)) {
            item_popular.setVisible(true);
            item_favorite.setVisible(true);
            item_top_rated.setVisible(false);
        } else if (selectedType.equals(FAVORITE)) {
            item_popular.setVisible(true);
            item_top_rated.setVisible(true);
            item_favorite.setVisible(false);
        }
    }

    private void setNameOnToolbar() {
        if (selectedType.equals(POPULAR)) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(POPULAR + " " + MOVIES);
        } else if (selectedType.equals(TOP_RATED)) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(TOP_RATED + " " + MOVIES);
        } else if (selectedType.equals(FAVORITE)) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(FAVORITE + " " + MOVIES);
        }
    }

    @Override
    public void onClick(View view, int position) {
        if (selectedType.equals(FAVORITE))
            showFavoriteMovieDetails(favoriteEntityList.get(position));
        else
            presenter.onMovieClick(position);
    }

}
