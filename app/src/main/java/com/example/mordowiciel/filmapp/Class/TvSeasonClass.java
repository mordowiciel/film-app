package com.example.mordowiciel.filmapp.Class;

import java.util.Date;

public class TvSeasonClass {

    private String airDate;
    private int episodeCount;
    private int seasonId;
    private String posterPath;
    private int seasonNumber;

    public TvSeasonClass(String airDate, int episodeCount, int seasonId, String posterPath,
                         int seasonNumber){

        this.airDate = airDate;
        this.episodeCount = episodeCount;
        this.seasonId = seasonId;
        this.posterPath = posterPath;
        this.seasonNumber = seasonNumber;
    }


}
