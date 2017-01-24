package base.movie.popmovie.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import base.movie.popmovie.MainActivity;
import base.movie.popmovie.Movie;
import base.movie.popmovie.data.MovieContract;

/**
 * Created by Jakub on 2017-01-23.
 * Read data from Content Provider
 * */

public class FavoritesAsyncTask extends AsyncTask<Object, Object, String[]> {


    private Context mContext;
    public String[] image2; //

    public FavoritesAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected String[] doInBackground(Object... params) {

        if (params.length == 0) {
            return null;
        }
        Object sortingBy = params[0];


        try {

            return image2;
        } catch (Exception e) {
            Toast.makeText(mContext, "Conection Error", Toast.LENGTH_LONG).show();
            return null;
        }


    }

    @Override
    protected void onPostExecute(String[] response) {
        checkFavorite();
    }
//Read data from content provider and save in image and movieList
    public void checkFavorite() {

        MainActivity.images.clear();
        MainActivity.moviesList.clear();

        Cursor data = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);


        data.moveToFirst();

        while (data.isAfterLast() == false) {
            Movie movieItem = new Movie();

            String posterPath = data.getString(3);
            //Update movie images
            MainActivity.images.add(posterPath);

            movieItem.setOriginalTitle(data.getString(1));
            movieItem.setId(Integer.parseInt(data.getString(2)));
            movieItem.setPosterPath(data.getString(3));
            movieItem.setOverview(data.getString(4));
            movieItem.setVoteAverage(data.getString(5));
            movieItem.setReleaseDate(data.getString(6));
            //Upadate movielist
            MainActivity.moviesList.add(movieItem);
            //Update recyclerView adapter
            MainActivity.recyclerView_Adapter.notifyDataSetChanged();

            data.moveToNext();
        }
    }

}

