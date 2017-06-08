package com.example.akis.popularmoviez.classes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by akis on 1/28/17.
 */

public class MovieList implements Parcelable {
    protected  Integer page;
    protected ArrayList<Movie> results;


    protected MovieList(Parcel in) {
        results = in.createTypedArrayList(Movie.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieList> CREATOR = new Creator<MovieList>() {
        @Override
        public MovieList createFromParcel(Parcel in) {
            return new MovieList(in);
        }

        @Override
        public MovieList[] newArray(int size) {
            return new MovieList[size];
        }
    };

    public ArrayList<Movie> getMovies() {
        return results;
    }

    public Integer getPage() {
        return page;
    }

}