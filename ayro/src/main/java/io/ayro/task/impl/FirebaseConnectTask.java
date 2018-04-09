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

public class FirebaseConnectTask extends Task<Device> {

  private static final String TASK_NAME = "firebase.connect";

  private static final String GENERIC_ERROR_STATUS = "999";
  private static final String GENERIC_ERROR_CODE = "firebase.connect.error";
  private static final String GENERIC_ERROR_MESSAGE = "Could not connect to Firebase";

  private static final String NO_TOKEN_ERROR_STATUS = "998";
  private static final String NO_TOKEN_ERROR_CODE = "firebase.connect.noToken";
  private static final String NO_TOKEN_ERROR_MESSAGE = "Could not obtain Firebase token";

  private Context context;
  private AyroService ayroService;

  public FirebaseConnectTask(Context context) {
    super(context, TASK_NAME);
    this.context = context;
    this.ayroService = AyroService.getInstance();
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
    device.setUid(AppUtils.getDevice(context).getUid());
    device.setPushToken(token);
    try {
      String apiToken = Store.getApiToken(context);
      Response<Device> response = ayroService.updateDevice(apiToken, device).execute();
      if (response.isSuccessful()) {
        Log.i(Constants.TAG, String.format("(%s) Firebase connected with success!", TASK_NAME));
        return response.body();
      } else {
        TaskException exception = new TaskException(response, true);
        Log.e(Constants.TAG, String.format("(%s) Could not connect to Firebase: %s", TASK_NAME, MessageUtils.get(exception)));
        throw exception;
      }
    } catch (Exception e) {
      TaskException exception = new TaskException(GENERIC_ERROR_STATUS, GENERIC_ERROR_CODE, GENERIC_ERROR_MESSAGE, e, false);
      Log.e(Constants.TAG, String.format("(%s) %s", TASK_NAME, exception.getMessage()));
      throw exception;
    }
  }
}
