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
    private int id;
    private int voteCount;

    public static final String TITLE = "title";
    public static final String POSTER = "poster_path";
    public static final String ID = "id";
    public static final String VOTE_COUNT = "vote_count";

    public Movie(String title, String posterURL, int id, int voteCount) {
        this.title = title;
        this.posterURL = posterURL;
        this.id = id;
        this.voteCount = voteCount;
    }

    /**
     * Overrides toString method
     * String includes movie title
     *
     * @return  String - movie title
     */
    @Override
    public String toString() {
        return this.title + "," + this.posterURL + "," + this.id + "," + this.getVoteCount();
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
                        "http://image.tmdb.org/t/p/w154" + obj.getString(POSTER),
                        obj.getInt(ID), obj.getInt(VOTE_COUNT));

                movieList.add(movie);
            }
        }

        return movieList;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPosterURL() {
        return this.posterURL;
    }

    public int getId() {
        return this.id;
    }

    public int getVoteCount() {
        return this.voteCount;
    }
}
