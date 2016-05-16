package ark.neuromusic.neurosky.signals;


import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;

public class SignalLowBattery extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(SignalLowBattery.class);

  @Override
  public void run() {
    LogUtils.LOGD(TAG, "Battery level is low.");
  }
}