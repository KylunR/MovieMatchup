package edu.tacoma.uw.kylunr.moviematchup;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class ViewFavoriteMoviesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);

        // TODO: Test Test Test Test
        new DownloadImageTask((ImageView) findViewById(R.id.poster_image))
                .execute("https://image.tmdb.org/t/p/w185/6FfCtAuVAW8XJjZ7eWeLibRLWTw.jpg");
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