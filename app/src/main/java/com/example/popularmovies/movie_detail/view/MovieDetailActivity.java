package com.example.popularmovies.movie_detail.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.popularmovies.R;
import com.example.popularmovies.databinding.ActivityMovieDetailBinding;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.squareup.picasso.Picasso;

import static com.example.popularmovies.utils.Constants.ADULT;
import static com.example.popularmovies.utils.Constants.BACKDROP_PATH;
import static com.example.popularmovies.utils.Constants.BIG_IMAGE_APPEND;
import static com.example.popularmovies.utils.Constants.ID;
import static com.example.popularmovies.utils.Constants.IMAGE_APPEND;
import static com.example.popularmovies.utils.Constants.ORIGINAL_LANGUAGE;
import static com.example.popularmovies.utils.Constants.OVERVIEW;
import static com.example.popularmovies.utils.Constants.POPULARITY;
import static com.example.popularmovies.utils.Constants.POSTER_PATH;
import static com.example.popularmovies.utils.Constants.RELEASE_DATE;
import static com.example.popularmovies.utils.Constants.TITLE;
import static com.example.popularmovies.utils.Constants.VIDEO;
import static com.example.popularmovies.utils.Constants.VOTE_AVERAGE;
import static com.example.popularmovies.utils.Constants.VOTE_COUNT;


public class MovieDetailActivity extends AppCompatActivity {


    ActivityMovieDetailBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        context=this;
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        assert bundle != null;
        MoviesEntity moviesEntity = (MoviesEntity) bundle.getSerializable("movieDetails");
        setDataOnViews(moviesEntity);

    }

    public void setDataOnViews(MoviesEntity moviesEntity){

        Picasso.with(context).load(BIG_IMAGE_APPEND+moviesEntity.getBackdrop_path())
                .error(R.drawable.ic_error_outline_grey_600_48dp)
                .placeholder(R.drawable.ic_error_outline_grey_600_48dp)
                .into(binding.ivMovieBackDrop);

        Picasso.with(context).load(IMAGE_APPEND+moviesEntity.getPoster_path())
                .error(R.drawable.ic_error_outline_grey_600_48dp)
                .placeholder(R.drawable.ic_error_outline_grey_600_48dp)
                .into(binding.ivMovieBanner);

        binding.tvMovieRate.setText(moviesEntity.getVote_average());
        binding.tvOriginalTitle.setText(moviesEntity.getOriginal_title());
        binding.tvOverview.setText(moviesEntity.getOverview());
        binding.tvReleaseDate.setText(moviesEntity.getRelease_date());
    }
    public void fetchDataFromIntent() {

        String vote_count=getIntent().getStringExtra(VOTE_COUNT);
        String id=getIntent().getStringExtra(ID);
        String video=getIntent().getStringExtra(VIDEO);
        String vote_average=getIntent().getStringExtra(VOTE_AVERAGE);
        String title=getIntent().getStringExtra(TITLE);
        String popularity=getIntent().getStringExtra(POPULARITY);
        String poster_path=getIntent().getStringExtra(POSTER_PATH);
        String original_language=getIntent().getStringExtra(ORIGINAL_LANGUAGE);
        String backup_path=getIntent().getStringExtra(BACKDROP_PATH);
        String adult=getIntent().getStringExtra(ADULT);
        String overview=getIntent().getStringExtra(OVERVIEW);
        String release_date=getIntent().getStringExtra(RELEASE_DATE);

       /* moviesEntity=new MoviesEntity();
        moviesEntity.setVote_count(vote_count);
        moviesEntity.setId(id);
        moviesEntity.setVideo(video);
        moviesEntity.setVote_average(vote_average);
        moviesEntity.setTitle(title);
        moviesEntity.setPopularity(popularity);
        moviesEntity.setPoster_path(poster_path);
        moviesEntity.setOriginal_language(original_language);
        moviesEntity.setBackdrop_path(backup_path);
        moviesEntity.setAdult(adult);
        moviesEntity.setOverview(overview);
        moviesEntity.setRelease_date(release_date);*/
    }
}
