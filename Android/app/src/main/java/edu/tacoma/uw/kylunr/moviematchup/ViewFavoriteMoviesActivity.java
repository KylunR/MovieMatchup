package edu.tacoma.uw.kylunr.moviematchup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.kylunr.moviematchup.data.FavoriteList;
import edu.tacoma.uw.kylunr.moviematchup.data.Movie;
import edu.tacoma.uw.kylunr.moviematchup.data.RecommendationItem;
import edu.tacoma.uw.kylunr.moviematchup.data.RecyclerViewAdapter;
import edu.tacoma.uw.kylunr.moviematchup.data.User;
import edu.tacoma.uw.kylunr.moviematchup.data.WatchList;

import static android.content.ContentValues.TAG;

public class ViewFavoriteMoviesActivity extends AppCompatActivity {

    private User user;
    private List<RecommendationItem> recommendationItemList;
    private FavoriteList favoriteList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        setTitle("Your Ranked Movie List");

        getUserData();
    }

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
                        Log.e(TAG, "DocumentSnapshot data: " + document.getData());

                        String watchListString = document.getString("Favorite List");
                        favoriteList.parseString(watchListString);

                        user.setFavoriteList(favoriteList);

                        List<Movie> movieList = favoriteList.getList();
                        Log.e("TEST", "" + movieList.toString());
                        recommendationItemList = new ArrayList<>();
                        int i = 1;

                        String shareString = "My Favorites Movies: \n";

                        for (Movie movie : movieList) {
                            recommendationItemList.add(new RecommendationItem(i + ".", movie.getTitle(), movie.getPosterURL()));

                            if (i < 11) {
                                shareString += i + ". " + movie.getTitle() + "\n";
                            }

                            i++;
                        }

                        setUpRecyclerView();

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

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerViewAdapter(recommendationItemList, ViewFavoriteMoviesActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}