package ark.neuromusic.utils;

import retrofit.RestAdapter;

/**
 * Created by avinash on 4/23/16.
 */
public class PostData {
    private static final RestAdapter REST_ADAPTER = new RestAdapter
            .Builder()
            .setEndpoint("http://"+Config.FOG_IP).build();
    private static final PostService SERVICE = REST_ADAPTER.create(PostService.class);
    public static PostService getService() {
        return SERVICE;
    }
}
