package ark.neuromusic.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import ark.neuromusic.adapters.Track;

/**
 * Created by akaitha on 4/17/2016.
 */
public class Database extends SQLiteOpenHelper {

    private static final int DB_VER = 1;


    static final String DB_NAME = "neuromusic.db";

    Context mContext;
    SQLiteDatabase db;



    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);

        this.mContext = context;
        Log.d("LOG_TAG","Created Databsse"+ context.getDatabasePath("neuromusic.db").getPath());


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("LOG_TAG","In OnCreate");
        try {
            final String SQL_CREATE_USER_TABLE = "CREATE TABLE USERS" +
                    " ( EMAIL  VARCHAR(30) NOT NULL PRIMARY KEY, " +
                    "NAME VARCHAR(50) NOT NULL, " +
                    "PICTURE VARCHAR(255), " +
                    "COVER VARCHAR(255))";
            db.execSQL(SQL_CREATE_USER_TABLE);
            Log.d("Database","Created Table: USERS");

        } catch (SQLException e) {
            Log.d("Database","Table: USERS already exists");
        }
        try {
            final String SQL_CREATE_TRACKS_TABLE = "CREATE TABLE TRACKS" +
                    " ( _ID INTEGER PRIMARY KEY, " +
                    "TITLE VARCHAR(50) NOT NULL, " +
                    "STREAM_URL VARCHAR(255), " +
                    "ARTWORK_URL VARCHAR(255)," +
                    "MOOD VARCHAR(10) )";
            db.execSQL(SQL_CREATE_TRACKS_TABLE);
            Log.d("Database","Created Table: TRACKS");

        } catch (SQLException e) {
            Log.d("Database","Table: TRACKS already exists");
        }

        try {
            final String SQL_CREATE_PLAYLISTS_TABLE = "CREATE TABLE PLAYLISTS" +
                    " ( _ID INTEGER PRIMARY KEY, " +
                    "NAME VARCHAR(50) NOT NULL " +
                   " )";
            db.execSQL(SQL_CREATE_PLAYLISTS_TABLE);
            Log.d("Database","Created Table: PLAYLISTS");

        } catch (SQLException e) {
            Log.d("Database","Table: PLAYLISTS already exists");
        }

        try {
            final String SQL_CREATE_PLAYLIST_TRACKS_TABLE = "CREATE TABLE PLAYLIST_TRACKS" +
                    " ( PLAYLIST_ID INTEGER, " +
                    "TRACK_ID INTEGER NOT NULL, " +
                    " PRIMARY KEY(PLAYLIST_ID,TRACK_ID), "+
                        "FOREIGN KEY(PLAYLIST_ID) REFERENCES PLAYLISTS(_ID),"+
                        "FOREIGN KEY(TRACK_ID) REFERENCES TRACKS(_ID))";
            db.execSQL(SQL_CREATE_PLAYLIST_TRACKS_TABLE);
            Log.d("Database","Created Table: PLAYLIST_TRACKS");

        } catch (SQLException e) {
            Log.d("Database","Table: PLAYLIST_TRACKS already exists");
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

    }

    int init = 0;
    public void insertValues(ContentValues values, String tableName) {
        if(init == 0) {
            db = this.getWritableDatabase();
            init++;
        }

        long _id = -2;

        try {

            _id = db.insert(tableName, null, values);
        }
        catch (SQLiteException e)
        {
            Log.d("LOG_TAG",e.toString());
        }
        finally {

            Cursor cursor = db.rawQuery("SELECT * FROM " + tableName,null);
            cursor.moveToLast();
            for(int i = 0;i<cursor.getColumnCount();i++) {
                Log.d("Database","Count: "+cursor.getColumnCount()+"Key: "+cursor.getColumnName(i)+": "+cursor.getString(i));
            }


        }



    }

    public ArrayList<String> getPlaylists() {
        db = this.getReadableDatabase();
        ArrayList<String> playlists = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM PLAYLISTS",null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            playlists.add(cursor.getString(1));
            cursor.moveToNext();
        }

        return playlists;
    }

    public int getPlaylistID(String playlist_name) {
        int playlistID = -1;
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _ID FROM PLAYLISTS WHERE NAME = \""+playlist_name+"\"",null);
        cursor.moveToFirst();
        playlistID = cursor.getInt(0);
        return  playlistID;
    }


    public void savePlaylist(ArrayList<Track> playlistTracks,String playlistName) {
        db = this.getWritableDatabase();
        ContentValues playlist_values = new ContentValues();
        playlist_values.put("NAME",playlistName);
        int playlist_id=0;
//        playlist_values.put("_ID",1);
        try {

            db.insert("PLAYLISTS", null, playlist_values);
        }
        catch (SQLiteException e)
        {
            Log.d("LOG_TAG",e.toString());
        }
        finally {

            Cursor cursor = db.rawQuery("SELECT * FROM PLAYLISTS",null);
            cursor.moveToLast();
            playlist_id = cursor.getInt(0);
            for(int i = 0;i<cursor.getColumnCount();i++) {
                Log.d("Database","Count: "+cursor.getColumnCount()+"Key: "+cursor.getColumnName(i)+": "+cursor.getString(i));
            }


        }

        ArrayList<Integer> track_id = new ArrayList<>();
        for(int i=0;i<playlistTracks.size();i++) {
            ContentValues track_values = new ContentValues();
            track_values.put("TITLE",playlistTracks.get(i).getTitle());
            track_values.put("STREAM_URL",playlistTracks.get(i).getStreamURL());
            track_values.put("ARTWORK_URL",playlistTracks.get(i).getArtworkURL());
            track_values.put("MOOD",playlistTracks.get(i).getMood());
            try {

                db.insert("TRACKS", null, track_values);
            }
            catch (SQLiteException e)
            {
                Log.d("LOG_TAG",e.toString());
            }
            finally {

                Cursor cursor = db.rawQuery("SELECT _ID FROM TRACKS",null);
                cursor.moveToLast();
                track_id.add(cursor.getInt(0));

            }

        }



        for(int i=0;i<track_id.size();i++) {

            ContentValues playlist_track_ids = new ContentValues();
            playlist_track_ids.put("PLAYLIST_ID",playlist_id+"");
            playlist_track_ids.put("TRACK_ID",track_id.get(i)+"");
            db.insert("PLAYLIST_TRACKS", null, playlist_track_ids);
        }







    }
    public HashMap<String,String> getFBData() {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS",null);
        HashMap<String,String> result = new HashMap<>();
        cursor.moveToLast();
        for(int i = 0;i<cursor.getColumnCount();i++) {
            result.put(cursor.getColumnName(i),cursor.getString(i));
        }

        return result;

    }

    public ArrayList<Track> getPlaylistTracks(int playlistID) {
        db = this.getReadableDatabase();
        ArrayList<Track> trackslist = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT _ID,TITLE,STREAM_URL,ARTWORK_URL,MOOD FROM PLAYLIST_TRACKS,TRACKS WHERE TRACK_ID = _ID AND PLAYLIST_ID = "+playlistID,null);
        Log.d("Cursor: ",cursor.getColumnCount()+"");
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            Track track = new Track();
                track.setmID(cursor.getInt(0));
                track.setmTitle(cursor.getString(1));
            track.setmStreamURL(cursor.getString(2));
                track.setmArtworkURL(cursor.getString(3));
                track.setMood(cursor.getString(4));
                trackslist.add(track);
                cursor.moveToNext();

        }
        return trackslist;
    }





}
