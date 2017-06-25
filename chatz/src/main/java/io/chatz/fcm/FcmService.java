package io.chatz.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.chatz.Chatz;
import io.chatz.ChatzMessages;
import io.chatz.Status;

public class FcmService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(final RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    Chatz chatz = Chatz.getInstance(this);
    if(chatz.getSettings().isMessagingServiceEnabled() && Status.LOGGED_IN.equals(chatz.getStatus())) {
      ChatzMessages.receive(this, remoteMessage.getData());
    }
  }
}