package ark.neuromusic.neurosky.signals;


import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;

public class SignalMeditation extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(SignalMeditation.class);

  @Override
  public void run() {
    EEGDeviceHandler.getActivityViewContract().setMeditationLevel(level);
//    LogUtils.LOGD(TAG, "Control signal - Meditation level: " + level);
  }
}