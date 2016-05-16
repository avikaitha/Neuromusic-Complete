package ark.neuromusic.neurosky.states;


import ark.neuromusic.R;
import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.utils.LogUtils;
import ark.neuromusic.utils.StringUtils;

public class StateConnecting implements Runnable {
  private static final String TAG = LogUtils.makeLogTag(StateConnecting.class);

  @Override
  public void run() {
    String message = StringUtils.getStringFromResources(R.string.connecting_to_device);
    LogUtils.LOGD(TAG, message);
    EEGDeviceHandler.getActivityViewContract().setMessageFromDevice(message);
  }
}
