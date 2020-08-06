package edu.tacoma.uw.kylunr.moviematchup.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *  Movie class represents a Movie that holds
 *  values for the title of the movies and a url to retrieve the
 *  movie's poster
 */
public class Movie {

    private String title;
    private String posterURL;

    public static final String TITLE = "title";
    public static final String POSTER = "poster_path";

    public Movie(String title, String posterURL) {
        this.title = title;
        this.posterURL = posterURL;
    }

    /**
     * Overrides toString method
     * String includes movie title
     *
     * @return  String - movie title
     */
    @Override
    public String toString() {
        return this.title;
    }

    /**
     * Takes in a JSON string from a GET request
     * Parses the JSON and creates movie objects
     * Adds the movie object to a list and returns
     * the list
     *
     * @param movieJson        - JSON string from API
     * @return List<Movie>     - Movie list
     * @throws JSONException
     */
    public static List<Movie> parseMovieJson(String movieJson) throws JSONException {

        List<Movie> movieList = new ArrayList<>();

        if (movieJson != null) {

            JSONArray arr = new JSONArray(movieJson);

            Log.e("e", "" + arr.toString());

            for (int i = 0; i < arr.length(); i++) {

                JSONObject obj = arr.getJSONObject(i);

                Movie movie = new Movie(obj.getString(TITLE),
                        "http://image.tmdb.org/t/p/w154" + obj.getString(POSTER));

                Log.e("e", "" + movie.toString());

                movieList.add(movie);
            }
        }

        Log.e("e", "" + movieList.toString());
        return movieList;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPosterURL() {
        return this.posterURL;
    }
}
