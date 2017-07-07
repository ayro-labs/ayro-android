package io.chatz.service;

import io.chatz.util.Constants;
import io.chatz.util.JsonUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Services {

  private static Services instance;

  private ChatzService chatzService;

  private Services() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL).addConverterFactory(GsonConverterFactory.create(JsonUtils.getGson())).build();
    chatzService = retrofit.create(ChatzService.class);
  }

  public static Services getInstance() {
    if(instance == null) {
      instance = new Services();
    }
    return instance;
  }

  public ChatzService getChatzService() {
    return chatzService;
  }
}