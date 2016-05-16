package ark.neuromusic.activities;


import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ark.neuromusic.R;
import ark.neuromusic.adapters.SCTrackAdapter;
import ark.neuromusic.adapters.Track;
import ark.neuromusic.generics.GenericActivity;
import ark.neuromusic.neurosky.signals.SignalRawData;
import ark.neuromusic.services.MusicService;
import ark.neuromusic.services.Musicovery;
import ark.neuromusic.services.SoundCloud;
import ark.neuromusic.services.TrackService;
import ark.neuromusic.utils.Config;
import ark.neuromusic.utils.Database;
import ark.neuromusic.utils.PostData;
import ark.neuromusic.utils.PostService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends GenericActivity {

    private static final String TAG = "MainActivity";
    private List<Track> mListItems;


    private SCTrackAdapter mSCAdapter;

    private TextView mSelectedTrackTitle;
    private ImageView mSelectedTrackImage;
    private ImageView mPlayerControl;
    private ListView mListView;
    private ImageView mTrackBackground;
    private SlidingUpPanelLayout mLayout;
    final ArrayList<Track> mSoundCloudTracks = new ArrayList<>();

    private MusicService musicSrv;
    private Intent playIntent;
    boolean musicBound;
    Database myDB;
    private Toolbar toolbar;
    private boolean paused=false, playbackPaused=false;

    public static String mood="not_train";

    public void songPicked(int index,boolean isInit){
        musicSrv.setSong(index);
        musicSrv.playSong(isInit);
        togglePlayPause();
        if(playbackPaused){
//            setController();

            playbackPaused=false;
        }
//        controller.show(0);
    }

    String final_mood;

    public void test(String mood, final double fd_value) {
        int index = 0;
        String pmood = mood.replace("'","");
        final_mood = pmood.replace(" ","");
        Log.d("Mood", final_mood);
        switch(final_mood) {
            case "happy":
                index = 0;
                break;
            case "sad":
                index = 1;
                break;
            case "calm":
                index = 2;
                break;
            case "angry":
                index = 3;
                break;
            default:
                index = -1;
        }
        new AlertDialog.Builder(this)
                .setTitle("Predicited Mood")
                .setSingleChoiceItems(R.array.moods, index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final_mood = (getResources().getStringArray(R.array.moods)[which]).toLowerCase();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getSoundtracks(final_mood, false);
                        PostService postService = PostData.getService();

                        if(fd_value > 0) {
                            postService.postfinalmood(fd_value, final_mood, new Callback<String>() {
                                @Override
                                public void success(String s, Response response) {
                                    Log.d("Final INsert",response.toString());
                                }

                                @Override
                                public void failure(RetrofitError error) {

                                    error.printStackTrace();

                                }
                            });
                        }


                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(false)
                .show();


    }

    private void getSoundtracks(final String mood, final boolean isTraining) {
        final ArrayList<String> trackslist = new ArrayList<>();
        TrackService musicoveryService = Musicovery.getService();

        musicoveryService.getNeuroTracks(mood, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement tracks, Response response) {
                try {
                    JsonObject obj = tracks.getAsJsonObject();
                    tracks = obj.get("root")
                            .getAsJsonObject()
                            .get("tracks")
                            .getAsJsonObject()
                            .get("track")
                            .getAsJsonArray();

                    if(isTraining) {
                        String title = tracks.getAsJsonArray()
                                .get(0)
                                .getAsJsonObject()
                                .get("title")
                                .toString();
                        trackslist.add(title);
                        Log.d(TAG, trackslist.get(0));
                    }
                    else {
                        for (int i = 0; i < tracks.getAsJsonArray().size(); i++) {
                            String title = tracks.getAsJsonArray()
                                    .get(i)
                                    .getAsJsonObject()
                                    .get("title")
                                    .toString();
                            trackslist.add(title);
                            Log.d(TAG, trackslist.get(i));

                        }
                    }

                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    response.getUrl();
                }

                TrackService soundCloudService = SoundCloud.getService();

                for (int i = 0; i < trackslist.size(); i++) {

                    try {
                        soundCloudService.getSoundCloudTracks(trackslist.get(i), new Callback<List<Track>>() {
                            @Override
                            public void success(List<Track> tracks, Response response) {
                                try {
                                    tracks.get(0).setMood(mood);
                                    mSoundCloudTracks.add(tracks.get(0));
                                    if (mSoundCloudTracks.size() > 0) {
                                        Log.d(TAG, "SC TRAck: " + mSoundCloudTracks.get((mSoundCloudTracks.size() - 1)).getTitle());
                                        loadTracks(mSoundCloudTracks);
                                        if (mSoundCloudTracks.size() == 1) {

                                            Track track = mSoundCloudTracks.get(0);

                                            mSelectedTrackTitle.setText(track.getTitle());
                                            Picasso.with(MainActivity.this)
                                                    .load(track.getArtworkURL())
                                                    .placeholder(getResources().getDrawable(R.drawable.placeholder_track_drawable))
                                                    .into(mSelectedTrackImage);
                                            Picasso.with(MainActivity.this)
                                                    .load(track.getArtworkURL())
                                                    .placeholder(getResources().getDrawable(R.drawable.placeholder_track_drawable))
                                                    .fit()
                                                    .into(mTrackBackground);
                                            mPlayerControl.setImageResource(R.drawable.ic_play);
                                            songPicked(0, true);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d(TAG, "Callback Failed");
                                error.printStackTrace();
                            }
                        });

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Callback Failed");
                error.printStackTrace();
            }

        });
    }


    final String PREFS_NAME = "MyPrefsFile";
    public boolean isFirstTime = false;

    public void train() {

        getSoundtracks("happy",true);
        getSoundtracks("sad",true);
        getSoundtracks("angry",true);
        getSoundtracks("calm",true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        getLayoutInflater().inflate(R.layout.activity_main, linearLayout);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        if(settings.getBoolean("my_first_time",true)) {
            Log.d("MainActivity", "First time");
            isFirstTime = true;
            mood = "null";
            getSupportActionBar().setTitle("Training Phase");
            new AlertDialog.Builder(this)
                    .setTitle("Welcome!!!")
                    .setMessage("Hi, Lets start by training our model\nPlease listen to these four songs while connected to your neurosky headset")

                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
            settings.edit().putBoolean("my_first_time",false).commit();

        }

        /**
         * Setting title and itemChecked
         */
//        mDrawerList.setItemChecked(position, true);
//        setTitle(listArray[position]);
        myDB = new Database(getApplicationContext());
        myDB.getWritableDatabase();
        Intent intent = getIntent();
        String fb_name,fb_dp,fb_cover,email;
        boolean isNeuroskyConnected = true;
        if(intent.hasExtra(LoginActivity.FB_NAME)) {
             fb_name = intent.getStringExtra(LoginActivity.FB_NAME);
             fb_dp = intent.getStringExtra(LoginActivity.FB_DP);
             fb_cover = intent.getStringExtra(LoginActivity.FB_COVER);
             email = intent.getStringExtra(LoginActivity.FB_EMAIL);
             isNeuroskyConnected = intent.getBooleanExtra(LoginActivity.FB_NONEURO, true);
            Config.setFBData(fb_name, fb_dp, fb_cover, email);
        }
        else {
            HashMap<String,String> fbData = myDB.getFBData();

             fb_name = fbData.get("NAME");
             fb_dp = fbData.get("PICTURE");
             fb_cover = fbData.get("COVER");
             email = fbData.get("EMAIL");

            Config.setFBData(fb_name, fb_dp, fb_cover, email);
        }


        mAdapter.notifyDataSetChanged();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // mDrawer Toggle Object Made
        mDrawer.setDrawerListener(mDrawerToggle); // mDrawer Listener set to the mDrawer toggle
        mDrawerToggle.syncState();

        if (isNeuroskyConnected) {
        if(!isFirstTime) {
            Intent moodIntent = getIntent();
            double fd_value = moodIntent.getDoubleExtra(SignalRawData.FD_VALUE, 0);
            String predicted_mood = moodIntent.getStringExtra(SignalRawData.MOOD_KEY);
//            Spinner spinner = new Spinner(getApplicationContext());
//            LinearLayout parentLayout = (LinearLayout)mLayout.getParent();
//            parentLayout.addView(spinner,1);

                if (fd_value != 0) {
                    test(predicted_mood, fd_value);
                }


            } else {
            train();
        }
        } else {
            test("",0);
        }
        mListItems = new ArrayList<>();
        mListView = (ListView)findViewById(R.id.track_list_view);

        mSCAdapter = new SCTrackAdapter(this, mListItems);
        mListView.setAdapter(mSCAdapter);

        mSelectedTrackTitle = (TextView)findViewById(R.id.selected_track_title);
        mSelectedTrackImage = (ImageView)findViewById(R.id.selected_track_image);
        mPlayerControl = (ImageView)findViewById(R.id.player_control);

        mTrackBackground = (ImageView)findViewById(R.id.track_background);



        mPlayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = mListItems.get(position);

                mSelectedTrackTitle.setText(track.getTitle());
                Picasso.with(MainActivity.this)
                        .load(track.getArtworkURL())
                        .placeholder(getResources().getDrawable(R.drawable.placeholder_track_drawable))
                        .into(mSelectedTrackImage);

                Picasso.with(MainActivity.this)
                        .load(track.getArtworkURL())
                        .placeholder(getResources().getDrawable(R.drawable.placeholder_track_drawable))
                        .fit()
                        .into(mTrackBackground);
                songPicked(position, false);
                mPlayerControl.setImageResource(R.drawable.ic_pause);
                if(isFirstTime) {
                    SignalRawData.index = 0;
                    SignalRawData.times = 0;
                    mood = track.getMood();
                }


        }
    });



        final Toolbar mSlidingToolbar = (Toolbar) findViewById(R.id.slidingToolbar);
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mSlidingToolbar.getLayoutParams();

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if(slideOffset >= 0.9) {
                    params.setMargins(0,getStatusBarHeight(),0,0);
                    mSlidingToolbar.setLayoutParams(params);
                }
                else {
                    params.setMargins(0,0,0,0);
                    mSlidingToolbar.setLayoutParams(params);
                }
            }

            @Override
            public void onPanelStateChanged(View view, SlidingUpPanelLayout.PanelState panelState, SlidingUpPanelLayout.PanelState panelState1) {

            }


        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });



    }


    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
//            setController();
            paused=false;
        }
    }
    @Override
    protected void onStop() {
//        controller.hide();
        super.onStop();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"In onStart");
        if(playIntent==null){
            Log.d(TAG,"playIntent Started");
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private void loadTracks(ArrayList<Track> tracks) {
        Log.d(TAG, "Inside LoadTracks");
        mListItems.clear();
        mListItems.addAll(tracks);
        mSCAdapter.notifyDataSetChanged();
    }

    private void togglePlayPause() {
        if(musicSrv.isPrepared()) {
            if (musicSrv.isPng()) {
                if(isFirstTime) mood = "null";
                musicSrv.pausePlayer();
                mPlayerControl.setImageResource(R.drawable.ic_play);
            } else {
                if(isFirstTime) mood = mSoundCloudTracks.get(musicSrv.getSongPosn()).getMood();
                musicSrv.go();
                mPlayerControl.setImageResource(R.drawable.ic_pause);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(playIntent);
        musicSrv=null;
        unbindService(musicConnection);
        unbindDrawables(findViewById(R.id.DrawerLayout));
        System.gc();

    }



    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_save_playlist) {
            final EditText input = new EditText(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);

            new AlertDialog.Builder(this)
                    .setTitle("Save Playlist")
                    .setMessage("Enter a name for playlist: ")
                    .setView(input)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            myDB.savePlaylist(mSoundCloudTracks,input.getText().toString());
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(mSoundCloudTracks);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
//            setController();
            playbackPaused=false;
        }
//        controller.show(0);
    }

    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
//            setController();
            playbackPaused=false;
        }
//        controller.show(0);
    }
}
