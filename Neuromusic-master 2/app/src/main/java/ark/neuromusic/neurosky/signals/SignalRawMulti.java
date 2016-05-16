package ark.neuromusic.neurosky.signals;

import com.neurosky.thinkgear.TGRawMulti;

import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;

public class SignalRawMulti extends GenericSignal implements Runnable, SignalSettable {
  @Override
  public void run() {
    if(SignalPoorSignal.signalLevel == 0) {
        TGRawMulti obj = (TGRawMulti) object;
        GenericSignal.writeToFile(obj.ch1+"\t","raw_multi_ch1.txt");
        GenericSignal.writeToFile(obj.ch2+"\t","raw_multi_ch2.txt");
        GenericSignal.writeToFile(obj.ch3+"\t","raw_multi_ch3.txt");
        GenericSignal.writeToFile(obj.ch4+"\t","raw_multi_ch4.txt");
        GenericSignal.writeToFile(obj.ch5+"\t","raw_multi_ch5.txt");
        GenericSignal.writeToFile(obj.ch6+"\t","raw_multi_ch6.txt");
        GenericSignal.writeToFile(obj.ch7+"\t","raw_multi_ch7.txt");
        GenericSignal.writeToFile(obj.ch8+"\t","raw_multi_ch8.txt");
    }
  }
}