package ark.neuromusic.neurosky.signals;


import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;

public class SignalSleepStage extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(SignalSleepStage.class);

  @Override
  public void run() {
    LogUtils.LOGD(TAG, "Control signal - Sleep Stage level: " + level);
  }
}
