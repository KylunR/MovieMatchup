package edu.tacoma.uw.kylunr.moviematchup.data;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *  Movie class represents a Movie that holds
 *  values for the title of the movies and a url to retrieve the
 *  movie's poster
 */
public class Movie implements Comparable<Movie> {

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
        if (isValidPosterURL(posterURL)) {
            this.posterURL = posterURL;
        } else {
            throw new IllegalArgumentException("Invalid poster URL");
        }
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
        return this.title + "$" + this.posterURL + "$" + this.id + "$" + this.getVoteCount();
    }

    /**
     * Overrides equals to compare
     * to movie objects
     *
     * @param o - object to compare
     * @return  - if movie is equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id;
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
                        "https://image.tmdb.org/t/p/w185" + obj.getString(POSTER),
                        obj.getInt(ID), obj.getInt(VOTE_COUNT));

                movieList.add(movie);
            }
        }

        return movieList;
    }

    /**
     * Checks to ensure the requested poster url is accurate
     * @param posterURL
     * @return
     */
    public boolean isValidPosterURL(String posterURL) {
        if (!posterURL.contains("https://image.tmdb.org/t/p/w185")) {
            return false;
        } else {
            return true;
        }
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

    @Override
    public int compareTo(Movie o) {

        // If equal
        if (o == this) {
            return 0;
        }
        // If less than
        else if (this.voteCount < o.getVoteCount()) {
            return -1;
        }
        // If greater
        else {
            return 1;
        }
    }
}
