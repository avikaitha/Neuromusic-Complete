package ark.neuromusic.generics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.facebook.login.LoginManager;

import ark.neuromusic.R;
import ark.neuromusic.activities.LoginActivity;
import ark.neuromusic.activities.MainActivity;
import ark.neuromusic.activities.PlaylistActivity;
import ark.neuromusic.activities.SettingsActivity;
import ark.neuromusic.adapters.MyAdapter;
import ark.neuromusic.preferences.SettingsSharedPreferences;
import ark.neuromusic.utils.Config;

public abstract class GenericActivity extends AppCompatActivity {
    public static int prevSel = 1;

  protected SettingsSharedPreferences settingsSharedPreferences;
  String TITLES[] = {"Home","Fav Artists","Playlists","Settings","Logout"};
  int ICONS[] = {R.drawable.artist_icon,R.drawable.artist_icon,R.drawable.artist_icon,R.drawable.artist_icon,R.drawable.artist_icon};

  //Similarly we Create a String Resource for the name and email in the header view
  //And we also create a int resource for profile picture in the header view


                              // Declaring the Toolbar Object

  RecyclerView mRecyclerView;                           // Declaring RecyclerView
    protected RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
  RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
  protected DrawerLayout mDrawer;// Declaring DrawerLayout
    protected LinearLayout linearLayout;

  protected ActionBarDrawerToggle mDrawerToggle;// Declaring Action Bar mDrawer Toggle



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic);

        linearLayout = (LinearLayout)findViewById(R.id.content_frame);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES,ICONS, Config.fb_name,Config.email,Config.fb_dp,this);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture


        mRecyclerView.setAdapter(mAdapter);
        // Setting the adapter to RecyclerView

        final GestureDetector mGestureDetector = new GestureDetector(GenericActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    mDrawer.closeDrawers();
                    Intent intent;
                    switch(recyclerView.getChildLayoutPosition(child)) {
                        case 1:
                            if(prevSel != 1) {
                                prevSel = 1;
                                intent = new Intent(getApplicationContext(), MainActivity.class);
//                                Toast.makeText(getApplicationContext(), this.getClass().getSimpleName().toString(), Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                            break;
                        case 3:
                            if(prevSel != 3) {
                                prevSel = 3;
                                intent = new Intent(getApplicationContext(), PlaylistActivity.class);
                                startActivity(intent);
                            }
                            break;
                        case 4:
                            if(prevSel != 4) {
                                prevSel = 4;
                                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                                startActivity(intent);
                            }
                            break;
                        case 5:
                            if(prevSel != 5) {
                                prevSel = 5;
                                LoginManager.getInstance().logOut();
                                intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                            break;
                    }
//                    Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildLayoutPosition(child), Toast.LENGTH_SHORT).show();

                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        mDrawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // mDrawer object Assigned to the view
         // Finally we set the drawer toggle sync State
    }
  @Override
  protected void onResume() {
    super.onResume();
    settingsSharedPreferences = new SettingsSharedPreferences();
    keepScreenOnIfNecessary();
  }

  protected void keepScreenOnIfNecessary() {
    if (settingsSharedPreferences.get(SettingsSharedPreferences.keepScreenTurnedOn)) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    } else {
      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
  }
}
