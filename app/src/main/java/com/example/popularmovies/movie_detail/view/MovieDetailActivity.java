package com.example.popularmovies.movie_detail.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.popularmovies.R;
import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.FavoriteEntity;
import com.example.popularmovies.databinding.ActivityMovieDetailBinding;
import com.example.popularmovies.movie_detail.contract.MovieDetailsContract;
import com.example.popularmovies.movie_detail.entity.MovieDetailsReviewEntity;
import com.example.popularmovies.movie_detail.entity.MovieDetailsVideoEntity;
import com.example.popularmovies.movie_detail.presenter.MovieDetailsPresenter;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.utils.AppExecutors;
import com.example.popularmovies.utils.ItemClickListner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.popularmovies.utils.Constants.BIG_IMAGE_APPEND;
import static com.example.popularmovies.utils.Constants.IMAGE_APPEND;
import static com.example.popularmovies.utils.Constants.MOVIE_DETAILS;


public class MovieDetailActivity extends AppCompatActivity implements MovieDetailsContract.View, ItemClickListner {
    private ActivityMovieDetailBinding binding;
    private Context context;
    MoviesEntity moviesEntity;
    FavoriteEntity favoriteEntity;

    private AppDatabase mDb;

    MovieDetailsPresenter presenter;
    MovieDetailsVideoAdapter movieDetailsVideoAdapter;
    MovieDetailsReviewAdapter movieDetailsReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        context = this;

        presenter=createPresenter();
        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        assert bundle != null;
        moviesEntity = (MoviesEntity) bundle.getSerializable(MOVIE_DETAILS);
        if (moviesEntity != null)
            setDataOnViews();


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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MOVIE_DETAILS, moviesEntity);
        super.onSaveInstanceState(outState);
    }

    public void markAsFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FavoriteEntity favoriteEntity2 = mDb.favoriteDao().loadMovieById(moviesEntity.getId());

                if (favoriteEntity2 != null) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            mDb.favoriteDao().deleteMovie(moviesEntity.getId());
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.unmarkedstar));
                            binding.tvFavorite.setText(R.string.mark_as_favorite);
                        }
                    });
                } else {
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.markedstar));
                            binding.tvFavorite.setText(R.string.remove_from_favorites);
                        }
                    });
                }
            }
        });
        /*if (checkFavoriteStoredData(moviesEntity.getId()) != null) {

            Log.d("alreadymarked", "datailcheck " + moviesEntity.getId());
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.favoriteDao().deleteMovie(moviesEntity.getId());
                }
            });
            binding.ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.unmarkedstar));
            binding.tvFavorite.setText(R.string.mark_as_favorite);
            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();

        } else {
            binding.ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.markedstar));
            binding.tvFavorite.setText(R.string.remove_from_favorites);
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

            this.favoriteEntity = null;
            Toast.makeText(context, "Marked as favorite!!!", Toast.LENGTH_SHORT).show();
        }*/
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

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FavoriteEntity favoriteEntity2 = mDb.favoriteDao().loadMovieById(moviesEntity.getId());

                if (favoriteEntity2 != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.markedstar));
                        }
                    });
                }
            }
        });
        /*FavoriteEntity favoriteEntity = checkFavoriteStoredData(moviesEntity.getId());
        if (favoriteEntity != null) {
            binding.ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.markedstar));
            this.favoriteEntity = null;
        } else {
            Log.d("gotThisId", "db" + "didn't get");
        }*/


    }







    private MovieDetailsPresenter createPresenter() {
        presenter = new MovieDetailsPresenter(this, this);
        presenter.getVideos();
        presenter.getReviews();
        return presenter;
    }


    @Override
    public void showVideoProgress(Boolean show) {

    }

    @Override
    public void showVideoList(Boolean show) {

    }

    @Override
    public void showVideoError(Boolean show, Boolean error, String errorMsg, Boolean favorite) {

    }

    @Override
    public void showTrailer(MovieDetailsVideoEntity moviesEntity) {

        try {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + moviesEntity.getKey()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {

            // youtube is not installed.Will be opened in other available apps

            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + moviesEntity.getKey()));
            startActivity(i);
        }
    }

    @Override
    public void setThumbnailsOnAdapter(List<MovieDetailsVideoEntity> moviesEntities) {

    }

    @Override
    public void showReviewProgress(Boolean show) {

    }

    @Override
    public void showReviewList(Boolean show) {

    }

    @Override
    public void showReviewError(Boolean show, Boolean error, String errorMsg, Boolean favorite) {

    }

    @Override
    public void setReviewOnAdapter(List<MovieDetailsReviewEntity> moviesEntities) {

    }

    @Override
    public void onClick(View view, int position) {

    }
}
