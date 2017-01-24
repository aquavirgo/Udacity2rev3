package base.movie.popmovie.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import base.movie.popmovie.Movie;
import base.movie.popmovie.R;

/**
 * Created by Jakub on 2017-01-19.
 */

public class ReviewAdapter  extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Movie> moviesList;

    public ReviewAdapter(ArrayList<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      //  Log.d("MOVIE_LIST",moviesList.toString());

        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getReviewAuthor());
        holder.genre.setText(movie.getReviewContent());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genre;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.author);
            genre = (TextView) view.findViewById(R.id.content);
        }
    }
}

