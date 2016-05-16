package ark.neuromusic.neurosky.brainwaves;


import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;

public class BrainwaveBetaLow extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(BrainwaveBetaLow.class);

  @Override
  public void run() {
    EEGDeviceHandler.getActivityViewContract().setLowBeta(level);
//    LogUtils.LOGD(TAG, "Control signal - Low Beta wave: " + level);
  }
}
