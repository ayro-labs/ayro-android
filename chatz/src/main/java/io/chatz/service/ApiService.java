package io.chatz.service;

import java.util.Map;

import io.chatz.model.User;
import io.chatz.service.payload.LoginPayload;
import io.chatz.model.Device;
import io.chatz.service.payload.LoginResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

  @POST("/auth/users")
  Call<LoginResult> login(@Body LoginPayload payload);

  @PUT("/users")
  Call<Void> updateUser(@Header("X-Token") String apiToken, @Body User user);

  @PUT("/users/devices")
  Call<Void> updateDevice(@Header("X-Token") String apiToken, @Body Device device);

  @POST("/chat/android")
  Call<Void> postMessage(@Header("X-Token") String apiToken, @Body Map<String, String> payload);
}