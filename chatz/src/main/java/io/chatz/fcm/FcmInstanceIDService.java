package io.chatz.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;

import io.chatz.Chatz;

public class FcmInstanceIDService extends FirebaseInstanceIdService {

  @Override
  public void onTokenRefresh() {
    super.onTokenRefresh();
    Chatz.getInstance(this).connectFirebase();
  }
}