package com.example.mordowiciel.filmapp;

/**
 * Created by mordowiciel on 21.02.17.
 */

public abstract class ShowClass {

    protected String id;
    protected String title;
    protected String originalTitle;
    protected String overview;
    protected String releaseDate;
    protected double voteAverage;
    protected String posterLink;

    public ShowClass(){

    }

    public ShowClass(String id, String title, String originalTitle, String overview, String releaseDate,
                      double voteAverage, String posterLink) {

        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.posterLink = posterLink;
    }

    public String getShowId() {
        return id;
    }

    public String getShowTitle() {
        return title;
    }

    public String getShowOriginalTitle() {
        return originalTitle;
    }

    public String getShowOverview() {
        return overview;
    }

    public String getShowReleaseDate() {
        return releaseDate;
    }

    public double getShowVoteAverage() {
        return voteAverage;
    }

    public String getShowPosterLink() {
        return posterLink;
    }

}
