package ark.neuromusic.neurosky;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;

import ark.neuromusic.generics.GenericApplication;


public abstract class GenericSignal implements Runnable, SignalSettable {
  protected int level;
  protected Message message;
  protected Object object;
  public static void  writeToFile(String data,String fileName) {
      try {
          OutputStreamWriter outputStreamWriter = new OutputStreamWriter(GenericApplication.getContext().openFileOutput(fileName, Context.MODE_APPEND | Context.MODE_WORLD_READABLE));
          outputStreamWriter.write(data);
          outputStreamWriter.close();
//          Log.d("Rawdata", "Wrote to file:" + data + "Path: " + GenericApplication.getContext().getFilesDir().getAbsolutePath());
      } catch (IOException e) {
          Log.e("Exception", "File write failed: " + e.toString());
      }
  }
  @Override
  public Runnable message(Message message) {
    this.message = message;
    this.level = message.arg1;
    this.object = message.obj;
    return this;
  }

  @Override
  public void run() {
  }
}
