package edu.tacoma.uw.kylunr.moviematchup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.tacoma.uw.kylunr.moviematchup.data.Movie;
import edu.tacoma.uw.kylunr.moviematchup.data.RecyclerViewItem;
import edu.tacoma.uw.kylunr.moviematchup.data.RecyclerViewAdapter;
import edu.tacoma.uw.kylunr.moviematchup.data.User;
import edu.tacoma.uw.kylunr.moviematchup.data.WatchList;

import static android.content.ContentValues.TAG;

/**
 * This class represents the RecommendationsActivity. This activty
 * displays movie recommendations to the viewer based on movies they
 * have not seen
 */
public class RecommendationsActivity extends AppCompatActivity {

    private User user;
    private List<RecyclerViewItem> recyclerViewItemList;
    private WatchList watchList;

    /**
     * Sets layout and calls function to getUserData
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);
        setTitle("Recommended Movies");

        getUserData();
    }

    /**
     * Gets user data from the database, once data is retrieved
     * the data is used to create a recommendations list to be used
     * in creating a recyclerview
     */
    private void getUserData() {
        user = new User();
        // Get email
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = firebaseUser.getEmail();
        // Set email
        user.setEmail(email);
        watchList = new WatchList();

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

                        // Take data and parse it
                        String watchListString = document.getString("Watch List");
                        watchList.parseString(watchListString);
                        user.setWatchList(watchList);

                        // Get watch list for recommendations
                        List<Movie> movieList = watchList.getWatchList();
                        Collections.sort(movieList, Collections.reverseOrder());
                        recyclerViewItemList = new ArrayList<>();
                        int i = 1;

                        // For every movie in the watchlist
                        // Add it to the recommendations list for the RecyclerView
                        for (Movie movie : movieList) {
                            recyclerViewItemList.add(new RecyclerViewItem(i + ".", movie.getTitle(), movie.getPosterURL()));
                            i++;
                        }

                        setUpRecyclerView();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * Sets up the RecyclerView using the data retrieved from
     * the database
     */
    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new RecyclerViewAdapter(recyclerViewItemList, RecommendationsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}