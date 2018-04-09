package io.ayro.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import io.ayro.R;

public class UIUtils {

  private static final String NOTIFICATION_CHANNEL = "Ayro";

  private UIUtils() {

  }

  public static int dpToPixels(Context context, int dp) {
    Resources resources = context.getResources();
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
  }

  public static void useCustomToolbar(final AppCompatActivity activity) {
    Toolbar toolbar = activity.findViewById(R.id.toolbar);
    if (toolbar != null) {
      activity.setSupportActionBar(toolbar);
      toolbar.setBackgroundColor(AppUtils.getPrimaryColor(activity));
      TypedValue typedValue = new TypedValue();
      activity.getTheme().resolveAttribute(R.attr.homeAsUpIndicator, typedValue, true);
      toolbar.setNavigationIcon(typedValue.resourceId);
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          activity.finish();
        }
      });
    }
  }

  public static void changeStatusBarColor(AppCompatActivity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      float factor = 0.8f;
      int primaryColor = AppUtils.getPrimaryColor(activity);
      int a = Color.alpha(primaryColor);
      int r = Math.round(Color.red(primaryColor) * factor);
      int g = Math.round(Color.green(primaryColor) * factor);
      int b = Math.round(Color.blue(primaryColor) * factor);
      int statusBarColor = Color.argb(a, Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
      Window window = activity.getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.setStatusBarColor(statusBarColor);
    }
  }

  public static void notify(Context context, int notificationId, Bitmap image, String title, String text, Intent intent) {
    registerNotificationChannel(context);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL);
    notificationBuilder.setSmallIcon(R.drawable.notification_icon);
    notificationBuilder.setColor(AppUtils.getPrimaryColor(context));
    notificationBuilder.setContentTitle(title);
    notificationBuilder.setContentText(text);
    notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));
    notificationBuilder.setAutoCancel(true);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
      notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
      notificationBuilder.setLights(AppUtils.getPrimaryColor(context), 500, 500);
    }
    if (image != null) {
      notificationBuilder.setLargeIcon(ImageUtils.getCircularBitmap(image));
    }
    if (intent != null) {
      TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
      stackBuilder.addNextIntentWithParentStack(intent);
      PendingIntent pendingIntent = stackBuilder.getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT);
      notificationBuilder.setContentIntent(pendingIntent);
    }
    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(notificationId, notificationBuilder.build());
  }

  private static void registerNotificationChannel(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL) != null) {
        return;
      }
      NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
      notificationChannel.setDescription(NOTIFICATION_CHANNEL);
      notificationChannel.enableLights(true);
      notificationChannel.setLightColor(AppUtils.getPrimaryColor(context));
      notificationChannel.enableVibration(true);
      notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
      notificationManager.createNotificationChannel(notificationChannel);
    }
  }
}
