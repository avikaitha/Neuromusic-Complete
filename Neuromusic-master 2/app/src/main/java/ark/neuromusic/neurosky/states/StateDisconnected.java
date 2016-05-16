package ark.neuromusic.neurosky.states;


import ark.neuromusic.R;
import ark.neuromusic.utils.LogUtils;
import ark.neuromusic.utils.StringUtils;

public class StateDisconnected implements Runnable {
  private static final String TAG = LogUtils.makeLogTag(StateDisconnected.class);

  @Override
  public void run() {
    String message = StringUtils.getStringFromResources(R.string.disconnected_from_device);
    LogUtils.LOGD(TAG, message);
  }
}