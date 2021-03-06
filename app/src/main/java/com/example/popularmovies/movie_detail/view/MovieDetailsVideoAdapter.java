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
import com.example.popularmovies.databinding.ThumbnailRowBinding;
import com.example.popularmovies.movie_detail.entity.MovieDetailsVideoEntity;
import com.example.popularmovies.utils.ItemClickListner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import static com.example.popularmovies.utils.Constants.PRE_YOUTUBE_THUMBNAIL;

public class MovieDetailsVideoAdapter extends RecyclerView.Adapter<MovieDetailsVideoAdapter.MovieViewHolder> {

    private Context context;
    private ItemClickListner onClickListener;
    private List<MovieDetailsVideoEntity> moviesEntityList;
    private int lastPosition = -1;

    MovieDetailsVideoAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MovieDetailsVideoEntity> data) {
        this.moviesEntityList = data;
        notifyDataSetChanged();
    }

    public void setOnClickListener(ItemClickListner onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public MovieDetailsVideoAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ThumbnailRowBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.thumbnail_row, parent, false);
        return new MovieDetailsVideoAdapter.MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final MovieDetailsVideoAdapter.MovieViewHolder holder, int position) {
        MovieDetailsVideoEntity moviesEntity = moviesEntityList.get(position);

        setAnimation(holder.itemView, position);

        Picasso.with(context).load(PRE_YOUTUBE_THUMBNAIL + moviesEntity.getKey() + "/0.jpg")
                .error(R.drawable.error)
                .into(holder.binding.ivThumbnail, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.binding.pbLoadingThumbnail.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                    }
                });
        holder.binding.tvName.setText(moviesEntity.getName());
        Locale loc = new Locale(moviesEntity.getIso_639_1());
        holder.binding.tvMovieLang.setText(loc.getDisplayLanguage(loc));


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

        private ThumbnailRowBinding binding;

        private MovieViewHolder(ThumbnailRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
            binding.ivThumbnail.setOnClickListener(this);
            binding.ivShare.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (onClickListener != null)
                onClickListener.onClick(view, getAdapterPosition());
        }
    }
}
