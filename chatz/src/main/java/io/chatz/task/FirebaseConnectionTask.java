package io.chatz.task;

import com.google.firebase.iid.FirebaseInstanceId;

import io.chatz.model.Device;
import io.chatz.service.ApiService;
import io.chatz.service.Services;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseConnectionTask extends Task<Void> {

  private static final String THREAD_NAME = "ChatzIO.task.firebase.connection";

  private ApiService apiService;

  public FirebaseConnectionTask() {
    super(THREAD_NAME);
    this.apiService = Services.getInstance().getApiService();
  }

  @Override
  public void executeJob() {
    String token = FirebaseInstanceId.getInstance().getToken();
    Device device = new Device();
    device.setPushToken(token);
    apiService.updateDevice(device).enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        success(null);
      }

      @Override
      public void onFailure(Call<String> call, Throwable throwable) {
        fail();
      }
    });
  }
}