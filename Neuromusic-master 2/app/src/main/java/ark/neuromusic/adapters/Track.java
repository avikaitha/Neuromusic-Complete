package ark.neuromusic.adapters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by avinash on 4/8/16.
 */
public class Track {
    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private int mID;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;


    private String mood;

    public String getTitle() {
        return mTitle;
    }

    public int getID() {
        return mID;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getMood() {
        return mood;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmID(int mID) {
        this.mID = mID;
    }

    public void setmStreamURL(String mStreamURL) {
        this.mStreamURL = mStreamURL;
    }

    public void setmArtworkURL(String mArtworkURL) {
        this.mArtworkURL = mArtworkURL;
    }
}
