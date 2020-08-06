package edu.tacoma.uw.kylunr.moviematchup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.kylunr.moviematchup.data.Movie;


/**
 * Search represents an activity to search for a
 * specific movie
 */
public class Search extends AppCompatActivity {

    private List<Movie> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button button = (Button) findViewById(R.id.search_button);
        final EditText query = (EditText) findViewById(R.id.search_bar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMovies(query.getText().toString());
            }
        });
    }

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

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    Log.e("e", "Made Connection!");

                    InputStream content = urlConnection.getInputStream();
                    Log.e("e", "Made Input Stream!");

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

        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(),  s, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                Log.e("e", "Parsing JSON!");
                JSONObject jsonObject = new JSONObject(s);

                    searchList = Movie.parseMovieJson(jsonObject.getString("data"));

                    TextView textView = (TextView) findViewById(R.id.textview);
                    textView.setMovementMethod(new ScrollingMovementMethod());

                    if (searchList != null) {
                        String results = "";

                        for (Movie movie: searchList) {
                            // Add data to text view
                            results += movie.toString() + "\n";
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