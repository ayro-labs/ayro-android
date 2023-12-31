package io.ayro.task.impl;

import android.content.Context;
import android.util.Log;

import io.ayro.exception.TaskException;
import io.ayro.model.Device;
import io.ayro.model.User;
import io.ayro.service.AyroService;
import io.ayro.service.payload.LoginPayload;
import io.ayro.service.payload.LoginResult;
import io.ayro.store.Store;
import io.ayro.task.Task;
import io.ayro.util.Constants;
import io.ayro.util.MessageUtils;
import retrofit2.Response;

public class LoginTask extends Task<LoginResult> {

  private static final String TASK_NAME = "user.login";

  private static final String GENERIC_ERROR_CODE = "login_error";
  private static final String GENERIC_ERROR_MESSAGE = "Could not sign in";

  private LoginPayload payload;
  private AyroService service;

  public LoginTask(Context context, String appToken, String jwtToken, User user, Device device) {
    super(context, TASK_NAME);
    this.payload = new LoginPayload(appToken, jwtToken, user, device);
    this.service = AyroService.getInstance();
  }

  @Override
  protected LoginResult executeJob() throws TaskException {
    try {
      Log.i(Constants.TAG, String.format("(%s) Signing in...", TASK_NAME));
      String apiToken = Store.getApiToken(getContext());
      if (apiToken == null) {
        return null;
      }
      Response<LoginResult> response = service.login(apiToken, payload).execute();
      if (response.isSuccessful()) {
        Log.i(Constants.TAG, String.format("(%s) User signed in with success!", TASK_NAME));
        return response.body();
      }
      TaskException exception = new TaskException(response, true);
      Log.e(Constants.TAG, String.format("(%s) Could not sign in: %s", TASK_NAME, MessageUtils.get(exception)));
      throw exception;
    } catch (TaskException e) {
      throw e;
    } catch (Exception e) {
      TaskException exception = new TaskException(GENERIC_ERROR_CODE, GENERIC_ERROR_MESSAGE, e, false);
      Log.e(Constants.TAG, String.format("(%s) %s", TASK_NAME, exception.getMessage()));
      throw exception;
    }
  }
}
