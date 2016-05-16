package ark.neuromusic.utils;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by avinash on 4/23/16.
 */
public interface PostService {
    @FormUrlEncoded
    @POST("/test.php")
    public void postRaw(
            @Field("raw[]") int[] raw,
            Callback<JsonElement> cb
    );

    @FormUrlEncoded
    @POST("/train.php")
    public void postTrainRaw(
            @Field("raw[]") int[] raw,@Field("mood") String mood,
            Callback<String> cb
    );

    @FormUrlEncoded
    @POST("/finalinsert.php")
    public void postfinalmood(
            @Field("fd") double fd,@Field("mood") String mood,
            Callback<String> cb
    );
}
