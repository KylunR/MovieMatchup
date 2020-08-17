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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.kylunr.moviematchup.data.Movie;

public class Matchup extends AppCompatActivity {

    private List<Movie> moviePool;
    protected Movie choiceA;
    protected Movie choiceB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchup);
        getPoplarMovies();
    }

    protected void setValues() {
        choiceA = getRandomMovie();
        choiceB = getRandomMovie();

        while (choiceA.equals(choiceB)) {
            choiceB = getRandomMovie();
        }

        Button aButton = (Button) findViewById(R.id.choicea_button);
        Button bButton = (Button) findViewById(R.id.choiceb_button);
        aButton.setText(choiceA.getTitle());
        bButton.setText(choiceB.getTitle());
    }

    protected void setPosterA() {
        new DownloadImageTask((ImageView) findViewById(R.id.choicea_imageview))
                .execute(choiceA.getPosterURL());

        setPosterB();
    }

    protected void setPosterB() {
        new DownloadImageTask((ImageView) findViewById(R.id.choiceb_imageview))
                .execute(choiceB.getPosterURL());
    }

    protected Movie getRandomMovie() {
        int length = moviePool.size();
        int randomNumber = (int) (Math.random() * length);

        return moviePool.get(randomNumber);
    }

    /**
     * Used to complete a search for movies, calls an AsyncTask
     * to complete connection and GET request
     *
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

                setValues();
                setPosterA();

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView image;

        public DownloadImageTask(ImageView bmImage) {
            this.image = bmImage;
        }

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

        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
        }
    }
}