package ark.neuromusic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ark.neuromusic.R;
import ark.neuromusic.activities.contracts.ActivityConnectContract;
import ark.neuromusic.activities.controllers.ConnectController;
import ark.neuromusic.generics.GenericApplication;
import ark.neuromusic.generics.contracts.GenericActivitySignalContract;
import ark.neuromusic.utils.StringUtils;


public class ConnectActivity extends AppCompatActivity implements ActivityConnectContract, GenericActivitySignalContract {

  private ConnectController connectController;

  public static final String NO_NEUROSKY = "ark.neurosky.NO_NEUROSKY";

  private Handler nextActivityHandler;

  private ImageView bluetoothConnect;
  private TextView labelConnect;
  private Button noNeurosky;

  private Runnable startNextActivity = new Runnable() {
    @Override
    public void run() {
      finish();
      startActivity(new Intent(GenericApplication.getContext(), LoginActivity.class));
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_connect);

    nextActivityHandler = new Handler();
    bluetoothConnect = (ImageView) findViewById(R.id.bluetooth_connect);
    labelConnect = (TextView) findViewById(R.id.label_connect);
    noNeurosky = (Button) findViewById(R.id.noNeurosky);
    noNeurosky.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();

        startActivity(new Intent(GenericApplication.getContext(), LoginActivity.class)
        .putExtra(NO_NEUROSKY,"No neurosky"));
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();

    connectController = new ConnectController(this);

    if (!GenericApplication.getEegDeviceUtils().initializeBlueToothAdapter()) {
      Toast.makeText(this, StringUtils.getStringFromResources(R.string.bluetooth_is_not_available), Toast.LENGTH_LONG).show();
    } else {
      GenericApplication.getEegDeviceUtils().setActivityViewContract(this);
    }
  }



  public void bluetoothConnectClickHandler(View view) {
    if (GenericApplication.getEegDeviceUtils().isBluetoothTurnedOn()) {
      GenericApplication.getEegDeviceUtils().connectToDevice();
    } else {
      Toast.makeText(this, StringUtils.getStringFromResources(R.string.please_activate_bluetooth), Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public ImageView getBluetoothConnect() {
    return bluetoothConnect;
  }

  @Override
  public Handler getNextActivityHandler() {
    return nextActivityHandler;
  }

  @Override
  public TextView getLabel() {
    return labelConnect;
  }

  @Override
  public void setMessageFromDevice(String string) {
    labelConnect.setText(string);
    connectController.establishConnection(string, startNextActivity);
  }

  @Override
  public void setSignalLevel(int level) {

  }

  @Override
  public void setAttentionLevel(int level) {

  }

  @Override
  public void setMeditationLevel(int level) {

  }

  @Override
  public void setBlinkLevel(int level) {

  }

  @Override
  public void setRawData(int level) {

  }

  @Override
  public void setDelta(int level) {

  }

  @Override
  public void setTheta(int level) {

  }

  @Override
  public void setLowAlpha(int level) {

  }

  @Override
  public void setHighAlpha(int level) {

  }

  @Override
  public void setLowBeta(int level) {

  }

  @Override
  public void setHighBeta(int level) {

  }

  @Override
  public void setLowGamma(int level) {

  }

  @Override
  public void setMidGamma(int level) {

  }
}
