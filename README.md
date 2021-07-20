
# Description
This app is designed to demonstrate the Fall Detection capabilities of Zebras TC2x Mobile Device range. Once launched, the app will create a long-running foreground service which will detect any broadcasts from Zebras Fall Detection Service and log these events to a CSV file, along with a timestamp and the current device location, if available. The CSV file is located at /sdcard/Download/device-drop-log.csv.

Please note that the Fall Detection service needs to be started first (via MX or StageNow).

# Installing & Testing

 1. Update TC2x device BSP to at least 10-24-04
 2. Download / Compile & Install TC2XDropDetectDemo APK
 3. Scan the below "enable_drop_detect" barcode in Stage Now application
	 4. Verify service has started by looking in the notification tray for "Fall Detection Service Active"

![enter image description here](https://downloads.jamesswinton.com/apks/Demos/FallDetectionDemo/Enable%20Fall%20Detection%20Service%20Stage%20Now%20Barcode.PNG)

 4. Start TC2XDropDetectDemo
	 5. Verify service has started by looking in the notification tray for "Drop Detection Logging Active"
 5. Throw device ~0.5m into air & catch. This will trigger a fall detection alert
 6. Verify Fall has been detected & logged by inspecting /sdcard/Download/device-drop-log.csv

# Testing via ADB

Alternatively, you can simulate a fall by sending the following ADB command:

```
adb shell am startservice -n com.zebra.jamesswinton.tc2xdropdetectdemo/.DropDetectionService -a "com.zebra.ffdservice.FREE_FALL_STATE_CHANGED"
```

You do not need to start the service before sending this command. If you use this command the Service will launch once, log the drop and stop itself.

# Pre-compiled
[Download APK](https://downloads.jamesswinton.com/apks/Demos/FallDetectionDemo/TC2X%20Drop%20Detect%20Demo%20(latest).apk)

# Install via Stage Now
![enter image description here](https://downloads.jamesswinton.com/apks/Demos/FallDetectionDemo/Install%20TC2x%20Drop%20Detect%20Demo%20Stage%20Now%20Barcode.PNG)