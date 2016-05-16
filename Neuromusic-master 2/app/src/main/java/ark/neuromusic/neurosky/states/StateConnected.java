package ark.neuromusic.neurosky.states;


import ark.neuromusic.R;
import ark.neuromusic.generics.GenericApplication;
import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.utils.LogUtils;
import ark.neuromusic.utils.StringUtils;

public class StateConnected implements Runnable {
  private static final String TAG = LogUtils.makeLogTag(StateConnected.class);

  @Override
  public void run() {
    GenericApplication.getEegDeviceUtils().getDevice().start();
    String message = StringUtils.getStringFromResources(R.string.connected_to_device);
    LogUtils.LOGD(TAG, message);
    EEGDeviceHandler.getActivityViewContract().setMessageFromDevice(message);
  }
}