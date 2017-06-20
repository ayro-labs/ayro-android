package io.chatz.task.impl;

import android.util.Log;

import io.chatz.model.User;
import io.chatz.service.ApiService;
import io.chatz.service.Services;
import io.chatz.task.Task;
import io.chatz.util.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserTask extends Task<Void> {

  private static final String TASK_NAME = "ChatzIO.task.user.update";

  private String apiToken;
  private User user;
  private ApiService apiService;

  public UpdateUserTask(String apiToken, User user) {
    super(TASK_NAME);
    this.apiToken = apiToken;
    this.user = user;
    this.apiService = Services.getInstance().getApiService();
  }

  @Override
  protected void executeJob() {
    apiService.updateUser(apiToken, user).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if(response.isSuccessful()) {
          success(response.body());
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
    Log.e(Constants.TAG, "Could not update user, retrying soon...");
  }
}
