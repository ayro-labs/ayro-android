package io.ayro;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import io.ayro.core.AyroApp;
import io.ayro.enums.UserStatus;
import io.ayro.model.Agent;
import io.ayro.model.ChatMessage;
import io.ayro.ui.activity.AyroActivity;
import io.ayro.util.Callback;
import io.ayro.util.Constants;
import io.ayro.util.ImageUtils;
import io.ayro.util.JsonUtils;
import io.ayro.util.UIUtils;

public class AyroMessages {

  private static final String KEY_ORIGIN = "origin";
  private static final String KEY_EVENT = "event";
  private static final String KEY_MESSAGE = "message";

  private static final String ORIGIN_AYRO = "ayro";
  private static final String EVENT_CHAT_MESSAGE = "chat_message";

  public static boolean fromAyro(RemoteMessage remoteMessage) {
    return ORIGIN_AYRO.equals(remoteMessage.getData().get(KEY_ORIGIN));
  }

  public static void receive(Context context, RemoteMessage remoteMessage) {
    AyroApp ayroApp = AyroApp.getInstance(context);
    if (fromAyro(remoteMessage) && UserStatus.LOGGED_IN.equals(ayroApp.getUserStatus())) {
      Map<String, String> data = remoteMessage.getData();
      String event = data.get(KEY_EVENT);
      String message = data.get(KEY_MESSAGE);
      switch (event) {
        case (EVENT_CHAT_MESSAGE):
          ChatMessage chatMessage = JsonUtils.fromJson(message, ChatMessage.class);
          notifyMessage(context, chatMessage);
          break;
      }
    }
  }

  private static void notifyMessage(final Context context, final ChatMessage chatMessage) {
    chatMessage.setStatus(ChatMessage.Status.sent);
    chatMessage.setDirection(ChatMessage.Direction.incoming);
    if (!AyroApp.getInstance(context).isChatOpened()) {
      final Agent agent = chatMessage.getAgent();
      ImageUtils.loadPicture(context, agent.getPhotoUrl(), new Callback<Bitmap>() {
        @Override
        public void onSuccess(Bitmap photo) {
          int notificationId = agent.getId().hashCode();
          UIUtils.notify(context, notificationId, photo, agent.getName(), chatMessage.getText(), getDefaultNotificationIntent(context));
        }

        @Override
        public void onFail(Exception exception) {

        }
      });
    } else {
      Intent intent = new Intent(Constants.INTENT_ACTION_MESSAGE_RECEIVED);
      intent.putExtra(Constants.INTENT_ACTION_EXTRA_DATA, chatMessage);
      context.sendBroadcast(intent);
    }
  }

  private static Intent getDefaultNotificationIntent(Context context) {
    return new Intent(context, AyroActivity.class);
  }
}
