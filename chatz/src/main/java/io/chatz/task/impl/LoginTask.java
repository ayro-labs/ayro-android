package io.chatz.task.impl;

import android.util.Log;

import io.chatz.task.Task;
import io.chatz.util.Constants;
import io.chatz.service.payload.LoginRequest;
import io.chatz.model.Device;
import io.chatz.model.User;
import io.chatz.service.ApiService;
import io.chatz.service.Services;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTask extends Task<String> {

  private static final String TASK_NAME = "ChatzIO.task.user.login";

  private LoginRequest payload;
  private ApiService apiService;

  public LoginTask(String appToken, User user, Device device) {
    super(TASK_NAME);
    this.payload = new LoginRequest();
    this.payload.setAppToken(appToken);
    this.payload.setUser(user);
    this.payload.setDevice(device);
    this.apiService = Services.getInstance().getApiService();
  }

  @Override
  protected void executeJob() {
    apiService.login(payload).enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        if(response.isSuccessful()) {
          success(response.body());
        } else {
          fail();
        }
      }

      @Override
      public void onFailure(Call<String> call, Throwable throwable) {
        fail();
      }
    });
  }

  @Override
  protected void fail() {
    super.fail();
    Log.e(Constants.TAG, "Could not authenticate user, retrying soon...");
  }
}