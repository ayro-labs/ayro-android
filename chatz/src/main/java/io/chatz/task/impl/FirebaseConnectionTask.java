package io.chatz.task.impl;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import io.chatz.model.Device;
import io.chatz.service.ApiService;
import io.chatz.service.Services;
import io.chatz.task.Task;
import io.chatz.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseConnectionTask extends Task<Void> {

  private static final String TASK_NAME = "ChatzIO.task.firebase";

  private String apiToken;
  private ApiService apiService;

  public FirebaseConnectionTask(String apiToken) {
    super(TASK_NAME);
    this.apiToken = apiToken;
    this.apiService = Services.getInstance().getApiService();
  }

  @Override
  protected void executeJob() {
    String token = FirebaseInstanceId.getInstance().getToken();
    if(token == null) {
      Log.d(Constants.TAG, "Could not obtain Firebase apiToken, retrying soon...");
      fail();
      return;
    }
    Device device = new Device();
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
    Log.d(Constants.TAG, "Could not update device, retrying soon...");
  }
}