package com.example.popularmovies.movie_detail.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.popularmovies.R;
import com.example.popularmovies.databinding.ReviewRowBinding;
import com.example.popularmovies.movie_detail.entity.MovieDetailsReviewEntity;
import com.example.popularmovies.utils.ItemClickListner;

import java.util.List;

public class MovieDetailsReviewAdapter extends RecyclerView.Adapter<MovieDetailsReviewAdapter.MovieViewHolder> {

    private Context context;
    private ItemClickListner onClickListener;
    private List<MovieDetailsReviewEntity> moviesEntityList;
    private int lastPosition = -1;

    MovieDetailsReviewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MovieDetailsReviewEntity> data) {
        this.moviesEntityList = data;
        notifyDataSetChanged();
    }

    public void setOnClickListener(ItemClickListner onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public MovieDetailsReviewAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReviewRowBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.review_row, parent, false);
        return new MovieDetailsReviewAdapter.MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MovieDetailsReviewAdapter.MovieViewHolder holder, int position) {
        MovieDetailsReviewEntity moviesEntity = moviesEntityList.get(position);

        setAnimation(holder.itemView, position);

        holder.binding.tvAuthor.setText(moviesEntity.getAuthor());
        holder.binding.tvContent.setText(moviesEntity.getContent());
        holder.binding.tvUrl.setText(moviesEntity.getUrl());
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

        private ReviewRowBinding binding;

        private MovieViewHolder(ReviewRowBinding binding) {
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
