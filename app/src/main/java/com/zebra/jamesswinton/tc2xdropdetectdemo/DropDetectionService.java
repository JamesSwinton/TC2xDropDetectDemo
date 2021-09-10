package com.zebra.jamesswinton.tc2xdropdetectdemo;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.Constants;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.FileUtils;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.NotificationHelper;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.WriteToCSVAsync;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.WriteToCSVAsync.WriteToCsvCallback;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

public class DropDetectionService extends Service implements WriteToCsvCallback {

  // Drop Detection Intent
  private static final String FreeFallStateChangedAction = "com.zebra.ffdservice.FREE_FALL_STATE_CHANGED";

  // CSV Date Format
  private static final SimpleDateFormat CSVDateFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.UK);

  // Launch Mode Holder
  private boolean launchedBySystem = false;

  // Location Manager
  private LocationManager mLocationManager;

  @Override
  public void onCreate() {
    super.onCreate();
    registerReceiver(DropDetectionReceiver, new IntentFilter(FreeFallStateChangedAction));

    // Init LocationManager
    mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    // StartUp
    startForeground(NotificationHelper.NOTIFICATION_ID,
        NotificationHelper.createNotification(this));
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent != null) {
      String intentAction = intent.getAction();
      if (intentAction == null) {
        return START_STICKY;
      } else if (intentAction.equals(FreeFallStateChangedAction)) {
        launchedBySystem = true;
        getLocationAndLogDrop();
        return START_NOT_STICKY;
      } else if (intentAction.equals(NotificationHelper.ACTION_STOP_SERVICE)) {
        stopSelf();
        return START_NOT_STICKY;
      } else {
        return START_NOT_STICKY;
      }
    } else {
      return START_NOT_STICKY;
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unregisterReceiver(DropDetectionReceiver);
  }

  /**
   * Drop Detection Receiver
   */

  private final BroadcastReceiver DropDetectionReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String intentAction = intent.getAction();
      if (intentAction != null) {
        if (intentAction.equals(FreeFallStateChangedAction)) {
          boolean dropDetected = intent.getBooleanExtra(Constants.SensorParameterExtra, false);
          if (dropDetected) {
            getLocationAndLogDrop();
          }
        }
      }
    }
  };

  @SuppressLint("MissingPermission")
  private void getLocationAndLogDrop() {
    long dropTime = System.currentTimeMillis();
    String provider = getBestLocationProvider();
    if (provider == null) {
      logDropToFile(dropTime, 0, 0);
    } else {
      mLocationManager.requestSingleUpdate(provider, location ->
              logDropToFile(dropTime, location.getLatitude(), location.getLongitude()),
          null);
    }
  }

  private String getBestLocationProvider() {
    boolean gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    boolean networkEnabled = mLocationManager
        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    if (gpsEnabled) {
      return LocationManager.GPS_PROVIDER;
    } else if (networkEnabled) {
      return LocationManager.NETWORK_PROVIDER;
    } else {
      return null;
    }
  }

  /**
   * Logging
   */

  private void logDropToFile(long dropTime, double latitude, double longitude) {
    // Build CSV Values
    String[] csvValues = new String[] {
        "Drop Detected",
        CSVDateFormat.format(dropTime),
        String.valueOf(latitude),
        String.valueOf(longitude)
    };

    // Write Values
    new WriteToCSVAsync(DropDetectionService.this, csvValues).execute();
  }

  /**
   * CSV Callbacks
   */

  @Override
  public void onComplete() {
    Toast.makeText(DropDetectionService.this,
        "Drop Detected & File Saved",
        Toast.LENGTH_SHORT).show();

    if (launchedBySystem) {
      stopSelf();
    }
  }

  @Override
  public void onError(String e) {
    Toast.makeText(DropDetectionService.this,
        String.format("Drop Detected but error saving file: %1$s", e),
        Toast.LENGTH_SHORT).show();

    if (launchedBySystem) {
      stopSelf();
    }
  }

  /**
   * Unsupported Operations
   */

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    throw new UnsupportedOperationException("Binding is not supported");
  }

}
