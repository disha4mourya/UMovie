package com.example.popularmovies.movie_list.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.popularmovies.R;
import com.example.popularmovies.databinding.MovieRowBinding;
import com.example.popularmovies.movie_list.entity.MoviesEntity;
import com.example.popularmovies.utils.ItemClickListner;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.popularmovies.utils.Constants.IMAGE_APPEND;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private Context context;
    private ItemClickListner onClickListener;
    private List<MoviesEntity> moviesEntityList;

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

        Log.d("InAdapter","dataSet"+moviesEntity.getTitle());
        Picasso.with(context).load(IMAGE_APPEND + moviesEntity.getPoster_path())
                .placeholder(R.drawable.ic_error_outline_grey_600_48dp)// Place holder image from drawable folder
                .error(R.drawable.ic_error_outline_grey_600_48dp)
                .into(holder.binding.ivMovieBanner);
        holder.binding.tvMovieName.setText(moviesEntity.getTitle());
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return moviesEntityList.size();
    }


  /*  public View getView(int position, View view, ViewGroup viewGroup) {
        View vi = view;
        if (view == null)
            vi = inflater.inflate(R.layout.movie_row, null);
        ImageView ivMoviePoster = vi.findViewById(R.id.ivMovieBanner);
        TextView tvMovieName = vi.findViewById(R.id.tvMovieName);

        MoviesEntity moviesEntity = presenter.getAdapterEntity(position);

        Picasso.with(context).load(IMAGE_APPEND + moviesEntity.getPoster_path())
                .placeholder(R.drawable.ic_error_outline_grey_600_48dp)// Place holder image from drawable folder
                .error(R.drawable.ic_error_outline_grey_600_48dp)
                .centerCrop()
                .into(ivMoviePoster);
        tvMovieName.setText(moviesEntity.getTitle());


        return null;
    }*/

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
                onClickListener.onClick(view,getAdapterPosition());
        }
    }
}
