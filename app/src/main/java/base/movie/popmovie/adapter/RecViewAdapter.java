package base.movie.popmovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import base.movie.popmovie.MainActivity;
import base.movie.popmovie.R;

/**
 * Created by Jakub on 2017-01-14.
 */

public class RecViewAdapter  extends RecyclerView.Adapter<RecViewAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<String> items;
    private ArrayList<String> images;

    final private ListItemClickListener mOnClickListener;



    @Override
    public void onClick(View view) {

    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }



    public RecViewAdapter(Context context, ListItemClickListener listener, ArrayList<String> items) {
        this.items = items;
        mOnClickListener = listener;
    }



    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_items, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        position =holder.getAdapterPosition();
        String item = items.get(position);
        holder.image.setImageBitmap(null);
        Picasso.with(holder.image.getContext()).load(MainActivity.images.get(position)).into(holder.image);


        holder.itemView.setTag(item);
    }

    @Override public int getItemCount() {
        return items.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.rv_item_number);
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
