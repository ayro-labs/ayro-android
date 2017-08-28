package io.chatz.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import io.chatz.R;
import io.chatz.ui.activity.ChatzActivity;

public class UIUtils {

  private UIUtils() {

  }

  public static void defaultToolbar(final AppCompatActivity activity) {
    Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
    if (toolbar != null) {
      activity.setSupportActionBar(toolbar);
      toolbar.setNavigationIcon(getAttributeResourceId(activity, R.attr.homeAsUpIndicator));
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          activity.finish();
        }
      });
    }
  }

  public static void notify(Context context, int notificationId, Bitmap image, String title, String text, Intent intent) {
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
    notificationBuilder.setSmallIcon(R.drawable.chatz_logo_small);
    notificationBuilder.setColor(ContextCompat.getColor(context, R.color.chatz_primary));
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

  private static int getAttributeResourceId(Context context, int attribute) {
    if (attribute == 0) {
      return 0;
    }
    TypedValue typedValue = new TypedValue();
    context.getTheme().resolveAttribute(attribute, typedValue, true);
    return typedValue.resourceId;
  }

  public static int dpToPixels(Context context, int dp) {
    Resources resources = context.getResources();
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
  }
}