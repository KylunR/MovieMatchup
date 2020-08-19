package edu.tacoma.uw.kylunr.moviematchup.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * This class represents an AsyncTask to download an movie poster
 * from an external API
 */
public class PosterDownloader extends AsyncTask<String, Void, Bitmap> {

    public PosterDownloader(ImageView bmImage, Context context) {
        this.image = bmImage;
        this.context = context;
    }

    ImageView image;
    Context context;

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
