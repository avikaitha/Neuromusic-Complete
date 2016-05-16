package ark.neuromusic.neurosky.signals;

import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;



public class SignalPoorSignal extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(SignalPoorSignal.class);
  public static int signalLevel = 100;
  @Override
  public void run() {
    EEGDeviceHandler.getActivityViewContract().setSignalLevel(level);
//    LogUtils.LOGD(TAG, "Control signal - Signal level: " + level);
    signalLevel = level;
  }
}
