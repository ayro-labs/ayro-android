package io.chatz.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.chatz.Chatz;
import io.chatz.ChatzMessages;

public class FcmService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(final RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    if(Chatz.getInstance(this).getSettings().isMessagingServiceEnabled()) {
      ChatzMessages.receive(this, remoteMessage.getData());
    }
  }
}