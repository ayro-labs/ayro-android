package io.chatz.task.impl;

import android.content.Context;
import android.util.Log;

import io.chatz.model.User;
import io.chatz.service.ApiService;
import io.chatz.service.Services;
import io.chatz.task.Task;
import io.chatz.util.Constants;
import io.chatz.util.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserTask extends Task<Void> {

  private static final String TASK_NAME = "ChatzIO.task.user.update";

  private Context context;
  private User user;
  private ApiService apiService;

  public UpdateUserTask(Context context, User user) {
    super(TASK_NAME);
    this.context = context;
    this.user = user;
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