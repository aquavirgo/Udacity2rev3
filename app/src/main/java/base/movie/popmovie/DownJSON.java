package base.movie.popmovie;

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


/**
 * Created by Jakub on 2017-01-14.
 */

public class DownJSON extends AsyncTask<String, Void, String> {

    static public String API_URL = "http://api.themoviedb.org/3/discover/movie";
    static public String API_KEY = "";
    static public String IMAGE_URL = "http://image.tmdb.org/t/p/";
    static public String IMAGE_SIZE_185 = "w185";
    static public String IMAGE_NOT_FOUND = "http://www.classicposters.com/images/nopicture.gif";

    @Override
    protected String doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }
        String sortingCriteria = params[0];

        Uri builtUri = Uri.parse(API_URL).buildUpon()
                .appendQueryParameter("sort_by", sortingCriteria + ".desc")
                .appendQueryParameter("api_key", API_KEY)
                .build();
        String response;
        Log.d("TEST",builtUri.toString());
        try {
            response  = getJSON(builtUri);
            Log.d("TEST",response);
            return response;
        }catch (Exception e){
            MainActivity.toast.setText("Connection Error");
            MainActivity.toast.setDuration(Toast.LENGTH_SHORT);
            MainActivity.toast.show();
            return null;
        }


    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            loadInfo(response);
        } else {
            MainActivity.toast.setText("No Internet Conection");
            MainActivity.toast.setDuration(Toast.LENGTH_SHORT);
            MainActivity.toast.show();
        }

    }



    public  static void loadInfo (String jsonString) {
        MainActivity.images.clear();
        MainActivity.moviesList.clear();

        try {
            if (jsonString != null) {
                JSONObject moviesObject = new JSONObject(jsonString);
                JSONArray moviesArray = moviesObject.getJSONArray("results");

                Log.d("JSON",moviesArray.toString());


                for (int i = 0; i <= moviesArray.length()-1; i++) {
                    JSONObject movie = moviesArray.getJSONObject(i);
                    base.movie.popmovie.Movie movieItem = new base.movie.popmovie.Movie();
                    movieItem.setTitle(movie.getString("title"));
                    movieItem.setId(movie.getInt("id"));
                    movieItem.setBackdrop_path(movie.getString("backdrop_path"));
                    movieItem.setOriginal_title(movie.getString("original_title"));
                    movieItem.setOriginal_language(movie.getString("original_language"));
                    if (movie.getString("overview") == "null") {
                        movieItem.setOverview("No Overview was Found");
                    } else {
                        movieItem.setOverview(movie.getString("overview"));
                    }
                    if (movie.getString("release_date") == "null") {
                        movieItem.setRelease_date("Unknown Release Date");
                    } else {
                        movieItem.setRelease_date(movie.getString("release_date"));
                    }
                    movieItem.setPopularity(movie.getString("popularity"));
                    movieItem.setVote_average(movie.getString("vote_average"));
                    movieItem.setPoster_path(movie.getString("poster_path"));
                    if (movie.getString("poster_path") == "null") {
                        MainActivity.images.add(IMAGE_NOT_FOUND);
                        movieItem.setPoster_path(IMAGE_NOT_FOUND);
                    } else {
                        MainActivity.images.add(IMAGE_URL + IMAGE_SIZE_185 + movie.getString("poster_path"));
                    }
                    Log.d("MA",MainActivity.images.toString());

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