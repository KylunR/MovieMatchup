package edu.tacoma.uw.kylunr.moviematchup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


/**
 * This class represents the launcher activity in which the user
 * be prompted to sign-in or sign-up.  Once logged-in this activity
 * will show the user all other available activities.
 */
public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 9118;
    private List<AuthUI.IdpConfig> providers;
    private Button signout_button;
    private Button search_button;
    private Button viewlist_button;
    private Button matchup_button;
    private Button viewtowatch_button;
    private ImageView app_logo;


    /**
     * Creates buttons for other features (search, signout)
     * and hides them from view.  Then calls showSignInOptions()
     * to allow the user to sign-in.  Once logged in the user
     * can view the available features.  Sign-out will hide all
     * features and show sign-in options again.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get app logo
        app_logo = (ImageView) findViewById(R.id.logo);

        // Sign-out button and behavior
        signout_button = (Button) findViewById(R.id.signout_button);
        signout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logout
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Once signed-out hide button
                                signout_button.setEnabled(false);
                                showSignInOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                });
            }
        });

        // Search button
        search_button = (Button) findViewById(R.id.goToSearch_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        // View Favorite Movies button
        viewlist_button = (Button) findViewById(R.id.goToViewList_button);
        viewlist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewFavoriteMoviesActivity.class);
                startActivity(intent);
            }
        });

        // Movie Matchup button
        matchup_button = (Button) findViewById(R.id.goToMatchup_button);
        matchup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MatchupActivity.class);
                startActivity(intent);
            }
        });

        // To Watch / Recommendation button
        viewtowatch_button = (Button) findViewById(R.id.goToWatchList_button);
        viewtowatch_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecommendationsActivity.class);
                startActivity(intent);
            }
        });

        // Initialize sign-in options
        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),        // Phone Builder
                new AuthUI.IdpConfig.EmailBuilder().build(),        // Email Builder
                new AuthUI.IdpConfig.GoogleBuilder().build()        // Google Builder
        );

        showSignInOptions();
    }

    /**
     *  Gets the sign in options (phone, email, and Google)
     *  and displays them on screen
     */
    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.AppTheme)
                .build(), MY_REQUEST_CODE
        );
    }

    /**
     * Uses Firebase to authenticate the user with their chose
     * sign-in option.  After sign-in is complete other features
     * are shown to user.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Show email on Toast
                Toast.makeText(this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();
                // Set Button visibility
                signout_button.setEnabled(true);
                search_button.setEnabled(true);
                viewlist_button.setEnabled(true);
                matchup_button.setEnabled(true);
                viewtowatch_button.setEnabled(true);
                app_logo.setEnabled(true);

            } else {
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}