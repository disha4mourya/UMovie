package com.example.popularmovies.movie_detail.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.popularmovies.R;
import com.example.popularmovies.databinding.ActivityNewMovieDetailBinding;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.example.popularmovies.utils.Constants.BIG_IMAGE_APPEND;
import static com.example.popularmovies.utils.Constants.IMAGE_APPEND;
import static com.example.popularmovies.utils.Constants.MOVIE_DETAILS;

public class NewMovieDetailActivity extends AppCompatActivity {

    ActivityNewMovieDetailBinding binding;
    private Context context;
    MoviesEntity moviesEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_movie_detail);
        context = this;

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        assert bundle != null;

        moviesEntity = (MoviesEntity) bundle.getSerializable(MOVIE_DETAILS);
        if (moviesEntity != null)
            setDataOnViews();

        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.collapsingToolbarLayout.setTitle(moviesEntity.getOriginal_title());
                    isShow = true;
                } else if (isShow) {
                    binding.collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDataOnViews() {
        String overview;
        float voteRate;
        Picasso.with(context).load(BIG_IMAGE_APPEND + moviesEntity.getBackdrop_path())
                .error(R.drawable.error)
                .into(binding.ivMovieBackDrop, new Callback() {
                    @Override
                    public void onSuccess() {
                        // binding.pbLoading.setVisibility(View.GONE);
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
                        //  binding.pbLoading.setVisibility(View.GONE);
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
      /*  if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(moviesEntity.getOriginal_title());
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcon));
        }*/
    }
}

