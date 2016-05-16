package ark.neuromusic.generics;

import android.app.Application;
import android.content.Context;

import ark.neuromusic.utils.EEGDeviceUtils;


public class GenericApplication extends Application {
  private static Application instance;
  private static EEGDeviceUtils eegDeviceUtils;

  public static Context getContext() {
    return instance.getApplicationContext();
  }

  public static EEGDeviceUtils getEegDeviceUtils() {
    return eegDeviceUtils;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    eegDeviceUtils = new EEGDeviceUtils(getContext());
  }
}
