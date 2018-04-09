package io.ayro.service;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.ayro.AyroMessages;
import io.ayro.core.AyroApp;

public class MessagingService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    AyroApp ayroApp = AyroApp.getInstance(this);
    if (ayroApp.getSettings().isMessagingServiceEnabled()) {
      AyroMessages.receive(this, remoteMessage);
    }
  }
}
