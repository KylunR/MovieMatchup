package edu.tacoma.uw.kylunr.moviematchup.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents a FavoriteList object.  Each
 * user has a Favorite List object and it is used to store the
 * list of the user's ranked movies.
 */
public class FavoriteList {

    private List<Movie> list;

    public FavoriteList() {
        list = new ArrayList<Movie>();
    }

    public FavoriteList(FavoriteList favoriteList) {
        this.list = favoriteList.getList();
    }

    /**
     * Takes in the result of a movie matchup
     * and updates the list accordingly
     * 
     * @param winner - winner of the matchup
     * @param loser  - loser of the matchup
     */
    public void matchupResult(Movie winner, Movie loser) {

        // Scenarios
        // - Both movies are not on the list
        // - Winner is on the list, loser is not on the list
        // - Loser is on the list, winner is not on the list
        // - Both are on the list

        // If both movies are not already on the list
        if (!list.contains(winner) && !list.contains(loser)) {
            // Add both movies, winner before loser
            list.add(winner);
            list.add(loser);
        } 
        // If winner is on list and loser is not on list
        else if (list.contains(winner) && !list.contains(loser)) {
            // Leave winner in same position, add loser to one position less
            int index = list.indexOf(winner) + 1;
            list.add(index, loser);
        }
        // If winner is not on list and loser is on list
        else if (!list.contains(winner) && list.contains(loser)) {
            // Insert winner at loser's index
            int index = list.indexOf(loser);
            list.add(index, winner);
        }
        // If both movies are on the list
        else {
            int winnerIndex = list.indexOf(winner);
            int loserIndex = list.indexOf(loser);

            // If loser's index is before winners
            if (loserIndex < winnerIndex) {
                // Remove winner and add at loser's index
                list.remove(winner);
                list.add(loserIndex, winner);
            } 
            // If winner's index is before losers
            else {
                // Do nothing
            }
        }

    }

    /**
     * Adds a chosen movie to the end of
     * the favorite movie list
     *
     * @param movie - movie to add
     */
    public void addMovie(Movie movie) {
        if(list.contains(movie)) {
            return;
        } else {
            list.add(movie);
        }
    }

    /**
     * Removes a movie from the favorite list
     * Used if a movie was added in error
     *
     * @param movie - movie to add
     */
    public void removeMovie(Movie movie) {
        if (list.contains(movie)) {
            list.remove(movie);
        }
    }

    /**
     * Parses a string representation of a favorite list
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

            list.add(new Movie(values[0], values[1],
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

        for (Movie movie : list) {
            retVal += movie.toString() + "\n";
        }

        return retVal;
    }

    public List<Movie> getList() {
        return this.list;
    }
}
