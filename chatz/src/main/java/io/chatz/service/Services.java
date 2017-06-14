package io.chatz.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.chatz.util.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Services {

  private static Services instance;

  private ApiService apiService;

  private Services() {
    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
    apiService = retrofit.create(ApiService.class);
  }

  public static Services getInstance() {
    if(instance == null) {
      instance = new Services();
    }
    return instance;
  }

  public ApiService getApiService() {
    return apiService;
  }
}