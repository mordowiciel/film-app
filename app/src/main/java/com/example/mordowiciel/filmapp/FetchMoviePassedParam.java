package com.example.mordowiciel.filmapp;

/**
 * Created by szyma on 19.02.2017.
 */

public class FetchMoviePassedParam {

    private String sortBy;
    private int pageNumber;

    public FetchMoviePassedParam(String sortBy, int pageNumber) {
        this.sortBy = sortBy;
        this.pageNumber = pageNumber;
    }

    public String getSorting(){
        return sortBy;
    }

    public int getPageNumber(){
        return pageNumber;
    }

    public void setSorting(String sortBy){
        this.sortBy = sortBy;
    }

    public void setPageNumber(int pageNumber){
        this.pageNumber = pageNumber;
    }
}
