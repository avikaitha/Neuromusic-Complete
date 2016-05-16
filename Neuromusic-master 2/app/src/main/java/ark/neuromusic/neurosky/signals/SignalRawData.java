package ark.neuromusic.neurosky.signals;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.JsonElement;

import ark.neuromusic.activities.MainActivity;
import ark.neuromusic.generics.GenericApplication;
import ark.neuromusic.neurosky.EEGDeviceHandler;
import ark.neuromusic.neurosky.GenericSignal;
import ark.neuromusic.neurosky.SignalSettable;
import ark.neuromusic.utils.LogUtils;
import ark.neuromusic.utils.PostData;
import ark.neuromusic.utils.PostService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignalRawData extends GenericSignal implements Runnable, SignalSettable {
  private static final String TAG = LogUtils.makeLogTag(SignalRawData.class);
    public static int index;
    public static int times;
    int max = 512*10;
    int[] data = new int[max];
    Context mContext;
    double fd_value;
    String predicted_mood;
    public SignalRawData(Context context) {
        mContext = context;
    }

    public static final String FD_VALUE = "ark.neuromusic.FD_VALUE";
    public static final String MOOD_KEY = "ark.neuromusic.MOOD";




  @Override
  public void run() {
    EEGDeviceHandler.getActivityViewContract().setRawData(level);
      if(SignalPoorSignal.signalLevel == 0) {
//          GenericSignal.writeToFile(level + "\t", "raw.txt");
          if(MainActivity.mood != "not_train") {
//                  LogUtils.LOGD(TAG, "Control signal - Raw data: " + GenericApplication.getContext().toString());
              if(MainActivity.mood != "null") {
                  if (index < max) {
                      data[index] = level;
                      Log.d("HFD: ", index + ""+ MainActivity.mood);
                      index++;
                  }
                  if (index == max) {
//              Log.d("HFD: ", hfdFromJNI(data)+"");
                      index++;
                      if(times == 0) {
                          index = 0;
                          times++;
                      }
                      PostService postService = PostData.getService();

                      postService.postTrainRaw(data,MainActivity.mood, new Callback<String>() {
                          @Override
                          public void success(String string, Response response) {

                              Log.d("Post", response.toString());
                          }

                          @Override
                          public void failure(RetrofitError error) {
                              error.printStackTrace();
                          }


                      });

                  }
              }
          }
          else {

                  if (index < max) {
                      data[index] = level;
                      Log.d("HFD: ", index + ": "+ data[index]);
                      index++;
                  }
                  if (index == max) {
//              Log.d("HFD: ", hfdFromJNI(data)+"");
                      index++;
                      PostService postService = PostData.getService();

                      postService.postRaw(data, new Callback<JsonElement>() {
                          @Override
                          public void success(JsonElement string, Response response) {
                               fd_value = string.getAsJsonArray()
                                      .get(string.getAsJsonArray().size() - 7)
                                      .getAsDouble();
                               predicted_mood = string.getAsJsonArray().get(string.getAsJsonArray().size() - 2).getAsString();
                              Intent intent = new Intent(GenericApplication.getContext(),MainActivity.class);
                              intent.putExtra(FD_VALUE,fd_value);
                              intent.putExtra(MOOD_KEY,predicted_mood);
                              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                              GenericApplication.getContext().startActivity(intent);

                              Log.d("Post", "Mood: "+predicted_mood+" FD: "+fd_value);
                          }

                          @Override
                          public void failure(RetrofitError error) {
                              error.printStackTrace();
                          }


                      });

                  }

          }


      }


  }


}


