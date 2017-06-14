package io.chatz.service;

import io.chatz.model.AuthenticationPayload;
import io.chatz.model.Device;
import io.chatz.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

  @POST("/auth/users")
  Call<String> authenticateUser(@Body AuthenticationPayload payload);

  @PUT("/users")
  Call<String> updateUser(@Body User user);

  @PUT("/devices")
  Call<String> updateDevice(@Body Device device);

}