package io.chatz.core.service;

import io.chatz.core.model.AuthenticationPayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

  @POST("/auth/customers")
  Call<String> authenticateCustomer(@Body AuthenticationPayload payload);

}