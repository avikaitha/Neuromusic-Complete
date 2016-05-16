package ark.neuromusic.services;

import ark.neuromusic.utils.Config;
import retrofit.RestAdapter;

/**
 * Created by avinash on 4/9/16.
 */
public class Musicovery {
    private static final RestAdapter REST_ADAPTER = new RestAdapter
            .Builder()
            .setEndpoint(Config.MC_API_URL)
            .build();
    private static final TrackService SERVICE = REST_ADAPTER.create(TrackService.class);
    public static TrackService getService() {
        return SERVICE;
    }
}
