package ark.neuromusic.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
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

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ark.neuromusic.R;
import ark.neuromusic.adapters.SCTrackAdapter;
import ark.neuromusic.adapters.Track;
import ark.neuromusic.generics.GenericActivity;
import ark.neuromusic.services.MusicService;
import ark.neuromusic.utils.Database;

public class PlaylistTrackActivity extends GenericActivity {

    private List<Track> mListItems;
    private static final String TAG = "PlaylistTrackActivity";

    private SCTrackAdapter mSCAdapter;

    private TextView mSelectedTrackTitle;
    private ImageView mSelectedTrackImage;
    private ImageView mPlayerControl;
    private ListView mListView;
    private ImageView mTrackBackground;
    private SlidingUpPanelLayout mLayout;
    ArrayList<Track> mSoundCloudTracks = new ArrayList<>();

    private MusicService musicSrv;
    private Intent playIntent;
    boolean musicBound;
    Database myDB;
    int playlistID = -1;
    private boolean paused=false, playbackPaused=false;

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

    private void getTracks(int playlistID) {
        mSoundCloudTracks = myDB.getPlaylistTracks(playlistID);
        if (mSoundCloudTracks.size() > 0) {
            Log.d(TAG, "SC TRAck: " + mSoundCloudTracks.get((mSoundCloudTracks.size() - 1)).getTitle());
            loadTracks(mSoundCloudTracks);


                Track track = mSoundCloudTracks.get(0);

                mSelectedTrackTitle.setText(track.getTitle());
                Picasso.with(PlaylistTrackActivity.this)
                        .load(track.getArtworkURL())
                        .placeholder(getResources().getDrawable(R.drawable.placeholder_track_drawable))
                        .into(mSelectedTrackImage);
                Picasso.with(PlaylistTrackActivity.this)
                        .load(track.getArtworkURL())
                        .placeholder(getResources().getDrawable(R.drawable.placeholder_track_drawable))
                        .fit()
                        .into(mTrackBackground);
                mPlayerControl.setImageResource(R.drawable.ic_play);
                songPicked(0, true);

        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, linearLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        myDB = new Database(getApplicationContext());
        myDB.getWritableDatabase();

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

        Intent intent = getIntent();
        playlistID = intent.getIntExtra(PlaylistActivity.PLAYLIST_NAME, -1);



        mLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
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
                Picasso.with(PlaylistTrackActivity.this)
                        .load(track.getArtworkURL())
                        .placeholder(getResources().getDrawable(R.drawable.placeholder_track_drawable))
                        .into(mSelectedTrackImage);

                Picasso.with(PlaylistTrackActivity.this)
                        .load(track.getArtworkURL())
                        .placeholder(getResources().getDrawable(R.drawable.placeholder_track_drawable))
                        .fit()
                        .into(mTrackBackground);
                songPicked(position, false);
                mPlayerControl.setImageResource(R.drawable.ic_pause);


            }
        });



        final Toolbar mSlidingToolbar = (Toolbar) findViewById(R.id.slidingToolbar);
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mSlidingToolbar.getLayoutParams();

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset >= 0.9) {
                    params.setMargins(0, getStatusBarHeight(), 0, 0);
                    mSlidingToolbar.setLayoutParams(params);
                } else {
                    params.setMargins(0, 0, 0, 0);
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
        Log.d(TAG, "In onStart");
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

                musicSrv.pausePlayer();
                mPlayerControl.setImageResource(R.drawable.ic_play);
            } else {

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
            final EditText input = new EditText(PlaylistTrackActivity.this);
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
            getTracks(playlistID);
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
