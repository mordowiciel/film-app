package com.example.mordowiciel.filmapp.Class;

/**
 * Created by szyma on 10.05.2017.
 */

public class ShowGenre {

    private int genreId;
    private String genreName;

    public ShowGenre(int genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }

    public int getGenreId() {
        return genreId;
    }

    public String getGenreName() {
        return genreName;
    }
}
