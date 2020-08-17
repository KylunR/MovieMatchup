package edu.tacoma.uw.kylunr.moviematchup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.kylunr.moviematchup.data.Movie;


/**
 * Search represents an activity to search for a
 * specific movie
 */
public class Search extends AppCompatActivity {

    // List of results from search
    private List<Movie> searchList;

    /**
     * Displays the search bar and button to the viewer.
     * Creates a listener for the button.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button button = (Button) findViewById(R.id.search_button);
        final EditText query = (EditText) findViewById(R.id.search_bar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If clicked, search with parameters
                searchMovies(query.getText().toString());
            }
        });
    }


    /**
     * When resumed, if there are results
     * clear them
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (searchList != null ) {
            searchList.clear();
        }
    }

    /**
     * Used to complete a search for movies, calls an AsyncTask
     * to complete connection and GET request
     *
     * @param query - the "search" or text to search for
     */
    protected void searchMovies(String query) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new MoviesTask().execute((getString(R.string.search_movies)) + query);
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
                    searchList = Movie.parseMovieJson(jsonObject.getString("data"));

                    // Add search results to screen
                    TextView textView = (TextView) findViewById(R.id.textview);
                    textView.setMovementMethod(new ScrollingMovementMethod());

                    if (searchList != null) {
                        String results = "";

                        for (Movie movie: searchList) {
                            // Add data to text view
                            results += movie.getTitle() + "\n";
                        }

                        textView.setText(results);
                    } else {
                        textView.setText("");
                        textView.append("No results");
                    }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}