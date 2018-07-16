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
import com.example.popularmovies.database.FavoriteEntity;
import com.example.popularmovies.databinding.MovieRowBinding;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.utils.ItemClickListner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import static com.example.popularmovies.utils.Constants.IMAGE_APPEND;

public class FavoriteMoviesAdapter extends RecyclerView.Adapter<FavoriteMoviesAdapter.MovieViewHolder> {

    private Context context;
    private ItemClickListner onClickListener;
    private List<FavoriteEntity> moviesEntityList;
    private int lastPosition = -1;

    FavoriteMoviesAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<FavoriteEntity> data) {
        this.moviesEntityList = data;
        notifyDataSetChanged();
    }

    public void setOnClickListener(ItemClickListner onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public FavoriteMoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MovieRowBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.movie_row, parent, false);
        return new FavoriteMoviesAdapter.MovieViewHolder(binding);
    }

    public List<FavoriteEntity> getMoviesEntityList() {
        return moviesEntityList;
    }

    @Override
    public void onBindViewHolder(final FavoriteMoviesAdapter.MovieViewHolder holder, int position) {
        FavoriteEntity moviesEntity = moviesEntityList.get(position);

        setAnimation(holder.itemView, position);

        if (moviesEntity.getPoster_path() != null && !moviesEntity.getPoster_path().equals("")) {
            Picasso.with(context).load(IMAGE_APPEND + moviesEntity.getPoster_path())
                    .error(R.drawable.error)
                    .into(holder.binding.ivMovieBanner, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.binding.pbLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        } else {
            Picasso.with(context).load(R.drawable.image_not_available)
                    .into(holder.binding.ivMovieBanner);
        }
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

        private MovieViewHolder(MovieRowBinding binding) {
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
