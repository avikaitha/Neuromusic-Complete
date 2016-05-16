package ark.neuromusic.neurosky.brainwaves;


import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;

public class BrainwaveTheta extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(BrainwaveTheta.class);

  @Override
  public void run() {
    EEGDeviceHandler.getActivityViewContract().setTheta(level);
//    LogUtils.LOGD(TAG, "Control signal - Theta wave: " + level);
  }
}
