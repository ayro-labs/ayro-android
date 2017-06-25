package io.chatz.task.impl;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import io.chatz.model.Device;
import io.chatz.service.ApiService;
import io.chatz.service.Services;
import io.chatz.task.Task;
import io.chatz.util.AppUtils;
import io.chatz.util.Constants;
import io.chatz.util.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseConnectionTask extends Task<Void> {

  private static final String TASK_NAME = "ChatzIO.task.firebase.connection";

  private Context context;
  private ApiService apiService;

  public FirebaseConnectionTask(Context context) {
    super(TASK_NAME);
    this.context = context;
    this.apiService = Services.getInstance().getApiService();
  }

  @Override
  protected void executeJob() {
    String apiToken = Preferences.getApiToken(context);
    if(apiToken == null) {
      Log.e(Constants.TAG, "User is not logged in yet, retrying soon...");
      super.fail();
      return;
    }
    String token = FirebaseInstanceId.getInstance().getToken();
    if(token == null) {
      Log.e(Constants.TAG, "Could not obtain Firebase token, retrying soon...");
      super.fail();
      return;
    }
    Device device = new Device();
    device.setUid(AppUtils.getDeviceId(context));
    device.setPushToken(token);
    apiService.updateDevice(apiToken, device).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if(response.isSuccessful()) {
          success(null);
        } else {
          fail();
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable throwable) {
        fail();
      }
    });
  }

  @Override
  protected void fail() {
    super.fail();
    Log.e(Constants.TAG, "Could not update device, retrying soon...");
  }
}