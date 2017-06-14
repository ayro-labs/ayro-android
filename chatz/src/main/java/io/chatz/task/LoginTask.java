package io.chatz.task;

import android.util.Log;

import io.chatz.util.Constants;
import io.chatz.model.AuthenticationPayload;
import io.chatz.model.Device;
import io.chatz.model.User;
import io.chatz.service.ApiService;
import io.chatz.service.Services;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTask extends Task<String> {

  private static final String THREAD_NAME = "ChatzIO.task.login";

  private AuthenticationPayload payload;
  private ApiService apiService;

  public LoginTask(String projectToken, User user, Device device) {
    super(THREAD_NAME);
    this.apiService = Services.getInstance().getApiService();
    this.payload = new AuthenticationPayload();
    this.payload.setProjectToken(projectToken);
    this.payload.setUser(user);
    this.payload.setDevice(device);
  }

  @Override
  public void executeJob() {
    apiService.authenticateUser(payload).enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        success(response.body());
      }

      @Override
      public void onFailure(Call<String> call, Throwable throwable) {
        Log.e(Constants.TAG, "Could not authenticate user, retrying soon...");
        fail();
      }
    });
  }
}