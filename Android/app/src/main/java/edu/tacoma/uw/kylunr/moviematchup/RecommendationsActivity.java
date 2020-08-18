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
import edu.tacoma.uw.kylunr.moviematchup.data.User;
import edu.tacoma.uw.kylunr.moviematchup.data.WatchList;

public class RecommendationsActivity extends AppCompatActivity {

    private User user;
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> posterList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        user = new User();

        // Get email
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = firebaseUser.getEmail();
        // Set email
        user.setEmail(email);
        // Update Data for user
        user.getData();
        // Get current watch list
        WatchList watchList = new WatchList(user.getWatchList());
        List<Movie> movieList = watchList.getWatchList();

        for (Movie movie : movieList) {
            titleList.add(movie.getTitle());
            posterList.add(movie.getPosterURL());
        }

        recyclerView = findViewById(R.id.recycler_view);
        assert recyclerView != null;
        startRecyclerView(recyclerView);
    }

    private void startRecyclerView(@NonNull RecyclerView recyclerView) {
        if(titleList != null) {
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, titleList, posterList));
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private RecommendationsActivity parentActivity;
        private ArrayList<String> titleList = new ArrayList<>();
        private ArrayList<String> posterList = new ArrayList<>();
        private Context context;

        public SimpleItemRecyclerViewAdapter(RecommendationsActivity parent, ArrayList<String> titleList,
                                             ArrayList<String> posterList) {
            this.parentActivity = parent;
            this.titleList = titleList;
            this.posterList = posterList;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_listitem, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            // new DownloadImageTask((ImageView) holder.poster)
            //        .execute(posterList.get(position));

            holder.title.setText(titleList.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return titleList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView poster;
            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                poster = itemView.findViewById(R.id.poster);
                title = itemView.findViewById(R.id.title_view);
            }
        }
    }

    /**
     * This class represents an AsyncTask to download an movie poster
     * from an external API
     */
    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        public DownloadImageTask(ImageView bmImage) {
            this.image = bmImage;
        }

        ImageView image;

        /**
         * Connects to the API's URL and begins decoding the
         * image into a bitmap
         *
         * @param urls  - url of movie poster
         * @return  bitmap representation of the poster
         */
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

        /**
         * Sets the result from doInBackground as the image
         * source (bitmap)
         *
         * @param result
         */
        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
        }
    }
}