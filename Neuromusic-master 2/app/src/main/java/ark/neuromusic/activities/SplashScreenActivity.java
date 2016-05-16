package ark.neuromusic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import ark.neuromusic.R;
import ark.neuromusic.generics.GenericApplication;


public class SplashScreenActivity extends AppCompatActivity {

  private Handler nextActivityHandler = new Handler();
  private Runnable startNextActivity = new Runnable() {
    @Override
    public void run() {
      finish();
      startActivity(new Intent(GenericApplication.getContext(), ConnectActivity.class));
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_splash_screen);
    nextActivityHandler.postDelayed(startNextActivity, 2500);
  }
}
