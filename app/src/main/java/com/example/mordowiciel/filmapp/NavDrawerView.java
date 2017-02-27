package com.example.mordowiciel.filmapp;

/**
 * Created by szyma on 26.02.2017.
 */

public class NavDrawerView {

    private String menuText;
    private int imageId;

    public NavDrawerView(String menuText, int imageId){
        this.menuText = menuText;
        this.imageId = imageId;
    }

    public String getMenuText(){
        return menuText;
    }

    public int getImageId(){
        return imageId;
    }
}
