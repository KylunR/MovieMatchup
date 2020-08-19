package edu.tacoma.uw.kylunr.moviematchup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.concurrent.atomic.AtomicBoolean;

import edu.tacoma.uw.kylunr.moviematchup.data.FavoriteList;
import edu.tacoma.uw.kylunr.moviematchup.data.Movie;
import edu.tacoma.uw.kylunr.moviematchup.data.RecommendationItem;
import edu.tacoma.uw.kylunr.moviematchup.data.RecyclerViewAdapter;
import edu.tacoma.uw.kylunr.moviematchup.data.User;
import edu.tacoma.uw.kylunr.moviematchup.data.WatchList;

import static android.content.ContentValues.TAG;

public class RecommendationsActivity extends AppCompatActivity {

    private User user;
    private List<RecommendationItem> recommendationItemList;
    private WatchList watchList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        getUserData();
    }

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

                        String watchListString = document.getString("Watch List");
                        watchList.parseString(watchListString);

                        user.setWatchList(watchList);

                        List<Movie> movieList = watchList.getWatchList();
                        Log.e("TEST", "" + movieList.toString());
                        recommendationItemList = new ArrayList<>();
                        int i = 1;

                        for (Movie movie : movieList) {
                            recommendationItemList.add(new RecommendationItem(i + ".", movie.getTitle(), movie.getPosterURL()));
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

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerViewAdapter(recommendationItemList, RecommendationsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        Log.e("TEST", "" + recommendationItemList.toString());
    }
}