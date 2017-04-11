package com.example.mordowiciel.filmapp.Class;

import java.util.ArrayList;

public class TvSeasonClass {

    private String airDate;
    private int episodeCount;
    private String seasonId;
    private String posterPath;
    private int seasonNumber;

    public TvSeasonClass(String airDate, int episodeCount, String seasonId, String posterPath,
                         int seasonNumber){

        this.airDate = airDate;
        this.episodeCount = episodeCount;
        this.seasonId = seasonId;
        this.posterPath = posterPath;
        this.seasonNumber = seasonNumber;
    }

    public String getSeasonId() {
        return this.seasonId;
    }

    public String getPosterPath() {
        return this.posterPath;
    }

    public int getSeasonNumber() {
        return this.seasonNumber;
    }


}
