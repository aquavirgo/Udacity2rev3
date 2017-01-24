package base.movie.popmovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import base.movie.popmovie.Movie;
import base.movie.popmovie.R;

/**
 * Created by Jakub on 2017-01-19.
 */

public class TrailerAdapter  extends RecyclerView.Adapter<TrailerAdapter.ViewHolder>  implements View.OnClickListener{

    private ArrayList<Movie> moviesList;

    final private TrailerAdapter.ListItemClickListener mOnClickListener;

    @Override
    public void onClick(View view) {

    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public TrailerAdapter(Context context, ArrayList<Movie> moviesList, ListItemClickListener listener) {
         this.moviesList = moviesList;
        mOnClickListener = listener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items_trailer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    //    Log.d("MOVIE_LIST",moviesList.toString());

        Movie movie = moviesList.get(position);
        holder.image.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        holder.title.setText(movie.getTrailerTitle());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public ImageView image;

        public ViewHolder(View view) {
            super(view);

            image= (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(final View v) {

            // onItemClickListener.onItemClick(v, (String) v.getTag());
            //Log.d("View", v.getTag().toString());
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}

