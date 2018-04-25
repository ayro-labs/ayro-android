package io.ayro.task.impl;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import io.ayro.exception.TaskException;
import io.ayro.model.Device;
import io.ayro.service.AyroService;
import io.ayro.store.Store;
import io.ayro.task.Task;
import io.ayro.util.AppUtils;
import io.ayro.util.Constants;
import io.ayro.util.MessageUtils;
import retrofit2.Response;

public class UpdatePushTokenTask extends Task<Device> {

  private static final String TASK_NAME = "device.update.pushToken";

  private static final String GENERIC_ERROR_CODE = "push_token_update_error";
  private static final String GENERIC_ERROR_MESSAGE = "Could not update push token";

  private static final String NO_TOKEN_ERROR_CODE = "could_not_obtain_push_token";
  private static final String NO_TOKEN_ERROR_MESSAGE = "Could not obtain push token";

  private Context context;
  private AyroService ayroService;

  public UpdatePushTokenTask(Context context) {
    super(context, TASK_NAME);
    this.context = context;
    this.ayroService = AyroService.getInstance();
  }

  @Override
  protected Device executeJob() throws TaskException {
    try {
      Log.i(Constants.TAG, String.format("(%s) Updating push token...", TASK_NAME));
      String token = FirebaseInstanceId.getInstance().getToken();
      if (token == null) {
        TaskException exception = new TaskException(NO_TOKEN_ERROR_CODE, NO_TOKEN_ERROR_MESSAGE, false);
        Log.e(Constants.TAG, String.format("(%s) %s", TASK_NAME, exception.getMessage()));
        throw exception;
      }
      Device device = new Device();
      device.setUid(AppUtils.getDeviceUid(context));
      device.setPushToken(token);
      String apiToken = Store.getApiToken(context);
      Response<Device> response = ayroService.updateDevice(apiToken, device).execute();
      if (response.isSuccessful()) {
        Log.i(Constants.TAG, String.format("(%s) Push token updated with success!", TASK_NAME));
        return response.body();
      }
      TaskException exception = new TaskException(response, true);
      Log.e(Constants.TAG, String.format("(%s) Could not update push token: %s", TASK_NAME, MessageUtils.get(exception)));
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
