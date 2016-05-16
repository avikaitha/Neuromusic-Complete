package ark.neuromusic.neurosky.states;


import ark.neuromusic.R;
import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.utils.LogUtils;
import ark.neuromusic.utils.StringUtils;

public class StateNotFound implements Runnable {
  private static final String TAG = LogUtils.makeLogTag(StateNotFound.class);

  @Override
  public void run() {
    String message = StringUtils.getStringFromResources(R.string.device_not_found);
    LogUtils.LOGD(TAG, message);
    EEGDeviceHandler.getActivityViewContract().setMessageFromDevice(message);
  }
}