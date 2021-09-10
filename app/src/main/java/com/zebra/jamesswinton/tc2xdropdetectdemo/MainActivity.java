package com.zebra.jamesswinton.tc2xdropdetectdemo;

import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.zebra.jamesswinton.profilemanagerwrapper.EMDKProfileManagerWrapper;
import com.zebra.jamesswinton.profilemanagerwrapper.EMDKProfileManagerWrapper.OnXmlProcessedListener;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.Constants;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.DeviceUtils;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.PermissionsHelper;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.PermissionsHelper.OnPermissionsResultListener;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

  // Permissions
  private PermissionsHelper mPermissionsHelper;

  // Wrapper
  private EMDKProfileManagerWrapper mEmdkProfileManagerWrapper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Verify Model
    if (!DeviceUtils.supportedDevice()) {
      Toast.makeText(this, "Device not supported", Toast.LENGTH_LONG).show();
      finish();
    }

    // Grant permissions & Start service
    mPermissionsHelper = new PermissionsHelper(this, () -> {

      // Start Drop detect Service via XML
      mEmdkProfileManagerWrapper = new EMDKProfileManagerWrapper(this,
          new OnXmlProcessedListener() {
            @Override
            public void onComplete() {
              Toast.makeText(MainActivity.this ,"Drop Detect Sensor Activated",
                  Toast.LENGTH_SHORT).show();

              // Start Detection Service
              startService(new Intent(MainActivity.this, DropDetectionService.class));
              finish();
            }

            @Override
            public void onError(String... errors) {
              Toast.makeText(MainActivity.this,
                  "Error starting Drop Detect Sensor: " + Arrays.toString(errors),
                  Toast.LENGTH_SHORT).show();
            }
          });
      mEmdkProfileManagerWrapper.applyXml(Constants.StartDropSensorXML);
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mEmdkProfileManagerWrapper.release();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    mPermissionsHelper.onRequestPermissionsResult();
  }
}