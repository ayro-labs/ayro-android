package io.ayro.task.impl;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import io.ayro.model.Device;
import io.ayro.model.User;
import io.ayro.service.AyroService;
import io.ayro.service.payload.LoginPayload;
import io.ayro.service.payload.LoginResult;
import io.ayro.task.Task;
import io.ayro.exception.TaskException;
import io.ayro.util.Constants;
import io.ayro.util.MessageUtils;
import retrofit2.Response;

public class LoginTask extends Task<LoginResult> {

  private static final String TASK_NAME = "login";

  private static final String GENERIC_ERROR_STATUS = "999";
  private static final String GENERIC_ERROR_CODE = "login.error";
  private static final String GENERIC_ERROR_MESSAGE = "Could not sign in";

  private LoginPayload payload;
  private AyroService ayroService;

  public LoginTask(Context context, String appToken, User user, Device device) {
    super(context, TASK_NAME);
    this.payload = new LoginPayload(appToken, user, device);
    this.ayroService = AyroService.getInstance();
  }

  @Override
  protected LoginResult executeJob() throws TaskException {
    try {
      Log.i(Constants.TAG, String.format("(%s) Signing in...", TASK_NAME));
      Response<LoginResult> response = ayroService.login(payload).execute();
      if (response.isSuccessful()) {
        Log.i(Constants.TAG, String.format("(%s) User signed in with success!", TASK_NAME));
        return response.body();
      } else {
        TaskException exception = new TaskException(response, true);
        Log.e(Constants.TAG, String.format("(%s) Could not sign in: %s", TASK_NAME, MessageUtils.get(exception)));
        throw exception;
      }
    } catch (IOException e) {
      TaskException exception = new TaskException(GENERIC_ERROR_STATUS, GENERIC_ERROR_CODE, GENERIC_ERROR_MESSAGE, e, false);
      Log.e(Constants.TAG, String.format("(%s) %s", TASK_NAME, exception.getMessage()));
      throw exception;
    }
  }
}
