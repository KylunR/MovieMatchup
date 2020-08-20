package edu.tacoma.uw.kylunr.moviematchup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.kylunr.moviematchup.data.FavoriteList;
import edu.tacoma.uw.kylunr.moviematchup.data.Movie;
import edu.tacoma.uw.kylunr.moviematchup.data.RecyclerViewItem;
import edu.tacoma.uw.kylunr.moviematchup.data.RecyclerViewAdapter;
import edu.tacoma.uw.kylunr.moviematchup.data.User;

import static android.content.ContentValues.TAG;


/**
 * This class represents the activity View Favorite Movies.
 * This activity displays the viewer's ranked list in order and
 * gives the viewer the ability to share their top-ten movies
 */
public class ViewFavoriteMoviesActivity extends AppCompatActivity {

    private User user;
    private List<RecyclerViewItem> recyclerViewItemList;
    private FavoriteList favoriteList;

    /**
     * Sets layout and calls function to
     * retrieve user data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        setTitle("Your Ranked Movie List");

        getUserData();
    }

    /**
     * Retrieves the user data from the database
     * and creates a list to be used for the RecyclerView
     */
    private void getUserData() {
        user = new User();
        // Get email
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = firebaseUser.getEmail();
        // Set email
        user.setEmail(email);
        favoriteList = new FavoriteList();

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
                        // Once data is retrieved
                        String watchListString = document.getString("Favorite List");

                        // Parse data
                        favoriteList.parseString(watchListString);
                        user.setFavoriteList(favoriteList);
                        List<Movie> movieList = favoriteList.getList();


                        recyclerViewItemList = new ArrayList<>();
                        int i = 1;

                        String shareString = "My Favorites Movies: \n";

                        // For every movie in favorite list
                        for (Movie movie : movieList) {
                            // Add it to recyclerview list
                            recyclerViewItemList.add(new RecyclerViewItem(i + ".", movie.getTitle(), movie.getPosterURL()));

                            // Add first ten to string for sharing
                            if (i < 11) {
                                shareString += i + ". " + movie.getTitle() + "\n";
                            }
                            i++;
                        }

                        // Set up recycler view
                        setUpRecyclerView();

                        // Create button for sharing top ten
                        Button button = (Button) findViewById(R.id.share_button);
                        final String finalShareString = shareString;
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, finalShareString);
                                sendIntent.setType("text/plain");

                                Intent shareIntent = Intent.createChooser(sendIntent, null);
                                startActivity(shareIntent);
                            }
                        });
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
     * Sets up the recycler view using the list
     * generating from the database data
     */
    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new RecyclerViewAdapter(recyclerViewItemList, ViewFavoriteMoviesActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}