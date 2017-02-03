package com.example.mordowiciel.filmapp;

import java.util.Date;

/**
 * Created by Szymon on 11.01.2017.
 */

public class Movie {

    private String id;
    private String title;
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private double voteAverage;
    private String posterLink;

    public Movie(String id, String title, String originalTitle, String overview, String releaseDate,
                 double voteAverage, String posterLink) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.posterLink = posterLink;
    }

    public String getMovieId() {
        return id;
    }

    public String getMovieTitle() {
        return title;
    }

    public String getMovieOriginalTitle() {
        return originalTitle;
    }

    public String getMovieOverview() {
        return overview;
    }

    public String getMovieReleaseDate() {
        return releaseDate;
    }

    public double getMovieVoteAverage() {
        return voteAverage;
    }

    public String getMoviePosterLink() {
        return posterLink;
    }

}
