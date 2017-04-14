package com.example.mordowiciel.filmapp.Class;

import java.util.ArrayList;

public class TvClass extends ShowDetails {

    private String lastAirDate;
    private ArrayList<TvSeasonClass> seasonsList;

    public TvClass(String id, String title, String originalTitle, String overview,
                   String releaseDate, String lastAirDate, double voteAverage, String posterLink,
                   ArrayList<TvSeasonClass> seasonsList) {

        super(id, title, originalTitle, overview, releaseDate, voteAverage, posterLink);

        this.seasonsList = seasonsList;
        this.lastAirDate = lastAirDate;
    }


    public ArrayList<ShowThumbnail> getSeasonsThumbnails() {

        ArrayList<ShowThumbnail> seasonsThumbnails = new ArrayList<>();
        for (TvSeasonClass season: seasonsList) {

            String seasonID = season.getSeasonId();
            String posterPath = season.getPosterPath();
            String seasonTitle = null;

            if (season.getSeasonNumber() == 0)
                seasonTitle = "Specials";
            else
                seasonTitle = "Season " + season.getSeasonNumber();

            ShowThumbnail thumbnail = new ShowThumbnail(seasonID, seasonTitle, posterPath);
            seasonsThumbnails.add(thumbnail);
        }
        return seasonsThumbnails;
    }

}
