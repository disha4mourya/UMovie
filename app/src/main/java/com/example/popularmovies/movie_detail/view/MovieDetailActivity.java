package com.example.popularmovies.movie_detail.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.popularmovies.R;
import com.example.popularmovies.databinding.ActivityMovieDetailBinding;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.squareup.picasso.Picasso;

import static com.example.popularmovies.utils.Constants.BIG_IMAGE_APPEND;
import static com.example.popularmovies.utils.Constants.IMAGE_APPEND;


public class MovieDetailActivity extends AppCompatActivity {
    ActivityMovieDetailBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        context = this;

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        assert bundle != null;
        MoviesEntity moviesEntity = (MoviesEntity) bundle.getSerializable("movieDetails");
        setDataOnViews(moviesEntity);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void setDataOnViews(MoviesEntity moviesEntity) {

        Picasso.with(context).load(BIG_IMAGE_APPEND + moviesEntity.getBackdrop_path())
                .error(R.drawable.error)
                .placeholder(R.drawable.rotate)
                .into(binding.ivMovieBackDrop);
        
        Picasso.with(context).load(IMAGE_APPEND + moviesEntity.getPoster_path())
                .error(R.drawable.error)
                .placeholder(R.drawable.rotate)
                .into(binding.ivMovieBanner);

        binding.tvMovieRate.setText(moviesEntity.getVote_average());
        binding.tvOriginalTitle.setText(moviesEntity.getOriginal_title());
        // binding.tvOverview.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        String overview;
        //  overview = "<html><body><p align=\"justify\">";
        overview = "<html><head>"
                + "<style type=\"text/css\">body{color: #BDBDBD; background-color: #000;}"
                + "</style></head><body><p align=\"justify\">";
        overview += moviesEntity.getOverview();
        overview += "</p></body></html>";
        binding.wvOverview.loadData(overview, "text/html", "utf-8");
        binding.tvReleaseDate.setText(moviesEntity.getRelease_date());
        binding.tvTotalCount.setText(moviesEntity.getVote_average());
        binding.tvTotalReviewCount.setText(moviesEntity.getVote_count());
        float voteRate = Float.valueOf(moviesEntity.getVote_average());
        binding.ratingBar1.setRating(voteRate);

    }

}
