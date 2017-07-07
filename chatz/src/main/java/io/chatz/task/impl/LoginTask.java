package io.chatz.task.impl;

import android.util.Log;

import io.chatz.service.payload.LoginResult;
import io.chatz.task.Task;
import io.chatz.util.Constants;
import io.chatz.service.payload.LoginPayload;
import io.chatz.model.Device;
import io.chatz.model.User;
import io.chatz.service.ChatzService;
import io.chatz.service.Services;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTask extends Task<LoginResult> {

  private static final String TASK_NAME = "ChatzIO.task.user.login";

  private LoginPayload payload;
  private ChatzService chatzService;

  public LoginTask(String appToken, User user, Device device) {
    super(TASK_NAME);
    this.payload = new LoginPayload(appToken, user, device);
    this.chatzService = Services.getInstance().getChatzService();
  }

  @Override
  protected boolean shouldBeReplaced() {
    return false;
  }

  @Override
  protected void executeJob() {
    chatzService.login(payload).enqueue(new Callback<LoginResult>() {
      @Override
      public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
        if(response.isSuccessful()) {
          success(response.body());
        } else {
          fail();
        }
      }

      @Override
      public void onFailure(Call<LoginResult> call, Throwable throwable) {
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