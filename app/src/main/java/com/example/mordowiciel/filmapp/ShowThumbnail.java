package com.example.mordowiciel.filmapp;

/**
 * Created by mordowiciel on 22.02.17.
 */

public class ShowThumbnail {

    protected String id;
    protected String title;
    protected String posterLink;

    public ShowThumbnail(){

    }

    public ShowThumbnail (String id, String title, String posterPath) {
        this.id = id;
        this.title = title;
        this.posterLink = posterPath;
    }

    public String getShowId() {return id;}
    public String getShowTitle() {return title;}
    public String getShowPosterLink() {return posterLink;}
}
