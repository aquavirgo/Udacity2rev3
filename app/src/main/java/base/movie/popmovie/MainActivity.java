package base.movie.popmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import static android.R.attr.x;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity implements RecViewAdapter.ListItemClickListener{


    RecyclerView recyclerView;
    Context context;
    public  static RecyclerView.Adapter recyclerView_Adapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    public static  Toast toast;
    static public ArrayList<base.movie.popmovie.Movie> moviesList;
    static public ArrayList<String> images;
    static public String lastSelect;

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


        updateMovies();


        recyclerView_Adapter = new RecViewAdapter(context,this,images);

        recyclerView.setAdapter(recyclerView_Adapter);
        toast = Toast.makeText(MainActivity.this,"", Toast.LENGTH_SHORT);


    }

    @Override
    public void onResume() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String selectBy = sharedPrefs.getString(getString(R.string.main_activity_pref_sorting_criteria_key), getString(R.string.main_activity_pref_sorting_criteria_default_value));

        if(lastSelect!= null && !selectBy.equals(lastSelect)){
            moviesList = new ArrayList<Movie>();
            images = new ArrayList<String>();
            updateMovies();
        }
        lastSelect = selectBy;
        super.onResume();

    }



    public void updateMovies() {
        //String sortingCriteria = "popularity";
       SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String selectBy = sharedPrefs.getString(getString(R.string.main_activity_pref_sorting_criteria_key), getString(R.string.main_activity_pref_sorting_criteria_default_value));

        new DownJSON(context).execute(selectBy, null);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(MainActivity.this, MovieOverview.class);
        intent.putExtra("position",clickedItemIndex);
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

}
