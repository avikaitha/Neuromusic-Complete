package ark.neuromusic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ark.neuromusic.R;
import ark.neuromusic.generics.GenericActivity;
import ark.neuromusic.utils.Database;

public class PlaylistActivity extends GenericActivity {

    Database myDB;
    ArrayList<String> playlists;
    ListView playlistView;

    public static final String PLAYLIST_NAME = "ark.neuromusic.playlist_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_playlist);

        getLayoutInflater().inflate(R.layout.activity_playlist, linearLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        playlistView = (ListView) findViewById(R.id.play_list_view);
        myDB = new Database(getApplicationContext());
        myDB.getWritableDatabase();
        playlists = myDB.getPlaylists();
        ArrayAdapter<String> playlistAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playlists);
        playlistView.setAdapter(playlistAdapter);
        playlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlaylistActivity.this,PlaylistTrackActivity.class);
                intent.putExtra(PLAYLIST_NAME,myDB.getPlaylistID(playlists.get(position)));
                startActivity(intent);
            }
        });


    }

}
