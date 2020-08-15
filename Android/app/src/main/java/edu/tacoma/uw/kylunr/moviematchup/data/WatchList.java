package edu.tacoma.uw.kylunr.moviematchup.data;

import java.util.ArrayList;
import java.util.List;

public class WatchList {

    private List<Movie> watchList;

    public WatchList() {
        watchList = new ArrayList<Movie>();
    }

    /**
     * Adds a chosen movie to the end of
     * to watch list
     *
     * @param movie - movie to add
     */
    public void addMovie(Movie movie) {
        if (!watchList.contains(movie)) {
            watchList.add(movie);
        }
    }

    /**
     * Removes a movie from the watch list
     *
     * @param movie
     */
    public void removeMovieFromWatchList(Movie movie) {
        if (watchList.contains(movie)) {
            watchList.remove(movie);
        }
    }

    /**
     * Returns if a movie is on the watch list,
     * used to prevent rotation of movies that
     * are unseen
     *
     * @param movie
     * @return bool - if movie is on watch list
     */
    public boolean isOnWatchList(Movie movie) {
        if (watchList.contains(movie)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Overrides the toString method to
     * return a string containing the
     * titles of all the movies on the list
     *
     * @return - string with all the movies titles
     *           delimited with space
     */
    @Override
    public String toString() {
        String retVal = "";

        for (Movie movie : watchList) {
            retVal += movie.toString() + ". ";
        }

        retVal = retVal.substring(0, retVal.length() - 1);

        return retVal;
    }

    public List<Movie> getWatchList() {
        return this.watchList;
    }
}
