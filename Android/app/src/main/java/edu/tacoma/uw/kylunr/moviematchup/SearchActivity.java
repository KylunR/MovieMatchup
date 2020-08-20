package edu.tacoma.uw.kylunr.moviematchup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.kylunr.moviematchup.data.FavoriteList;
import edu.tacoma.uw.kylunr.moviematchup.data.Movie;
import edu.tacoma.uw.kylunr.moviematchup.data.RecommendationItem;
import edu.tacoma.uw.kylunr.moviematchup.data.RecyclerTouchListener;
import edu.tacoma.uw.kylunr.moviematchup.data.RecyclerViewAdapter;
import edu.tacoma.uw.kylunr.moviematchup.data.User;
import edu.tacoma.uw.kylunr.moviematchup.data.WatchList;

import static android.content.ContentValues.TAG;


/**
 * Search represents an activity to search for a
 * specific movie
 */
public class SearchActivity extends AppCompatActivity {

    // List of results from search
    private List<Movie> searchList;
    private User user;

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
        setTitle("Search");

        Button button = (Button) findViewById(R.id.search_button);
        final EditText query = (EditText) findViewById(R.id.search_bar);

        // Listener for search button
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
     * Notifies the user that the selected movie
     * was added to their list
     */
    private void addedMovie() {
        searchList.clear();
        Toast.makeText(this, "Added movie to list", Toast.LENGTH_SHORT).show();
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

                    if (searchList != null) {

                        // Create a new list for the recycler view
                        List<RecommendationItem> recommendationItemList = new ArrayList<>();

                        // For every movie in the search query
                        // Add it to the recylcer view list
                        for (Movie movie: searchList) {
                            recommendationItemList.add(new RecommendationItem("", movie.getTitle(), movie.getPosterURL()));
                        }

                        // Set up recycler view
                        RecyclerView recyclerView = findViewById(R.id.recycler_view);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                        RecyclerView.Adapter adapter = new RecyclerViewAdapter(recommendationItemList, SearchActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);

                        // Add listener to recycler view
                        recyclerView.addOnItemTouchListener(
                                new RecyclerTouchListener(getApplicationContext(),
                                        recyclerView, new RecyclerTouchListener.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        // Get movie that was clicked
                                        final Movie movieToAdd = searchList.get(position);

                                        // Update user and retrieve data from database
                                        user = new User();
                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        String email = firebaseUser.getEmail();
                                        user.setEmail(email);

                                        final FavoriteList favoriteList = new FavoriteList();
                                        final WatchList watchList = new WatchList();

                                        // Get database instance
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        // Get user instance
                                        DocumentReference docRef = db.collection("users").document(user.getEmail());

                                        // Retrieve data
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    // If user exists
                                                    if (document.exists()) {
                                                        Log.e(TAG, "DocumentSnapshot data: " + document.getData());

                                                        // Get data from database
                                                        String favoriteListString = document.getString("Favorite List");
                                                        String watchListString = document.getString("Watch List");
                                                        favoriteList.parseString(favoriteListString);
                                                        watchList.parseString(watchListString);

                                                        // Add selected movie to favorite list
                                                        favoriteList.addMovie(movieToAdd);

                                                        // Push data to database
                                                        user.setFavoriteList(favoriteList);
                                                        user.setWatchList(watchList);
                                                        user.pushData();

                                                        // Notify user that the movie was added
                                                        addedMovie();
                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });
                                    }
                                    @Override
                                    public void onLongClick(View view, int position) { }
                                }));
                    } else { }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}