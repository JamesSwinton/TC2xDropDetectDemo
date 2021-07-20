package com.zebra.jamesswinton.tc2xdropdetectdemo;

import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.DeviceUtils;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.PermissionsHelper;
import com.zebra.jamesswinton.tc2xdropdetectdemo.utils.PermissionsHelper.OnPermissionsResultListener;

public class MainActivity extends AppCompatActivity {

  // Permissions
  private PermissionsHelper mPermissionsHelper;

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
      startService(new Intent(this, DropDetectionService.class));
      finish();
    });
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    mPermissionsHelper.onRequestPermissionsResult();
  }
}