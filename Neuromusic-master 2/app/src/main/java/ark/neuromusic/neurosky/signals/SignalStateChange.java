package ark.neuromusic.neurosky.signals;


import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;

public class SignalStateChange extends GenericSignal implements Runnable, SignalSettable {
  @Override
  public void run() {
    if (EEGDeviceHandler.getDeviceStates().containsKey(level)) {
      EEGDeviceHandler.getDeviceStates().get(level).run();
    }
  }
}