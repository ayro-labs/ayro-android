package io.chatz.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.chatz.Chatz;
import io.chatz.ChatzMessages;
import io.chatz.ChatzStatus;

public class FcmService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(final RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    Chatz chatz = Chatz.getInstance(this);
    if(ChatzStatus.LOGGED_IN.equals(chatz.getStatus()) && chatz.getSettings().isMessagingServiceEnabled()) {
      ChatzMessages.receive(this, remoteMessage.getData());
    }
  }
}