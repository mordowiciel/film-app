package com.example.mordowiciel.filmapp;

/**
 * Created by mordowiciel on 21.02.17.
 */

public abstract class ShowDetails extends ShowThumbnail {

    protected String originalTitle;
    protected String overview;
    protected String releaseDate;
    protected double voteAverage;

    public ShowDetails(){

    }

    public ShowDetails(String id, String title, String originalTitle, String overview, String releaseDate,
                       double voteAverage, String posterLink) {

        super(id, title, posterLink);
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
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


}
