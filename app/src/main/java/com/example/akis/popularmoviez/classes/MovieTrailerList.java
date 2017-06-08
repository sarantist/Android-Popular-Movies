package com.example.akis.popularmoviez.classes;


import java.util.ArrayList;

public class MovieTrailerList {
    long id;
    public long getId() {
        return id;
    }
    ArrayList<MovieTrailer> results;

    public ArrayList<MovieTrailer> getVideos() {
        return results;
    }
}
