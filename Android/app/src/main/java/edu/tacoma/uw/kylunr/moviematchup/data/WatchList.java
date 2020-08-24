package edu.tacoma.uw.kylunr.moviematchup.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class WatchList {

    private List<Movie> watchList;

    public WatchList() {
        watchList = new ArrayList<Movie>();
    }

    public WatchList(WatchList watchList) {
        this.watchList = watchList.getWatchList();
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

    public void sort() {
        Collections.sort(watchList);
    }

    /**
     * Parses a string representation of a watch list
     * that is returned from Google's Firestore
     *
     * @param data
     */
    public void parseString(String data) {
        Scanner scanner = new Scanner(data);

        String[] values;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            values = line.split("\\$");

            watchList.add(new Movie(values[0], values[1],
                    Integer.parseInt(values[2]), Integer.parseInt(values[3])));
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
            retVal += movie.toString() + "\n";
        }

        return retVal;
    }

    public List<Movie> getWatchList() {
        return this.watchList;
    }
}
