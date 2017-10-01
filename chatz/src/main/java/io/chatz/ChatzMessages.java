package io.chatz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.Map;

import io.chatz.core.ChatzApp;
import io.chatz.enums.UserStatus;
import io.chatz.model.Agent;
import io.chatz.model.ChatMessage;
import io.chatz.ui.activity.ChatzActivity;
import io.chatz.util.Callback;
import io.chatz.util.Constants;
import io.chatz.util.ImageUtils;
import io.chatz.util.JsonUtils;
import io.chatz.util.UIUtils;

public class ChatzMessages {

  private static final String KEY_ORIGIN = "origin";
  private static final String KEY_EVENT = "event";
  private static final String KEY_MESSAGE = "message";

  private static final String ORIGIN_CHATZ = "chatz";
  private static final String EVENT_CHAT_MESSAGE = "chat_message";

  public static void receive(Context context, Map<String, String> data) {
    ChatzApp chatzApp = ChatzApp.getInstance(context);
    if (UserStatus.LOGGED_IN.equals(chatzApp.getUserStatus()) && ORIGIN_CHATZ.equals(data.get(KEY_ORIGIN))) {
      String event = data.get(KEY_EVENT);
      String message = data.get(KEY_MESSAGE);
      Log.d(Constants.TAG, "Incoming message of event " + event + ": " + message);
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
    if (!ChatzApp.getInstance(context).isChatOpened()) {
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
    return new Intent(context, ChatzActivity.class);
  }
}
