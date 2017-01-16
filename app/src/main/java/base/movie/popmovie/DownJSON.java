package base.movie.popmovie;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.os.Build.VERSION_CODES.N;


/**
 * Created by Jakub on 2017-01-14.
 */

public class DownJSON extends AsyncTask<String, Void, String> {

    static public String URL = "https://api.themoviedb.org/3/movie/";
    static public String IMAGE_URL = "http://image.tmdb.org/t/p/";
    static public String DEF_IMAGE_SIZE = "w185";
    static public String DEF_IMAGE = "http://www.classicposters.com/images/nopicture.gif";

    private Context mContext;

    public DownJSON (Context context){
        mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }
        String sortingBy = params[0];

        String newUrl = URL+sortingBy;
        Uri builtUri = Uri.parse(newUrl).buildUpon()
            //    .appendQueryParameter("sort_by", sortingBy + ".desc")
                .appendQueryParameter("api_key", BuildConfig.MYAPIKEY)
                .build();
        String response;
        Log.d("builtUrl",builtUri.toString());
        try {
            response  = getJSON(builtUri);
            //Log.d("responseJSON",response);
            return response;
        }catch (Exception e){
            Toast.makeText(mContext,"Conection Error", Toast.LENGTH_LONG).show();
            return null;
        }


    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            loadInfo(response);
        } else {
            Toast.makeText(mContext,"No Internet Conection", Toast.LENGTH_LONG).show();
        }

    }



    public  static void loadInfo (String jsonString) {
        MainActivity.images.clear();
        MainActivity.moviesList.clear();

        try {
            if (jsonString != null) {
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesArray = moviesObject.getJSONArray("results");

              //  Log.d("JSON",moviesArray.toString());


                for (int i = 0; i <= moviesArray.length()-1; i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);
                    base.movie.popmovie.Movie movieItem = new base.movie.popmovie.Movie();
                    movieItem.setTitle(movie.getString("title"));
                    movieItem.setId(movie.getInt("id"));
                    movieItem.setBackdropPath(movie.getString("backdrop_path"));
                    movieItem.setOriginalTitle(movie.getString("original_title"));
                    movieItem.setOriginalLanguage(movie.getString("original_language"));
                    if (movie.getString("overview") == "null") {
                        movieItem.setOverview("Lack of overview");
                    } else {
                        movieItem.setOverview(movie.getString("overview"));
                    }
                    if (movie.getString("release_date") == "null") {
                        movieItem.setReleaseDate("Unknown release date");
                    } else {
                        movieItem.setReleaseDate(movie.getString("release_date"));
                    }
                    movieItem.setPopularity(movie.getString("popularity"));
                    movieItem.setVoteAverage(movie.getString("vote_average"));
                    movieItem.setPosterPath(movie.getString("poster_path"));
                    if (movie.getString("poster_path") == "null") {
                        MainActivity.images.add(DEF_IMAGE);
                        movieItem.setPosterPath(DEF_IMAGE);
                    } else {
                        MainActivity.images.add(IMAGE_URL + DEF_IMAGE_SIZE + movie.getString("poster_path"));
                    }
                  //  Log.d("MA",MainActivity.images.toString());

                    MainActivity.moviesList.add(movieItem);

                  MainActivity.recyclerView_Adapter.notifyDataSetChanged();
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
            URL url = new URL(builtUri.toString());
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