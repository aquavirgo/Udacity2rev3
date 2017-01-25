package base.movie.popmovie;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import base.movie.popmovie.adapter.ReviewAdapter;
import base.movie.popmovie.adapter.TrailerAdapter;
import base.movie.popmovie.asynctask.DownJSON;
import base.movie.popmovie.data.MovieContract;

import static base.movie.popmovie.MainActivity.images;
import static base.movie.popmovie.MainActivity.moviesList;

public class MovieOverview extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,TrailerAdapter.ListItemClickListener{

    private static int REVIEW_LOADER=1;
    private static final String REVIEW_EXTRA="movie_id";
    private static final String TYPE_DOWNLOAD="videos";
    private static final String YOUTUBE_URL="http://www.youtube.com/watch?v=";
    Movie movie;
    String posterUrl;
    Context mContext;
    TextView textView;
    private RecyclerView recyclerView,recyclerViewTrailer;
    static public ArrayList<base.movie.popmovie.Movie> moviesListReview;
    static public ArrayList<base.movie.popmovie.Movie> moviesListTrailer;
    static public String URL = "https://api.themoviedb.org/3/movie/";
    private static ReviewAdapter mAdapter;
    private static TrailerAdapter mAdapterTrailer;
    ImageView stars;
    boolean isFavorite =false;
    int moviePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_overview);



       //UI FOR MovieOverview
        textView = (TextView) findViewById(R.id.review_view);

        moviePosition = getIntent().getIntExtra(getString(R.string.position), 0);
        movie = moviesList.get(moviePosition);


        mContext = getApplicationContext();
        TextView movieTitle = (TextView) findViewById(R.id.title);
        movieTitle.setText(movie.getOriginalTitle());

        TextView rating = (TextView) findViewById(R.id.rating);
        rating.setText(movie.getVoteAverage() + "/10");

        TextView release = (TextView) findViewById(R.id.release);
        release.setText(movie.getReleaseDate());

        TextView synopsis = (TextView) findViewById(R.id.synopsis);
        synopsis.setText(movie.getOverview());

        ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);


        //Check if data from Internet or Content Provider
        if(movie.getPosterPath().substring(0,7).equals("http://")){
            posterUrl = movie.getPosterPath();
        }else {
            if (movie.getPosterPath() == DownJSON.DEF_IMAGE) {
                posterUrl = DownJSON.DEF_IMAGE;
            } else {
                posterUrl = DownJSON.IMAGE_URL + DownJSON.DEF_IMAGE_SIZE + "/" + movie.getPosterPath();
            }
        }


        Picasso.with(mContext).load(posterUrl).into(thumbnail);
        synopsis.setVisibility(View.VISIBLE);

        //Check movie in favorites
        checkFavorite();

        //Preparation of RecyclerView for Movie Review
        prepRecViewAdapterReview();
        //AsyncTaskLoader for movie reviews
        updateMoviesReviews();

        //Preparation of RecyclerView for Movie Trailers
        prepRecViewAdapterTrailer();
        //AsyncTaskLoader for movie trailers
        updateMoviesTrailer();

    }


    //Put or delete movie from favorites list
    public void addToFavorites(View view){
        if(isFavorite==true){
            isFavorite=false;
            //For delete movie from favorites list change star icon to black border
            stars = (ImageView) findViewById(R.id.star);
            stars.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
            //and delete movie from content provider and sql data base
            Uri uri =MovieContract.MovieEntry.CONTENT_URI;
            getContentResolver().delete(uri,MovieContract.MovieEntry.COLUMN_MOVIE_ID+ "=?",new String[]{Long.toString(movie.getId())});

            Toast.makeText(getBaseContext(), getString(R.string.movie_removed), Toast.LENGTH_LONG).show();

            images.remove(moviePosition);
            moviesList.remove(moviePosition);
            MainActivity.recyclerView_Adapter.notifyItemRemoved(moviePosition);
            MainActivity.recyclerView_Adapter.notifyItemRangeChanged(moviePosition,images.size());


        }else {
            isFavorite = true;
            //If movie will be set in sql data base change star icon to mark
            stars = (ImageView) findViewById(R.id.star);
            stars.setBackgroundResource(R.drawable.ic_star_black_24dp);

            //and take values from movie object and put to Content Provider
            ContentValues contentValues = new ContentValues();
            // Put the task description and selected mPriority into the ContentValues
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
           // Log.d("ID", String.valueOf(movie.getId()));
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, posterUrl);
            contentValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
            contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getVoteAverage());
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE, movie.getReleaseDate());
            // Insert the content values via a ContentResolver
            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

            if(uri != null) {
                Toast.makeText(getBaseContext(), getString(R.string.movie_added), Toast.LENGTH_LONG).show();
            }

        }
    }


//Check value if is in favorite check list
public void checkFavorite() {
    Cursor data = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
            null,
            null,
            null,
            null);

    data.moveToFirst();
    while (data.isAfterLast() == false) {
        String name = data.getString(1);
        if (name.equals(movie.getOriginalTitle())) {
            isFavorite = true;
            stars = (ImageView) findViewById(R.id.star);
            stars.setBackgroundResource(R.drawable.ic_star_black_24dp);
            break;
        }
        data.moveToNext();
    }
}

    //Preparation  Adapter View for Movie Review
    public void prepRecViewAdapterReview(){
        REVIEW_LOADER =1; //#Review
        getSupportLoaderManager().initLoader(REVIEW_LOADER, null, this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_review);
        moviesListReview = new ArrayList<Movie>();
        mAdapter = new ReviewAdapter(moviesListReview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    //Preparation  Adapter View for Movie Trailers
    public void prepRecViewAdapterTrailer(){
        REVIEW_LOADER =2; //#Trailers
        getSupportLoaderManager().initLoader(REVIEW_LOADER, null, this);
        recyclerViewTrailer = (RecyclerView) findViewById(R.id.recycler_view_trailer);
        moviesListTrailer = new ArrayList<Movie>();
        mAdapterTrailer = new TrailerAdapter(mContext,moviesListTrailer, this);
        RecyclerView.LayoutManager mLayoutManagerTrailer = new LinearLayoutManager(getApplicationContext());
        recyclerViewTrailer.setLayoutManager(mLayoutManagerTrailer);
        recyclerViewTrailer.setAdapter(mAdapterTrailer);
    }

    //Update Movie Review by AsyncTaskLoader
    public void updateMoviesReviews() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString(REVIEW_EXTRA,String.valueOf(movie.getId()));
        queryBundle.putString(TYPE_DOWNLOAD,"reviews");
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> reviewLoader = loaderManager.getLoader(REVIEW_LOADER);

        if(reviewLoader==null){
            loaderManager.initLoader(REVIEW_LOADER,queryBundle,this);
        }else{
            loaderManager.restartLoader(REVIEW_LOADER,queryBundle,this);
        }
    }

    //Update Movie Trailers by AsyncTaskLoader
    public void updateMoviesTrailer() {

        Bundle queryBundle = new Bundle();
        queryBundle.putString(REVIEW_EXTRA,String.valueOf(movie.getId()));
        queryBundle.putString(TYPE_DOWNLOAD,"videos");
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> reviewLoader = loaderManager.getLoader(REVIEW_LOADER);

        if(reviewLoader==null){
            loaderManager.initLoader(REVIEW_LOADER,queryBundle,this);
        }else{
            loaderManager.restartLoader(REVIEW_LOADER,queryBundle,this);
        }
    }

    //Listener for movie trailers
    @Override
    public void onListItemClick(int clickedItemIndex) {
        String key = String.valueOf(moviesListTrailer.get(clickedItemIndex).getTrailerKey());
        //shows movie trailer on web
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL + key));
        startActivity(appIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingPref.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


/*AsyncTaskLoader for Review and Trailers

 */
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String movieID = args.getString(REVIEW_EXTRA);
                String typ = args.getString(TYPE_DOWNLOAD);
                if (movieID == null | TextUtils.isEmpty(movieID)) {
                    return null;
                }
                String newUrl = URL + movieID + "/"+typ;
                Uri builtUri = Uri.parse(newUrl).buildUpon()
                        //    .appendQueryParameter("sort_by", sortingBy + ".desc")
                        .appendQueryParameter("api_key", BuildConfig.MYAPIKEY)
                        .build();
                String response;
                Log.d("builtUrl", builtUri.toString());
                try {
                    response = getJSON(builtUri);
                    //Log.d("responseJSON",response);
                    return response;
                } catch (Exception e) {
                    Toast.makeText(mContext, getString(R.string.error_conection), Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        };
    }


            @Override
            public void onLoadFinished(Loader<String> loader, String data) {
                if (data != null) {
                    // if loader id = 1 read review else trailers
                    loadInfo(data,loader.getId());
                } else {
                    Toast.makeText(mContext, getString(R.string.intenet_state), Toast.LENGTH_LONG).show();
                }

            }


        @Override
        public void onLoaderReset (Loader < String > loader) {

        }

    public  static void loadInfo (String jsonString, int version) {
         //if version =1 == review
        // version =2 == trailers
        if(version==1) {
            moviesListReview.clear();
        }else{moviesListTrailer.clear();
        }

        try {
            if (jsonString != null) {
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesArray = moviesObject.getJSONArray("results");
                for (int i = 0; i <= moviesArray.length()-1; i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);
                    base.movie.popmovie.Movie movieItem = new base.movie.popmovie.Movie();
                     //Review
                    if(version==1) {
                        movieItem.setReviewAuthor(movie.getString("author"));
                        movieItem.setReviewContent(movie.getString("content"));
                        moviesListReview.add(movieItem);
                       // Log.d("ML", String.valueOf(moviesListReview));
                        mAdapter.notifyDataSetChanged();
                     //Trailers
                    }else{
                        movieItem.setTrailerTitle(movie.getString("name"));
                        movieItem.setTrailerKey(movie.getString("key"));
                        moviesListTrailer.add(movieItem);
                      //  Log.d("ML", String.valueOf(moviesListTrailer));
                        mAdapterTrailer.notifyDataSetChanged();
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static String getJSON(Uri builtUri)
    {
        InputStream inputStream;
        StringBuffer buffer;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJson = null;

        try {
            java.net.URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            inputStream = urlConnection.getInputStream();
            buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            moviesJson = buffer.toString();
        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {

                }
            }
        }

        return moviesJson;
    }

}
