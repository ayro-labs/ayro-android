package io.chatz.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.chatz.core.ChatzApp;
import io.chatz.ChatzMessages;
import io.chatz.enums.UserStatus;

public class MessagingService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(final RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    ChatzApp chatzApp = ChatzApp.getInstance(this);
    if (chatzApp.getSettings().isMessagingServiceEnabled() && UserStatus.LOGGED_IN.equals(chatzApp.getUserStatus())) {
      ChatzMessages.receive(this, remoteMessage.getData());
    }
  }
}