package io.ayro.service;

import com.google.firebase.iid.FirebaseInstanceIdService;

import io.ayro.core.AyroApp;

public class InstanceIDService extends FirebaseInstanceIdService {

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    AyroApp ayroApp = AyroApp.getInstance(this);
    if (ayroApp.getSettings().isMessagingServiceEnabled()) {
      AyroApp.getInstance(this).updatePushToken();
    }
  }
}
