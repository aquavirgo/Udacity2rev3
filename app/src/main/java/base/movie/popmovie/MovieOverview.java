package base.movie.popmovie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.security.AccessController;

import static android.R.attr.data;
import static android.os.Build.VERSION_CODES.M;
import static base.movie.popmovie.DownJSON.IMAGE_NOT_FOUND;
import static base.movie.popmovie.DownJSON.IMAGE_SIZE_185;
import static base.movie.popmovie.DownJSON.IMAGE_URL;

public class MovieOverview extends AppCompatActivity {

    Movie movie;
    String movie_poster_url;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_overview);

        int movie_position = getIntent().getIntExtra("movie_position",0);
        movie= MainActivity.moviesList.get(movie_position);

       TextView movie_title = (TextView)findViewById(R.id.title);
        movie_title.setText(movie.getOriginal_title());

        TextView rating = (TextView)findViewById(R.id.rating);
        rating.setText(movie.getVote_average()+"/10");

        TextView release = (TextView)findViewById(R.id.release);
        release.setText(movie.getRelease_date());

        TextView synopsis = (TextView)findViewById(R.id.synopsis);
        synopsis.setText(movie.getOverview());

       ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);

        if (movie.getPoster_path() == DownJSON.IMAGE_NOT_FOUND) {
            movie_poster_url = DownJSON.IMAGE_NOT_FOUND;
        }else {
            movie_poster_url = DownJSON.IMAGE_URL + DownJSON.IMAGE_SIZE_185 + "/" + movie.getPoster_path();
        }

        Picasso.with(mContext).load(movie_poster_url).into(thumbnail);
        synopsis.setVisibility(View.VISIBLE);

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
