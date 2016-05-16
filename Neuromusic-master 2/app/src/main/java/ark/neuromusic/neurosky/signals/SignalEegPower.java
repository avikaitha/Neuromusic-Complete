package ark.neuromusic.neurosky.signals;

import android.os.Message;

import com.neurosky.thinkgear.TGEegPower;

import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;


public class SignalEegPower extends GenericSignal implements Runnable, SignalSettable {

  public static int DELTA = 1, THETA = 2, LOW_ALPHA = 3, HIGH_ALPHA = 4, LOW_BETA = 5, HIGH_BETA = 6, LOW_GAMMA = 7, MID_GAMMA = 8;
  private TGEegPower tgEegPower;
  private Message message;
  private GenericSignal genericSignal;

  @Override
  public void run() {
    message = new Message();
    tgEegPower = (TGEegPower) object;
    setBrainWave(DELTA, tgEegPower.delta);
    setBrainWave(THETA, tgEegPower.theta);
    setBrainWave(LOW_ALPHA, tgEegPower.lowAlpha);
    setBrainWave(HIGH_ALPHA, tgEegPower.highAlpha);
    setBrainWave(LOW_BETA, tgEegPower.lowBeta);
    setBrainWave(HIGH_BETA, tgEegPower.highBeta);
    setBrainWave(LOW_GAMMA, tgEegPower.lowGamma);
    setBrainWave(MID_GAMMA, tgEegPower.midGamma);
    GenericSignal.writeToFile(tgEegPower.delta + "\t", "delta.txt");
    GenericSignal.writeToFile(tgEegPower.theta + "\t", "theta.txt");
    GenericSignal.writeToFile(tgEegPower.lowAlpha + "\t", "lowAlpha.txt");
    GenericSignal.writeToFile(tgEegPower.highAlpha + "\t", "highAlpha.txt");
    GenericSignal.writeToFile(tgEegPower.lowBeta + "\t", "lowBeta.txt");
    GenericSignal.writeToFile(tgEegPower.highBeta + "\t", "highBeta.txt");
    GenericSignal.writeToFile(tgEegPower.lowGamma + "\t", "lowGamma.txt");
    GenericSignal.writeToFile(tgEegPower.midGamma + "\t", "midGamma.txt");
  }

  private void setBrainWave(int brainWaveType, int brainWaveValue) {
    message.arg1 = brainWaveValue;
    EEGDeviceHandler.getBrainWaves().get(brainWaveType).message(message).run();
  }
}