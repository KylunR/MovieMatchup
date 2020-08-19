package edu.tacoma.uw.kylunr.moviematchup.data;

public class RecommendationItem {
    private String number;
    private String title;
    private String poster;

    public RecommendationItem(String number, String title, String poster) {
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
