package com.example.akis.popularmoviez.utilities;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;


public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "movies.db";
    public static final String TABLE_NAME = "movie";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_MOVIE_TITLE = "original_title";
    public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
    public static final String COLUMN_MOVIE_OVERVIEW = "overview";
    public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
    public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdrop_path";
    public static final String CONTENT_AUTHORITY = "com.example.akis.popularmoviez";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

    public static Uri buildMovieUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_NAME
                + " (" +
                COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL, " +
                COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
