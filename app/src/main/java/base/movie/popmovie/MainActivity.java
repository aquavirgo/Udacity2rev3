package base.movie.popmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import base.movie.popmovie.adapter.RecViewAdapter;
import base.movie.popmovie.asynctask.DownJSON;
import base.movie.popmovie.asynctask.FavoritesAsyncTask;

public class MainActivity extends AppCompatActivity implements RecViewAdapter.ListItemClickListener{


    RecyclerView recyclerView;
    Context context;
    public  static RecyclerView.Adapter recyclerView_Adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    public static  Toast toast;
    static public ArrayList<base.movie.popmovie.Movie> moviesList;
    static public ArrayList<String> images;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    Parcelable mListState;
    Bundle  saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.rv_numbers);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerViewLayoutManager = new GridLayoutManager(this, 2);
        }else{
            recyclerViewLayoutManager = new GridLayoutManager(this, 4);
        }

        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        moviesList = new ArrayList<Movie>();
        images = new ArrayList<String>();

       // updateMovies();

       // Log.d("IMAGE_PATH",images.toString());

      // recyclerView_Adapter = new RecViewAdapter(context,this,images);

//        recyclerView.setAdapter(recyclerView_Adapter);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            Log.v("onCreate ",mListState.toString());
            recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }
        toast = Toast.makeText(MainActivity.this,"", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve list state and list/item positions
            mListState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
        Log.v("My_Tag", "onRestoreInstanceState called");
        Log.v("My_Tag", "mListState in onRestoreInstanceState is " + mListState);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.v("Resume ", String.valueOf(mListState));
     //   SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //String selectBy = sharedPrefs.getString(getString(R.string.main_activity_pref_sorting_criteria_key), getString(R.string.main_activity_pref_sorting_criteria_default_value));

        moviesList = new ArrayList<Movie>();
        images = new ArrayList<String>();


        updateMovies();
        recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
    }

    public void updateMovies() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String selectBy = sharedPrefs.getString(getString(R.string.main_activity_pref_sorting_criteria_key), getString(R.string.main_activity_pref_sorting_criteria_default_value));
        if(selectBy.equals(getString(R.string.favorites))){
             new FavoritesAsyncTask(context).execute(selectBy,null);
        }else {
            new DownJSON(context).execute(selectBy, null);
        }


   recyclerView_Adapter = new RecViewAdapter(context,this,images);
   recyclerView.setAdapter(recyclerView_Adapter);


    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(MainActivity.this, MovieOverview.class);
        intent.putExtra(getString(R.string.position),clickedItemIndex);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingPref.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        // Save list state
        mListState = new Bundle();
        mListState = recyclerView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putParcelable(KEY_RECYCLER_STATE, mListState);
        Log.v("onSave ",mListState.toString());

        super.onSaveInstanceState(savedInstanceState);
    }

/*
    @Override
    protected void onPause(){
       mListState = new Bundle();
        mListState = recyclerView.getLayoutManager().onSaveInstanceState();

       saved.putParcelable(KEY_RECYCLER_STATE, mListState);
        Log.v("PAUSE", String.valueOf(mListState));
        super.onPause();
    }
*/
}
