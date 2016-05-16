package ark.neuromusic.utils;


import ark.neuromusic.generics.GenericApplication;

public class StringUtils {
  public static String getStringFromResources(int resourcesId) {
    return GenericApplication.getContext().getResources().getString(resourcesId);
  }
}
