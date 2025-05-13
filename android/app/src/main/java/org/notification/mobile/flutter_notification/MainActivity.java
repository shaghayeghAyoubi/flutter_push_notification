package org.notification.mobile.flutter_notification;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {

    @Override
    public void configureFlutterEngine(@NonNull @NotNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "backgroundservice").setMethodCallHandler(
                (methodCall, result) -> {
                    if (methodCall.method.equals("startService")) {
                        Intent intent = new Intent(this, PushNotificationServiceTest.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(intent);
                        }
                        result.success("Method Found");
                    }
                });
    }
}
