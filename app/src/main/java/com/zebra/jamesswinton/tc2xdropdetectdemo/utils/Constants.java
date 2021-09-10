package com.zebra.jamesswinton.tc2xdropdetectdemo.utils;

public class Constants {
  public static final String StartDropSensorXML =
      "<wap-provisioningdoc>\n"
    + "  <characteristic version=\"10.5\" type=\"Intent\">\n"
    + "    <parm name=\"Mode\" value=\"4\" />\n"
    + "    <parm name=\"SensorIdentifier\" value=\"1\" />\n"
    + "    <parm name=\"Action3\" value=\"Broadcast\" />\n"
    + "    <parm name=\"SensorParameter\" value=\"FreeFallState\" />\n"
    + "    <parm name=\"ActionName\" value=\"com.zebra.ffdservice.FREE_FALL_STATE_CHANGED\" />\n"
    + "  </characteristic>\n"
    + "</wap-provisioningdoc>";
}
