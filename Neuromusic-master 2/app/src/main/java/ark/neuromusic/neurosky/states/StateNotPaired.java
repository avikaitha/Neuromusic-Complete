package ark.neuromusic.neurosky.states;


import ark.neuromusic.R;
import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.utils.LogUtils;
import ark.neuromusic.utils.StringUtils;

public class StateNotPaired implements Runnable {
  private static final String TAG = LogUtils.makeLogTag(StateNotPaired.class);

  @Override
  public void run() {
    String message = StringUtils.getStringFromResources(R.string.device_not_paired);
    LogUtils.LOGD(TAG, message);
    EEGDeviceHandler.getActivityViewContract().setMessageFromDevice(message);
  }
}
