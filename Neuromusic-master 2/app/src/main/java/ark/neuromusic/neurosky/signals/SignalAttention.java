package ark.neuromusic.neurosky.signals;


import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;

public class SignalAttention extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(SignalAttention.class);

  @Override
  public void run() {
    EEGDeviceHandler.getActivityViewContract().setAttentionLevel(level);
//    LogUtils.LOGD(TAG, "Control signal - Attention level: " + level);
  }
}
