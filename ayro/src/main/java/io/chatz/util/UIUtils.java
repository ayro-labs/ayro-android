package io.ayro.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import io.ayro.R;
import io.ayro.core.AyroApp;
import io.ayro.model.Integration;

public class UIUtils {

  private UIUtils() {

  }

  public static void defaultToolbar(final AppCompatActivity activity, Integer color) {
    Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
    if (toolbar != null) {
      activity.setSupportActionBar(toolbar);
      toolbar.setBackgroundColor(color != null ? color : ContextCompat.getColor(activity, R.color.ayro_primary));
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

  public static void changeStatusBarColor(AppCompatActivity activity, Integer color) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      if (color == null) {
        color = ContextCompat.getColor(activity, R.color.ayro_primary);
      }
      float factor = 0.8f;
      int a = Color.alpha(color);
      int r = Math.round(Color.red(color) * factor);
      int g = Math.round(Color.green(color) * factor);
      int b = Math.round(Color.blue(color) * factor);
      int statusBarColor = Color.argb(a, Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
      Window window = activity.getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.setStatusBarColor(statusBarColor);
    }
  }

  public static void notify(Context context, int notificationId, Bitmap image, String title, String text, Intent intent) {
    Integer primaryColor = null;
    Integration integration = AyroApp.getInstance(context).getIntegration();
    if (integration != null) {
      String colorHex = integration.getConfiguration().get(Integration.PRIMARY_COLOR_CONFIGURATION);
      primaryColor = Color.parseColor(colorHex);
    }
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
    notificationBuilder.setSmallIcon(R.drawable.ayro_logo_small);
    notificationBuilder.setColor(primaryColor != null ? primaryColor : ContextCompat.getColor(context, R.color.ayro_primary));
    notificationBuilder.setContentTitle(title);
    notificationBuilder.setContentText(text);
    notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));
    notificationBuilder.setAutoCancel(true);
    notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
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

  public static int dpToPixels(Context context, int dp) {
    Resources resources = context.getResources();
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
  }
}
