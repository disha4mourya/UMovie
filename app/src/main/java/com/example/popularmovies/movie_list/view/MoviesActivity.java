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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.popularmovies.R;
import com.example.popularmovies.database.AppDatabase;
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
    private AppDatabase mDb;
    private List<FavoriteEntity> favoriteEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        context = this;
        mDb = AppDatabase.getInstance(getApplicationContext());
        // selectedType = POPULAR;
        presenter = createPresenter();

        adapter = new MoviesAdapter(this);
        favoriteMoviesAdapter = new FavoriteMoviesAdapter(this);
        binding.tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getMovies(selectedType);
            }
        });

        /*new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP | ItemTouchHelper.DOWN) {
            @Override
            public boolean onMove(RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<FavoriteEntity> favoriteEntities = favoriteMoviesAdapter.getMoviesEntityList();
                        mDb.favoriteDao().deleteMovie(favoriteEntities.get(position).getId());
                        createPresenter(selectedType);
                    }
                });
            }
        }).attachToRecyclerView(binding.rvMovies);*/

        Log.d("restoringTheDataOncreat", "is " + selectedType);

        if (savedInstanceState != null)
            selectedType = savedInstanceState.getString("selectedType");

        assert selectedType != null;
        if (selectedType.equals(FAVORITE)) {
            setupViewModel();
        } else {
            presenter.getMovies(selectedType);
        }

        setNameOnToolbar();
    }

    private void setupViewModel() {
        // COMPLETED (5) Remove the logging and the call to loadAllTasks, this is done in the ViewModel now
        // COMPLETED (6) Declare a ViewModel variable and initialize it by calling ViewModelProviders.of
        MoviesViewModel viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        // COMPLETED (7) Observe the LiveData object in the ViewModel
        viewModel.getFavoriteEntities().observe(this, new Observer<List<FavoriteEntity>>() {

            @Override
            public void onChanged(@Nullable List<FavoriteEntity> favoriteEntities) {
                if (favoriteEntities != null && favoriteEntities.size() > 0) {
                    //favoriteMoviesAdapter.setData(favoriteEntities);
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

    /*public void retriveFavoriteMovies() {
        LiveData<List<FavoriteEntity>> tasks = mDb.favoriteDao().loadAllMovies();
        tasks.observe(this, new Observer<List<FavoriteEntity>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntity> favoriteEntities) {
                if (favoriteEntities != null && favoriteEntities.size() > 0) {
                    //favoriteMoviesAdapter.setData(favoriteEntities);
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
        favoriteMoviesAdapter.setOnClickListener(this);

    }
*/
    @Override
    protected void onResume() {
        super.onResume();

        int orientation = this.getResources().getConfiguration().orientation;
        GridLayoutManager mLayoutManager;
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

   /* public void setFavoriteDataOnAdapter() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<FavoriteEntity> favoriteEntityList = mDb.favoriteDao().loadAllMovies();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        favoriteMoviesAdapter.setData(favoriteEntityList);
                        binding.rvMovies.setAdapter(favoriteMoviesAdapter);
                    }
                });
            }
        });
        favoriteMoviesAdapter.setOnClickListener(this);


    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("storingTheData", "is " + selectedType);

        outState.putString("selectedType", selectedType);
    }

   /* @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("restoringTheData", "is " + savedInstanceState.getString("selectedType"));
        selectedType = savedInstanceState.getString("selectedType");

        if (selectedType.equals(FAVORITE)) {
            retriveFavoriteMovies();
        } else {
            presenter.getMovies(selectedType);
        }

        setNameOnToolbar();
    }*/

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
        // setMenuItems();
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
