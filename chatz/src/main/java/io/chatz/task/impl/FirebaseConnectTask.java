package io.chatz.task.impl;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import io.chatz.model.Device;
import io.chatz.service.ChatzService;
import io.chatz.store.Store;
import io.chatz.task.Task;
import io.chatz.exception.TaskException;
import io.chatz.util.AppUtils;
import io.chatz.util.Constants;
import io.chatz.util.MessageUtils;
import retrofit2.Response;

public class FirebaseConnectTask extends Task<Device> {

  private static final String TASK_NAME = "firebase.connect";

  private static final String GENERIC_ERROR_STATUS = "999";
  private static final String GENERIC_ERROR_CODE = "firebase.connect.error";
  private static final String GENERIC_ERROR_MESSAGE = "Could not connect to Firebase";

  private static final String NO_TOKEN_ERROR_STATUS = "998";
  private static final String NO_TOKEN_ERROR_CODE = "firebase.connect.noToken";
  private static final String NO_TOKEN_ERROR_MESSAGE = "Could not obtain Firebase token";

  private Context context;
  private ChatzService chatzService;

  public FirebaseConnectTask(Context context) {
    super(context, TASK_NAME);
    this.context = context;
    this.chatzService = ChatzService.getInstance();
  }

  @Override
  protected Device executeJob() throws TaskException {
    Log.i(Constants.TAG, String.format("(%s) Connecting to Firebase...", TASK_NAME));
    String token = FirebaseInstanceId.getInstance().getToken();
    if (token == null) {
      TaskException exception = new TaskException(NO_TOKEN_ERROR_STATUS, NO_TOKEN_ERROR_CODE, NO_TOKEN_ERROR_MESSAGE, false);
      Log.e(Constants.TAG, String.format("(%s) %s", TASK_NAME, exception.getMessage()));
      throw exception;
    }
    Device device = new Device();
    device.setUid(AppUtils.getDeviceId(context));
    device.setPushToken(token);
    try {
      String apiToken = Store.getApiToken(context);
      Response<Device> response = chatzService.updateDevice(apiToken, device).execute();
      if (response.isSuccessful()) {
        Log.d(Constants.TAG, String.format("(%s) Firebase was connected with success", TASK_NAME));
        return response.body();
      } else {
        TaskException exception = new TaskException(response, true);
        Log.e(Constants.TAG, String.format("(%s) Could not connect to Firebase: %s", TASK_NAME, MessageUtils.get(exception)));
        throw exception;
      }
    } catch (IOException e) {
      TaskException exception = new TaskException(GENERIC_ERROR_STATUS, GENERIC_ERROR_CODE, GENERIC_ERROR_MESSAGE, e, false);
      Log.e(Constants.TAG, String.format("(%s) %s", TASK_NAME, exception.getMessage()));
      throw exception;
    }
  }
}
