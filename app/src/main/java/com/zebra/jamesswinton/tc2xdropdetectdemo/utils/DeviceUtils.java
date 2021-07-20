package com.zebra.jamesswinton.tc2xdropdetectdemo.utils;

import android.os.Build;

public class DeviceUtils {

  // Models
  private static final String[] SupportedModelsList = new String[]{ "TC26", "TC21" };

  public static boolean supportedDevice() {
    String model = Build.MODEL;
    for (String supportedModel : SupportedModelsList) {
      if (model.equals(supportedModel)) {
        return true;
      }
    } return false;
  }

}
