package ark.neuromusic.neurosky.brainwaves;


import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;

public class BrainwaveGammaLow extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(BrainwaveGammaLow.class);

  @Override
  public void run() {
    EEGDeviceHandler.getActivityViewContract().setLowGamma(level);
//    LogUtils.LOGD(TAG, "Control signal - Low Gamma wave: " + level);
  }
}