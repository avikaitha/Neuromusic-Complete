package ark.neuromusic.neurosky.signals;


import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;

public class SignalBlink extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(SignalBlink.class);

  @Override
  public void run() {
    EEGDeviceHandler.getActivityViewContract().setBlinkLevel(level);
//    LogUtils.LOGD(TAG, "Control signal - Blink level: " + level);
  }
}