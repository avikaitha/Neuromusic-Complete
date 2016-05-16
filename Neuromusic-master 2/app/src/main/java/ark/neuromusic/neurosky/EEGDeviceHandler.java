package ark.neuromusic.neurosky;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.neurosky.thinkgear.TGDevice;

import java.util.HashMap;
import java.util.Map;

import ark.neuromusic.generics.GenericApplication;
import ark.neuromusic.generics.contracts.GenericActivitySignalContract;
import ark.neuromusic.neurosky.brainwaves.BrainwaveAlphaHigh;
import ark.neuromusic.neurosky.brainwaves.BrainwaveAlphaLow;
import ark.neuromusic.neurosky.brainwaves.BrainwaveBetaHigh;
import ark.neuromusic.neurosky.brainwaves.BrainwaveBetaLow;
import ark.neuromusic.neurosky.brainwaves.BrainwaveDelta;
import ark.neuromusic.neurosky.brainwaves.BrainwaveGammaHigh;
import ark.neuromusic.neurosky.brainwaves.BrainwaveGammaLow;
import ark.neuromusic.neurosky.brainwaves.BrainwaveTheta;
import ark.neuromusic.neurosky.signals.SignalAttention;
import ark.neuromusic.neurosky.signals.SignalBlink;
import ark.neuromusic.neurosky.signals.SignalEegPower;
import ark.neuromusic.neurosky.signals.SignalHeartRate;
import ark.neuromusic.neurosky.signals.SignalLowBattery;
import ark.neuromusic.neurosky.signals.SignalMeditation;
import ark.neuromusic.neurosky.signals.SignalPoorSignal;
import ark.neuromusic.neurosky.signals.SignalRawCount;
import ark.neuromusic.neurosky.signals.SignalRawData;
import ark.neuromusic.neurosky.signals.SignalRawMulti;
import ark.neuromusic.neurosky.signals.SignalSleepStage;
import ark.neuromusic.neurosky.signals.SignalStateChange;
import ark.neuromusic.neurosky.states.StateConnected;
import ark.neuromusic.neurosky.states.StateConnecting;
import ark.neuromusic.neurosky.states.StateDisconnected;
import ark.neuromusic.neurosky.states.StateIdle;
import ark.neuromusic.neurosky.states.StateNotFound;
import ark.neuromusic.neurosky.states.StateNotPaired;



public final class EEGDeviceHandler extends Handler {

  private static Map<Integer, Runnable> deviceStates;
  private static Map<Integer, GenericSignal> deviceSignals;
  private static Map<Integer, GenericSignal> brainWaves;
  private static GenericActivitySignalContract activityViewContract;
  Context mContext;

  public EEGDeviceHandler(Context context) {
    initializeDeviceStates();
    initializeDeviceSignals();
    initializeBrainWaves();
    mContext = context;
  }

  public static Map<Integer, Runnable> getDeviceStates() {
    return deviceStates;
  }

  public static Map<Integer, GenericSignal> getBrainWaves() {
    return brainWaves;
  }

  public static GenericActivitySignalContract getActivityViewContract() {
    return activityViewContract;
  }

  public void setActivityViewContract(GenericActivitySignalContract activityForUiUpdates) {
    this.activityViewContract = activityForUiUpdates;
  }

  private void initializeDeviceStates() {
    deviceStates = new HashMap<Integer, Runnable>();
    deviceStates.put(TGDevice.STATE_IDLE, new StateIdle());
    deviceStates.put(TGDevice.STATE_CONNECTING, new StateConnecting());
    deviceStates.put(TGDevice.STATE_CONNECTED, new StateConnected());
    deviceStates.put(TGDevice.STATE_NOT_FOUND, new StateNotFound());
    deviceStates.put(TGDevice.STATE_NOT_PAIRED, new StateNotPaired());
    deviceStates.put(TGDevice.STATE_DISCONNECTED, new StateDisconnected());
  }

  private void initializeDeviceSignals() {
    deviceSignals = new HashMap<Integer, GenericSignal>();
    deviceSignals.put(TGDevice.MSG_STATE_CHANGE, new SignalStateChange());
    deviceSignals.put(TGDevice.MSG_POOR_SIGNAL, new SignalPoorSignal());
    deviceSignals.put(TGDevice.MSG_ATTENTION, new SignalAttention());
    deviceSignals.put(TGDevice.MSG_MEDITATION, new SignalMeditation());
    deviceSignals.put(TGDevice.MSG_BLINK, new SignalBlink());
    deviceSignals.put(TGDevice.MSG_SLEEP_STAGE, new SignalSleepStage());
    deviceSignals.put(TGDevice.MSG_LOW_BATTERY, new SignalLowBattery());
    deviceSignals.put(TGDevice.MSG_RAW_COUNT, new SignalRawCount());
    deviceSignals.put(TGDevice.MSG_RAW_DATA, new SignalRawData(mContext));
    deviceSignals.put(TGDevice.MSG_HEART_RATE, new SignalHeartRate());
    deviceSignals.put(TGDevice.MSG_RAW_MULTI, new SignalRawMulti());
    deviceSignals.put(TGDevice.MSG_EEG_POWER, new SignalEegPower());
  }

  private void initializeBrainWaves() {
    brainWaves = new HashMap<Integer, GenericSignal>();
    brainWaves.put(SignalEegPower.DELTA, new BrainwaveDelta());
    brainWaves.put(SignalEegPower.THETA, new BrainwaveTheta());
    brainWaves.put(SignalEegPower.LOW_ALPHA, new BrainwaveAlphaLow());
    brainWaves.put(SignalEegPower.HIGH_ALPHA, new BrainwaveAlphaHigh());
    brainWaves.put(SignalEegPower.LOW_BETA, new BrainwaveBetaLow());
    brainWaves.put(SignalEegPower.HIGH_BETA, new BrainwaveBetaHigh());
    brainWaves.put(SignalEegPower.LOW_GAMMA, new BrainwaveGammaLow());
    brainWaves.put(SignalEegPower.MID_GAMMA, new BrainwaveGammaHigh());
  }

  @Override
  public void handleMessage(Message msg) {
    if (GenericApplication.getEegDeviceUtils().getDevice() != null) {
      if (deviceSignals.containsKey(msg.what)) {
        deviceSignals.get(msg.what).message(msg).run();
      }
    }
  }
}
