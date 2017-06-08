package com.example.akis.popularmoviez.classes;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * Created by akis on 1/28/17.
 */

public class Movie  implements Parcelable {
    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    protected  String original_title;
    protected  String poster_path;
    protected  String backdrop_path;
    protected  String overview;
    protected  String vote_average;
    protected  String release_date;
    protected  Integer id;


    public Movie(Parcel in) {
        original_title = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        release_date = in.readString();
        id = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie() {

    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getBackDropPath() {
        return   backdrop_path;
    }

    public String getTitle() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() {
        return vote_average;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public Integer getId() {return id;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeString(overview);
        dest.writeString(vote_average);
        dest.writeString(release_date);
        dest.writeInt(id);
    }
}