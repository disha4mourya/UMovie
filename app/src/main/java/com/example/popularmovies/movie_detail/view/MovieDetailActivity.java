package com.example.popularmovies.movie_detail.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import static com.example.popularmovies.utils.Constants.PRE_APP_YOUTUBE;
import static com.example.popularmovies.utils.Constants.PRE_YOUTUBE;


public class MovieDetailActivity extends AppCompatActivity implements MovieDetailsContract.View, ItemClickListner {
    private ActivityMovieDetailBinding binding;
    private Context context;
    MoviesEntity moviesEntity;

    private AppDatabase mDb;

    MovieDetailsPresenter presenter;
    MovieDetailsVideoAdapter movieDetailsVideoAdapter;
    MovieDetailsReviewAdapter movieDetailsReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        context = this;


        mDb = AppDatabase.getInstance(getApplicationContext());
        movieDetailsVideoAdapter = new MovieDetailsVideoAdapter(this);
        movieDetailsReviewAdapter = new MovieDetailsReviewAdapter(this);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        assert bundle != null;
        moviesEntity = (MoviesEntity) bundle.getSerializable(MOVIE_DETAILS);
        if (moviesEntity != null)
            setDataOnViews();

        presenter = createPresenter();
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
    protected void onResume() {
        super.onResume();

        LinearLayoutManager mLayoutManagerTrailer = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        binding.rvTrailer.setNestedScrollingEnabled(true);
        binding.rvTrailer.setLayoutManager(mLayoutManagerTrailer);
        binding.rvTrailer.setHasFixedSize(true);

        LinearLayoutManager mLayoutManagerReview = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        binding.rvReviews.setNestedScrollingEnabled(true);
        binding.rvReviews.setLayoutManager(mLayoutManagerReview);
        binding.rvReviews.setHasFixedSize(true);
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
                            binding.tvFavorite.setText(R.string.remove_from_favorites);
                        }
                    });
                }
            }
        });
    }


    private MovieDetailsPresenter createPresenter() {
        presenter = new MovieDetailsPresenter(this, this);
        presenter.getVideos(moviesEntity.getId());
        presenter.getReviews(moviesEntity.getId());
        return presenter;
    }


    @Override
    public void showVideoProgress(Boolean show) {
        binding.pbTrailerLoading.setVisibility(show ? View.VISIBLE : View.GONE);

    }

    @Override
    public void showVideoList(Boolean show) {
        binding.rvTrailer.setVisibility(show ? View.VISIBLE : View.GONE);

    }

    @Override
    public void showVideoError(Boolean show, Boolean error, String errorMsg, Boolean favorite) {
        binding.rlTrailerError.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.tvTrailerError.setVisibility(error ? View.VISIBLE : View.GONE);
        binding.tvTrailerRetry.setVisibility(favorite ? View.GONE : View.VISIBLE);
        binding.tvTrailerEmpty.setVisibility(error ? View.GONE : View.VISIBLE);
        binding.tvTrailerError.setText(errorMsg);
    }

    @Override
    public void showTrailer(MovieDetailsVideoEntity moviesEntity) {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PRE_APP_YOUTUBE + moviesEntity.getKey()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(PRE_YOUTUBE + moviesEntity.getKey()));
            startActivity(i);
        }
    }

    @Override
    public void shareTrailer(MovieDetailsVideoEntity moviesEntity) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.watch_this));
            String sAux = getString(R.string.amazing_trailer);
            sAux = sAux + PRE_YOUTUBE + moviesEntity.getKey() + "\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, getString(R.string.choose_one)));
        } catch (Exception e) {
            //e.toString();
        }
    }

    @Override
    public void setThumbnailsOnAdapter(List<MovieDetailsVideoEntity> moviesEntities) {
        movieDetailsVideoAdapter.setData(moviesEntities);
        binding.rvTrailer.setAdapter(movieDetailsVideoAdapter);
        movieDetailsVideoAdapter.setOnClickListener(this);
    }

    @Override
    public void notifyVideoData() {
        movieDetailsVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void showReviewProgress(Boolean show) {
        binding.pbReviewsLoading.setVisibility(show ? View.VISIBLE : View.GONE);

    }

    @Override
    public void showReviewList(Boolean show) {
        binding.rvReviews.setVisibility(show ? View.VISIBLE : View.GONE);

    }

    @Override
    public void showReviewError(Boolean show, Boolean error, String errorMsg, Boolean favorite) {
        binding.rlReviewsError.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.tvReviewsError.setVisibility(error ? View.VISIBLE : View.GONE);
        binding.tvReviewsRetry.setVisibility(favorite ? View.GONE : View.VISIBLE);
        binding.tvReviewsEmpty.setVisibility(error ? View.GONE : View.VISIBLE);
        binding.tvReviewsError.setText(errorMsg);
    }

    @Override
    public void setReviewOnAdapter(List<MovieDetailsReviewEntity> moviesEntities) {
        movieDetailsReviewAdapter.setData(moviesEntities);
        binding.rvReviews.setAdapter(movieDetailsReviewAdapter);
        movieDetailsReviewAdapter.setOnClickListener(this);
    }

    @Override
    public void notifyReviewData() {
        movieDetailsReviewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view, int position) {
        switch (view.getId()) {
            case R.id.ivShare:
                presenter.onShareClick(position);
                break;
            case R.id.ivThumbnail:
                presenter.onThumbnailClick(position);
                break;
        }
    }
}
