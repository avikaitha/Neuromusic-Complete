package ark.neuromusic.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.neurosky.thinkgear.TGDevice;

import ark.neuromusic.generics.contracts.GenericActivitySignalContract;
import ark.neuromusic.neurosky.EEGDeviceHandler;

public class EEGDeviceUtils {
  private static TGDevice device;
  private BluetoothAdapter bluetoothAdapter;
  private EEGDeviceHandler deviceHandler;
  private boolean isDeviceRawSignalEnabled = true;

  Context mContext;

  public EEGDeviceUtils(Context context) {
    mContext = context;
  }

  private boolean isDeviceConnecting() {
    return device.getState() == TGDevice.STATE_CONNECTING;
  }

  public boolean isDeviceConnected() {
    return device.getState() == TGDevice.STATE_CONNECTED;
  }

  public boolean initializeBlueToothAdapter() {
    deviceHandler = new EEGDeviceHandler(mContext);
    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (bluetoothAdapter == null) {
      return false;
    } else {
      setDevice(new TGDevice(bluetoothAdapter, deviceHandler));
      return true;
    }
  }

  public TGDevice getDevice() {
    return device;
  }

  public void setDevice(TGDevice tgDevice) {
    EEGDeviceUtils.device = tgDevice;
  }

  public void reconnectToDevice() {
    initializeBlueToothAdapter();
    connectToDevice();
  }

  public void connectToDevice() {
    if (!isDeviceConnecting() && !isDeviceConnected()) {
      device.connect(isDeviceRawSignalEnabled);
    }
  }

  public void disconnectFromDevice() {
    if (device != null) {
      device.close();
      device = null;
    }
  }

  public boolean isBluetoothTurnedOn() {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    return (bluetoothAdapter != null && bluetoothAdapter.isEnabled());
  }

  public void setActivityViewContract(GenericActivitySignalContract activity) {
    deviceHandler.setActivityViewContract(activity);
  }
}