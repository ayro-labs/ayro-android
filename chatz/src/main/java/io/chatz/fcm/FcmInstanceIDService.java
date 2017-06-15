package io.chatz.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;

import io.chatz.Chatz;
import io.chatz.ChatzStatus;

public class FcmInstanceIDService extends FirebaseInstanceIdService {

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    Chatz chatz = Chatz.getInstance(this);
    if(ChatzStatus.LOGGED_IN.equals(chatz.getStatus())) {
      Chatz.getInstance(this).connectToFirebase();
    }
  }
}