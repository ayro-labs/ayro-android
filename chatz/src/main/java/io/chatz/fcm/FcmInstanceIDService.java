package io.chatz.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;

import io.chatz.Chatz;
import io.chatz.Status;

public class FcmInstanceIDService extends FirebaseInstanceIdService {

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    Chatz chatz = Chatz.getInstance(this);
    if(chatz.getSettings().isMessagingServiceEnabled() && Status.LOGGED_IN.equals(chatz.getStatus())) {
      Chatz.getInstance(this).connectToFirebase();
    }
  }
}