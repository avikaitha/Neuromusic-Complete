package ark.neuromusic.neurosky.brainwaves;


import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;

public class BrainwaveDelta extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(BrainwaveDelta.class);

  @Override
  public void run() {
    EEGDeviceHandler.getActivityViewContract().setDelta(level);
//    LogUtils.LOGD(TAG, "Control signal - Delta wave: " + level);
  }
}
