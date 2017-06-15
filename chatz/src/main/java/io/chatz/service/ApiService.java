package io.chatz.service;

import io.chatz.model.User;
import io.chatz.service.payload.AuthenticationPayload;
import io.chatz.model.Device;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

  @POST("/auth/users")
  Call<String> authenticateUser(@Body AuthenticationPayload payload);

  @PUT("/users")
  Call<Void> updateUser(@Header("X-Token") String apiToken, @Body User user);

  @PUT("/users/devices")
  Call<Void> updateDevice(@Header("X-Token") String apiToken, @Body Device device);

}