package com.example.mordowiciel.filmapp;

public class FetchMoviesPassedParam {

    private String sortBy;
    private int pageNumber;

    public FetchMoviesPassedParam(String sortBy, int pageNumber) {
        this.sortBy = sortBy;
        this.pageNumber = pageNumber;
    }

    public String getSorting(){
        return sortBy;
    }

    public int getPageNumber(){
        return pageNumber;
    }
}
