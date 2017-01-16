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
import static base.movie.popmovie.DownJSON.DEF_IMAGE;
import static base.movie.popmovie.DownJSON.DEF_IMAGE_SIZE;
import static base.movie.popmovie.DownJSON.IMAGE_URL;

public class MovieOverview extends AppCompatActivity {

    Movie movie;
    String posterUrl;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_overview);

        int moviePosition = getIntent().getIntExtra("position",0);
        movie= MainActivity.moviesList.get(moviePosition);

       TextView movieTitle = (TextView)findViewById(R.id.title);
        movieTitle.setText(movie.getOriginalTitle());

        TextView rating = (TextView)findViewById(R.id.rating);
        rating.setText(movie.getVoteAverage()+"/10");

        TextView release = (TextView)findViewById(R.id.release);
        release.setText(movie.getReleaseDate());

        TextView synopsis = (TextView)findViewById(R.id.synopsis);
        synopsis.setText(movie.getOverview());

       ImageView thumbnail = (ImageView) findViewById(R.id.thumbnail);

        if (movie.getPosterPath() == DownJSON.DEF_IMAGE) {
            posterUrl = DownJSON.DEF_IMAGE;
        }else {
            posterUrl = DownJSON.IMAGE_URL + DownJSON.DEF_IMAGE_SIZE + "/" + movie.getPosterPath();
        }

        Picasso.with(mContext).load(posterUrl).into(thumbnail);
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
