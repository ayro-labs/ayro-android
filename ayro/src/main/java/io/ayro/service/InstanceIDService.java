package io.ayro.service;

import com.google.firebase.iid.FirebaseInstanceIdService;

import io.ayro.core.AyroApp;
import io.ayro.enums.UserStatus;

public class InstanceIDService extends FirebaseInstanceIdService {

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    AyroApp ayroApp = AyroApp.getInstance(this);
    if (ayroApp.getSettings().isMessagingServiceEnabled() && UserStatus.LOGGED_IN.equals(ayroApp.getUserStatus())) {
      AyroApp.getInstance(this).connectToFirebase();
    }
  }
}
