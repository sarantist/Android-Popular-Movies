package com.example.akis.popularmoviez.classes;


public class MovieTrailer {

    public static final String URL = "https://www.youtube.com/watch?v=";
    String id;
    String key;
    String name;
    String size;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getSize() {return size; }

    public String getVideoUrl() {
        return URL + getKey();
    }
}
