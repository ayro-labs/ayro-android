package io.chatz.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import io.chatz.R;
import io.chatz.service.ApiService;
import io.chatz.service.Services;

public class ChatActivity extends AppCompatActivity {

  private ApiService apiService;

  private Button sendMessageButton;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    apiService = Services.getInstance().getApiService();

    setupViews();

    setTitle(getString(R.string.activity_chat_title));
  }

  private void setupViews() {
    //    sendMessageButton = findViewById(R.id.send_message);
  }
}