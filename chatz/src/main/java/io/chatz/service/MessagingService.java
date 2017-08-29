package io.chatz.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.chatz.core.ChatzApp;
import io.chatz.ChatzMessages;

public class MessagingService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(final RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    ChatzApp chatzApp = ChatzApp.getInstance(this);
    if (chatzApp.getSettings().isMessagingServiceEnabled()) {
      ChatzMessages.receive(this, remoteMessage.getData());
    }
  }
}