package io.chatz.service;

import com.google.firebase.iid.FirebaseInstanceIdService;

import io.chatz.core.ChatzApp;
import io.chatz.enums.UserStatus;

public class InstanceIDService extends FirebaseInstanceIdService {

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    ChatzApp chatzApp = ChatzApp.getInstance(this);
    if (chatzApp.getSettings().isMessagingServiceEnabled() && UserStatus.LOGGED_IN.equals(chatzApp.getUserStatus())) {
      ChatzApp.getInstance(this).connectToFirebase();
    }
  }
}