package base.movie.popmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
   // static public RecViewAdapter recViewAdapter;
    public static  Toast toast, mToast;
    static public ArrayList<base.movie.popmovie.Movie> moviesList;
    static public ArrayList<String> images;
    static public String lastSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.rv_numbers);

        //Change 2 to your choice because here 2 is the number of Grid layout Columns in each row.
        recyclerViewLayoutManager = new GridLayoutManager(this, 2);


        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        moviesList = new ArrayList<Movie>();
        images = new ArrayList<String>();


        updateMovies();


      //  Log.d("MA",images.toString());
      //  Log.d("TEST",images.get(0));

        recyclerView_Adapter = new RecViewAdapter(context,this,images);

        recyclerView.setAdapter(recyclerView_Adapter);
        toast = Toast.makeText(MainActivity.this,"", Toast.LENGTH_SHORT);


    }

    @Override
    public void onResume() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortingCriteria = sharedPrefs.getString(getString(R.string.pref_sorting_criteria_key), getString(R.string.pref_sorting_criteria_default_value));

        if(lastSortOrder!= null && !sortingCriteria.equals(lastSortOrder)){
            moviesList = new ArrayList<Movie>();
            images = new ArrayList<String>();
            updateMovies();
        }
        lastSortOrder = sortingCriteria;
        super.onResume();

    }



    public void updateMovies() {
        //String sortingCriteria = "popularity";
       SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortingCriteria = sharedPrefs.getString(getString(R.string.pref_sorting_criteria_key), getString(R.string.pref_sorting_criteria_default_value));

        new DownJSON().execute(sortingCriteria, null);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mToast != null) {
            mToast.cancel();
        }

        // COMPLETED (12) Show a Toast when an item is clicked, displaying that item number that was clicked
        /*
         * Create a Toast and store it in our Toast field.
         * The Toast that shows up will have a message similar to the following:
         *
         *                     Item #42 clicked.
         */
        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mToast.show();

        Intent intent = new Intent(MainActivity.this, MovieOverview.class);
        intent.putExtra("movie_position",clickedItemIndex);
        startActivity(intent);

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

}
