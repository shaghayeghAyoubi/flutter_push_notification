package org.notification.mobile.flutter_notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PushNotificationServiceTest extends Service {
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        startForeground(1, createNotification("سرویس شروع شد"));

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                showWelcomeNotification();
            }
        }, 0, 10_000); // هر 10 ثانیه
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // تا در پس‌زمینه بماند
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotification(String text) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, "greeting_channel")
                .setContentTitle("پیام سرویس")
                .setContentText(text)
                .setSmallIcon(R.drawable.launch_background) // آیکون را اضافه کن
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
    }

    private void showWelcomeNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt(), createNotification("سلام! خوش آمدید 🌟"));
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "greeting_channel",
                    "Greeting Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
