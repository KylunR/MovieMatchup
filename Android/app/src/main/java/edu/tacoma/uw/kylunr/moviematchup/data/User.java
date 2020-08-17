package edu.tacoma.uw.kylunr.moviematchup.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class User {

    private String email;
    private FavoriteList favoriteList;
    private WatchList watchList;

    public User() {
        this.favoriteList = new FavoriteList();
        this.watchList = new WatchList();
    }

    public User(String email, FavoriteList favoriteList, WatchList watchList) {
        this.email = email;
        this.favoriteList = favoriteList;
        this.watchList = watchList;
    }

    public void pushData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("Email", this.email);
        user.put("Favorite List", favoriteList.toString());
        user.put("Watch List", watchList.toString());

        db.collection("users").document(this.email)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "DocumentSnapshot successfully written");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error writing document", e);
                    }
                });
    }

    public void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(this.email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.e(TAG, "DocumentSnapshot data: " + document.getData());

                        String favoriteListString = document.getString("Favorite List");
                        favoriteList.parseString(favoriteListString);

                        String watchListString = document.getString("Watch List");
                        watchList.parseString(watchListString);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    public String getEmail() {
        return this.email;
    }

    public FavoriteList getFavoriteList() {
        return this.favoriteList;
    }

    public WatchList getWatchList() {
        return this.watchList;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFavoriteList(FavoriteList favoriteList) {
        this.favoriteList = favoriteList;
    }

    public void setWatchList(WatchList watchList) {
        this.watchList = watchList;
    }
}
