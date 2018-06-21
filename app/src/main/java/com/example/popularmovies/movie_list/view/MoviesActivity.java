package com.example.popularmovies.movie_list.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.popularmovies.R;
import com.example.popularmovies.databinding.ActivityMoviesBinding;
import com.example.popularmovies.movie_detail.view.MovieDetailActivity;
import com.example.popularmovies.movie_list.contract.MoviesContract;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.movie_list.presenter.MoviesPresenter;
import com.example.popularmovies.utils.ItemClickListner;

import java.util.List;

import static com.example.popularmovies.utils.Constants.INTERNET_LOST;
import static com.example.popularmovies.utils.Constants.MOVIES;
import static com.example.popularmovies.utils.Constants.POPULAR;
import static com.example.popularmovies.utils.Constants.TOP_RATED;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View, ItemClickListner {

    private ActivityMoviesBinding binding;
    private Context context;
    private MoviesPresenter presenter;
    private MoviesAdapter adapter;
    GridLayoutManager mLayoutManager;
    String selectedType;
    MenuItem item_popular, item_top_rated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        context = this;
        selectedType = POPULAR;
        setNameOnToolbar();
        presenter = createPresenter(POPULAR);
        adapter = new MoviesAdapter(this);
        int spacingInPixels = 0;
        binding.rvMovies.addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        registerReceiver(broadcastReceiver, new IntentFilter(INTERNET_LOST));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            createPresenter(POPULAR);
        }
    };

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        item_popular = menu.findItem(R.id.menu_popular);
        item_top_rated = menu.findItem(R.id.menu_top_rated);
        setMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_popular:
                selectedType = POPULAR;
                createPresenter(POPULAR);
                break;
            case R.id.menu_top_rated:
                selectedType = TOP_RATED;
                createPresenter(TOP_RATED);
                break;
        }
        setNameOnToolbar();
        return true;
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
    public void showError(Boolean show, Boolean error, String errorMsg) {
        binding.rlError.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.tvError.setVisibility(error ? View.VISIBLE : View.GONE);
        binding.tvEmpty.setVisibility(error ? View.GONE : View.VISIBLE);
        binding.tvError.setText(errorMsg);
    }

    @Override
    public void showMovieDetails(MoviesEntity moviesEntity) {

       /* Intent intent = new Intent(context, MovieDetailActivity.class);
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

        startActivity(intent);*/

        Intent yourIntent = new Intent(this, MovieDetailActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("movieDetails", moviesEntity);
        yourIntent.putExtras(b); //pass bundle to your intent
        startActivity(yourIntent);
    }

    @Override
    public void setDataOnAdapter(List<MoviesEntity> moviesEntities) {
        adapter.setData(moviesEntities);
        binding.rvMovies.setAdapter(adapter);
        adapter.setOnClickListener(this);
        setMenuItems();
    }

    @Override
    public void notifyMovieData() {
        adapter.notifyDataSetChanged();
    }

    public MoviesPresenter createPresenter(String type) {
        presenter = new MoviesPresenter(context, this);
        presenter.getMovies(type);
        return presenter;
    }

    private void setMenuItems() {
        if (selectedType.equals(POPULAR)) {
            item_popular.setVisible(false);
            item_top_rated.setVisible(true);
        } else if (selectedType.equals(TOP_RATED)) {
            item_popular.setVisible(true);
            item_top_rated.setVisible(false);
        }
    }

    private void setNameOnToolbar() {
        if (selectedType.equals(POPULAR)) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(POPULAR + " " + MOVIES);
        } else if (selectedType.equals(TOP_RATED)) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(TOP_RATED + " " + MOVIES);
        }
    }

    @Override
    public void onClick(View view, int position) {
        presenter.onMovieClick(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
