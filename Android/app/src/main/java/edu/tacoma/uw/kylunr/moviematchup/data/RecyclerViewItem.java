package edu.tacoma.uw.kylunr.moviematchup.data;


/**
 * This class represents a single item in the recyclerview.
 * This item provides a representation of a movie with the movie's
 * title and poster.
 */
public class RecyclerViewItem {
    private String number;      // Used to number items in recyclerview
    private String title;       // Title of movie
    private String poster;      // Poster url of movie

    public RecyclerViewItem(String number, String title, String poster) {
        this.number = number;
        this.title = title;
        this.poster = poster;
    }

    public String getNumber() {
        return this.number;
    }

    public String getTitle() {
        return this.title;
    }

    public String getPoster() {
        return this.poster;
    }
}
