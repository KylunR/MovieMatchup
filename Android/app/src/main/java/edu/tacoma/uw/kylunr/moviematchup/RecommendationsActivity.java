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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

public class RecommendationsActivity extends AppCompatActivity {

    private User user;
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> posterList = new ArrayList<>();
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
        setUserData();
        setUpRecyclerView();
    }

    private void getUserData() {
        user = new User();
        // Get email
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = firebaseUser.getEmail();
        // Set email
        user.setEmail(email);
        // Update Data for user
        user.getData();
    }

    private void setUserData() {
        // Get current watch list
        watchList = new WatchList(user.getWatchList());
        List<Movie> movieList = watchList.getWatchList();
        Log.e("TEST", "" + movieList.toString());
        recommendationItemList = new ArrayList<>();
        int i = 1;

        for (Movie movie : movieList) {
            recommendationItemList.add(new RecommendationItem(i + ".", movie.getTitle(), movie.getPosterURL()));
            i++;
        }
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