package edu.tacoma.uw.kylunr.moviematchup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.kylunr.moviematchup.data.FavoriteList;
import edu.tacoma.uw.kylunr.moviematchup.data.Movie;
import edu.tacoma.uw.kylunr.moviematchup.data.User;
import edu.tacoma.uw.kylunr.moviematchup.data.WatchList;

/**
 * This class represents the Matchup Activity.
 * This activity presents the viewer with the choice
 * of two movies.  The user is able to choose either
 * movie or select that they have not seen either movie.
 */
public class MatchupActivity extends AppCompatActivity {

    private List<Movie> moviePool;
    protected Movie choiceA;
    protected Movie choiceB;

    /**
     * Gets user data and sets up buttons with listeners
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchup);
        getPoplarMovies();

        final User user = new User();
        // Get email
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = firebaseUser.getEmail();
        // Set email
        user.setEmail(email);
        // Update Data for user
        user.getData();
        // Get current favorite list
        final FavoriteList favoriteList = new FavoriteList(user.getFavoriteList());
        // Get current watch list
        final WatchList watchList = new WatchList(user.getWatchList());


        // Button for movie choice A
        Button aButton = (Button) findViewById(R.id.choicea_button);
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Test", "Choice A");
                favoriteList.matchupResult(choiceA, choiceB);
                getPoplarMovies();
            }
        });

        // Button for movie choice B
        Button bButton = (Button) findViewById(R.id.choiceb_button);
        bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Test", "Choice B");
                favoriteList.matchupResult(choiceB, choiceA);
                // Set user's favorite list
                user.setFavoriteList(favoriteList);
                // Set user's watch list
                user.setWatchList(watchList);
                // Update user data
                user.pushData();
                getPoplarMovies();
            }
        });

        // Button for haven't seen movie choice A
        Button aNotSeenButton = (Button) findViewById(R.id.choiceahavenotseen_button);
        aNotSeenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Test", "Have not seen Choice A");
                watchList.addMovie(choiceA);
                // Set user's favorite list
                user.setFavoriteList(favoriteList);
                // Set user's watch list
                user.setWatchList(watchList);
                // Update user data
                user.pushData();
                getPoplarMovies();
            }
        });

        // Button for haven't seen movie choice B
        Button bNotSeenButton = (Button) findViewById(R.id.choicebhavenotseen_button);
        bNotSeenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Test", "Have not seen Choice B");
                watchList.addMovie(choiceB);
                // Set user's favorite list
                user.setFavoriteList(favoriteList);
                // Set user's watch list
                user.setWatchList(watchList);
                // Update user data
                user.pushData();
                getPoplarMovies();
            }
        });

        // Button for haven't seen movie choice B
        Button notSeenEither = (Button) findViewById(R.id.havenotseeneither_button);
        notSeenEither.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Test", "Have not seen either");
                watchList.addMovie(choiceA);
                watchList.addMovie(choiceB);
                // Set user's favorite list
                user.setFavoriteList(favoriteList);
                // Set user's watch list
                user.setWatchList(watchList);
                // Update user data
                user.pushData();
                getPoplarMovies();
            }
        });
    }

    /**
     * Sets the value of the first movie option
     */
    protected void setValueA() {
        choiceA = getRandomMovie();

        // If B is already set and A is equal to B
        // Get a new movie for B
        //while (choiceB != null && choiceA.equals(choiceB)) {
        //    choiceB = getRandomMovie();
        //}

        Button aButton = (Button) findViewById(R.id.choicea_button);
        aButton.setText(choiceA.getTitle());
    }

    /**
     * Sets the value of the second movie option
     */
    protected void setValueB() {
        choiceB = getRandomMovie();

        // If A is already set and A is equal to B
        // Get a new movie for A
        while (choiceA != null && choiceA.equals(choiceB)) {
            choiceA = getRandomMovie();
        }

        Button bButton = (Button) findViewById(R.id.choiceb_button);
        bButton.setText(choiceB.getTitle());
    }

    /**
     * Sets the poster for the first movie option
     */
    protected void setPosterA() {
        new DownloadImageTask((ImageView) findViewById(R.id.choicea_imageview))
                .execute(choiceA.getPosterURL());

        setPosterB();
    }

    /**
     * Sets the poster for the second movie option
     */
    protected void setPosterB() {
        new DownloadImageTask((ImageView) findViewById(R.id.choiceb_imageview))
                .execute(choiceB.getPosterURL());
    }

    /**
     * Returns a random movie from the GET request
     *
     * @return - Movie object
     */
    protected Movie getRandomMovie() {
        int length = moviePool.size();
        int randomNumber = (int) (Math.random() * length);

        return moviePool.get(randomNumber);
    }

    /**
     * Used to complete a search for movies, calls an AsyncTask
     * to complete connection and GET request
     */
    protected void getPoplarMovies() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new MoviesTask().execute((getString(R.string.toprated_movies)));
        }
    }

    /**
     * Async Task to complete a connection and GET request
     * to the API in order to complete the search
     */
    private class MoviesTask extends AsyncTask<String, Void, String> {

        /**
         * Makes a GET request to the argument URL.
         * Records the response from the request and
         * returns it in a string.
         *
         * @param urls - API to be connected to
         * @return response - JSON response from API
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download the list of courses, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            Log.e("e", "Returned response!");
            return response;
        }

        /**
         * Takes the response from the GET request
         * and creates a JSON object for it.  The JSON object
         * is passed to the movie class for parsing.  The parsed
         * data is then printed to the screen.
         *
         * @param s - response from GET request
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(),  s, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                // Parse JSON
                moviePool = Movie.parseMovieJson(jsonObject.getString("data"));

                // Set movie values
                setValueA();
                setValueB();
                setPosterA();

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This class represents an AsyncTask to download an movie poster
     * from an external API
     */
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        public DownloadImageTask(ImageView bmImage) {
            this.image = bmImage;
        }

        ImageView image;

        /**
         * Connects to the API's URL and begins decoding the
         * image into a bitmap
         *
         * @param urls  - url of movie poster
         * @return  bitmap representation of the poster
         */
        protected Bitmap doInBackground(String... urls) {

            String urldisplay = urls[0];
            Bitmap bitmap = null;

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        /**
         * Sets the result from doInBackground as the image
         * source (bitmap)
         *
         * @param result
         */
        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
        }
    }
}