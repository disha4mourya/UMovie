package com.example.popularmovies.movie_detail.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.popularmovies.R;
import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.FavoriteEntity;
import com.example.popularmovies.databinding.ActivityMovieDetailBinding;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.utils.AppExecutors;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.example.popularmovies.utils.Constants.BIG_IMAGE_APPEND;
import static com.example.popularmovies.utils.Constants.CAMEFROM;
import static com.example.popularmovies.utils.Constants.FAVORITE;
import static com.example.popularmovies.utils.Constants.IMAGE_APPEND;
import static com.example.popularmovies.utils.Constants.MOVIE_DETAILS;


public class MovieDetailActivity extends AppCompatActivity {
    private ActivityMovieDetailBinding binding;
    private Context context;
    MoviesEntity moviesEntity;
    FavoriteEntity favoriteEntity;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        context = this;

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent i = getIntent();
        Bundle bundle = i.getExtras();


        assert bundle != null;
        CAMEFROM = bundle.getString(CAMEFROM);

        assert CAMEFROM != null;
        if (CAMEFROM.equals(FAVORITE)) {
            favoriteEntity = (FavoriteEntity) bundle.getSerializable(MOVIE_DETAILS);
            favoriteToMovieEntity();
        } else {
            moviesEntity = (MoviesEntity) bundle.getSerializable(MOVIE_DETAILS);
            if (moviesEntity != null)
                setDataOnViews();
        }

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markAsFavorite();
            }
        });
    }

    public void favoriteToMovieEntity() {
        moviesEntity = new MoviesEntity();
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
    }

    public void markAsFavorite() {

        binding.ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        final FavoriteEntity favoriteEntity = new FavoriteEntity(moviesEntity.getVote_count(), String.valueOf(moviesEntity.getId()), moviesEntity.getVideo(),
                moviesEntity.getVote_average(), moviesEntity.getTitle(), moviesEntity.getPopularity(), moviesEntity.getPoster_path(),
                moviesEntity.getOriginal_language(), moviesEntity.getOriginal_title(),
                moviesEntity.getBackdrop_path(), moviesEntity.getAdult(), moviesEntity.getOverview(), moviesEntity.getRelease_date());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.favoriteDao().insertMovie(favoriteEntity);
            }
        });

        Toast.makeText(context, "inserted", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setDataOnViews() {
        String overview;
        float voteRate;
        Picasso.with(context).load(BIG_IMAGE_APPEND + moviesEntity.getBackdrop_path())
                .error(R.drawable.error)
                .into(binding.ivMovieBackDrop, new Callback() {
                    @Override
                    public void onSuccess() {
                        binding.pbLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                    }
                });

        Picasso.with(context).load(IMAGE_APPEND + moviesEntity.getPoster_path())
                .error(R.drawable.error)
                .into(binding.ivMovieBanner, new Callback() {
                    @Override
                    public void onSuccess() {
                        binding.pbLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                    }
                });

        binding.tvMovieRate.setText(moviesEntity.getVote_average());
        binding.tvOriginalTitle.setText(moviesEntity.getOriginal_title());

        overview = "<html><head>"
                + "<style type=\"text/css\">body{color: #BDBDBD; background-color: #000;}"
                + "</style></head><body><p align=\"justify\">";
        overview += moviesEntity.getOverview();
        overview += "</p></body></html>";
        binding.wvOverview.loadData(overview, "text/html", "utf-8");
        binding.tvReleaseDate.setText(moviesEntity.getRelease_date());
        binding.tvTotalCount.setText(moviesEntity.getVote_average());
        binding.tvTotalReviewCount.setText(moviesEntity.getVote_count());
        voteRate = Float.valueOf(moviesEntity.getVote_average());
        binding.ratingBar1.setRating(voteRate);
    }
}
