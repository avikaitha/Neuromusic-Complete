package ark.neuromusic.neurosky.brainwaves;


import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;

public class BrainwaveAlphaHigh extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(BrainwaveAlphaHigh.class);

  @Override
  public void run() {
    EEGDeviceHandler.getActivityViewContract().setHighAlpha(level);
//    LogUtils.LOGD(TAG, "Control signal - High Alpha wave: " + level);
  }
}