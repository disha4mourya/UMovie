package com.example.popularmovies.movie_list.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.popularmovies.R;
import com.example.popularmovies.databinding.MovieRowBinding;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.utils.ItemClickListner;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import static com.example.popularmovies.utils.Constants.IMAGE_APPEND;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Context context;
    private ItemClickListner onClickListener;
    private List<MoviesEntity> moviesEntityList;
    private int lastPosition = -1;

    MoviesAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MoviesEntity> data) {
        this.moviesEntityList = data;
        notifyDataSetChanged();
    }

    public void setOnClickListener(ItemClickListner onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MovieRowBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.movie_row, parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MoviesEntity moviesEntity = moviesEntityList.get(position);

        setAnimation(holder.itemView, position);

        Picasso.with(context).load(IMAGE_APPEND + moviesEntity.getPoster_path())
                .placeholder(R.drawable.ic_error_outline_grey_600_48dp)// Place holder image from drawable folder
                .error(R.drawable.ic_error_outline_grey_600_48dp)
                .into(holder.binding.ivMovieBanner);

        holder.binding.tvMovieName.setSelected(true);
        holder.binding.tvMovieName.setText(moviesEntity.getTitle());
        Locale loc = new Locale(moviesEntity.getOriginal_language());
        holder.binding.tvMovieLang.setText(loc.getDisplayLanguage(loc));
        holder.binding.tvMovieRate.setText(moviesEntity.getVote_average());
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return moviesEntityList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MovieRowBinding binding;

        public MovieViewHolder(MovieRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (onClickListener != null)
                onClickListener.onClick(view, getAdapterPosition());
        }
    }
}
