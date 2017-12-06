package io.ayro.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import io.ayro.core.AyroApp;
import io.ayro.R;
import io.ayro.enums.UserStatus;
import io.ayro.model.ChatMessage;
import io.ayro.model.Integration;
import io.ayro.model.User;
import io.ayro.service.AyroService;
import io.ayro.service.payload.PostMessagePayload;
import io.ayro.task.TaskCallback;
import io.ayro.ui.adapter.ChatAdapter;
import io.ayro.util.Constants;
import io.ayro.util.UIUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AyroActivity extends AppCompatActivity {

  private static final IntentFilter INTENT_FILTER = new IntentFilter();

  static {
    INTENT_FILTER.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    INTENT_FILTER.addAction(Constants.INTENT_ACTION_MESSAGE_RECEIVED);
    INTENT_FILTER.addAction(Constants.INTENT_ACTION_TASKS_CHANGED);
  }

  private AyroService ayroService;
  private AyroApp ayroApp;

  private EditText messageInput;
  private RecyclerView chatMessagesView;
  private ImageView postMessageView;
  private ChatAdapter chatAdapter;
  private BroadcastReceiver broadcastReceiver;
  private String currentError;

  private Drawable postMessageEnabledDrawable;
  private Drawable postMessageDisabledDrawable;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ayro_activity_chat);

    ayroService = AyroService.getInstance();
    ayroApp = AyroApp.getInstance(this);

    setupToolbar();
    setupBroadcastReceiver();
    setupAdapter();
    setupMessageInput();
    setupPostMessageButton();
    loadContent();

    setTitle(getString(R.string.ayro_activity_title));
  }

  @Override
  protected void onResume() {
    super.onResume();
    ayroApp.setChatOpened(true);
    registerReceiver(broadcastReceiver, INTENT_FILTER);
    updateConnectionStatus();
  }

  @Override
  protected void onPause() {
    super.onPause();
    ayroApp.setChatOpened(false);
    unregisterReceiver(broadcastReceiver);
  }

  private void setupToolbar() {
    Integer primaryColor = null;
    Integration integration = ayroApp.getIntegration();
    if (integration != null) {
      String colorHex = integration.getConfiguration().get(Integration.PRIMARY_COLOR_CONFIGURATION);
      primaryColor = Color.parseColor(colorHex);
    }
    UIUtils.defaultToolbar(this, primaryColor);
    UIUtils.changeStatusBarColor(this, primaryColor);
  }

  private void setupBroadcastReceiver() {
    broadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        Object data = intent.getSerializableExtra(Constants.INTENT_ACTION_EXTRA_DATA);
        switch (intent.getAction()) {
          case ConnectivityManager.CONNECTIVITY_ACTION:
          case Constants.INTENT_ACTION_TASKS_CHANGED:
            updateConnectionStatus();
            break;
          case Constants.INTENT_ACTION_MESSAGE_RECEIVED:
            onMessageReceived((ChatMessage) data);
            break;
        }
      }
    };
  }

  private void setupAdapter() {
    chatAdapter = new ChatAdapter(this);
    chatAdapter.setOnRetryMessageClickListener(new ChatAdapter.OnRetryMessageClickListener() {
      @Override
      public void onClick(int position, ChatMessage chatMessage) {
        onRetryMessageClick(position, chatMessage);
      }
    });
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    linearLayoutManager.setStackFromEnd(true);
    chatMessagesView = (RecyclerView) findViewById(R.id.chat_messages);
    chatMessagesView.setLayoutManager(linearLayoutManager);
    chatMessagesView.setAdapter(chatAdapter);
  }

  private void setupMessageInput() {
    messageInput = (EditText) findViewById(R.id.message);
    messageInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence text, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence text, int start, int before, int count) {
        onMessageChanged(text.toString());
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
  }

  private void setupPostMessageButton() {
    postMessageView = (ImageView) findViewById(R.id.post_message);
    postMessageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onPostMessageClick();
      }
    });
    postMessageEnabledDrawable = ContextCompat.getDrawable(this, R.drawable.ayro_post_message).mutate();
    DrawableCompat.setTint(postMessageEnabledDrawable, ContextCompat.getColor(this, R.color.ayro_send_button_enabled));
    postMessageDisabledDrawable = ContextCompat.getDrawable(this, R.drawable.ayro_post_message).mutate();
    DrawableCompat.setTint(postMessageDisabledDrawable, ContextCompat.getColor(this, R.color.ayro_send_button_disabled));
    onMessageChanged("");
  }

  private void loadContent() {
    if (!UserStatus.LOGGED_IN.equals(ayroApp.getUserStatus())) {
      ayroApp.login(ayroApp.getUser(), new TaskCallback<User>() {
        @Override
        public void onSuccess(User user) {
          loadMessages();
        }
      });
    } else {
      loadMessages();
    }
  }

  private void loadMessages() {
    ayroService.listMessages(ayroApp.getApiToken()).enqueue(new Callback<List<ChatMessage>>() {
      @Override
      public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
        if (response.isSuccessful()) {
          chatAdapter.setItems(response.body());
        }
      }

      @Override
      public void onFailure(Call<List<ChatMessage>> call, Throwable throwable) {

      }
    });
  }

  private void updateConnectionStatus() {
    String error = null;
    ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager != null) {
      NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
      if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
        error = getString(R.string.ayro_activity_status_no_internet_connection);
      }
    }
    if (ayroApp.hasPendingTasks()) {
      error = getString(R.string.ayro_activity_status_connecting_to_the_servers);
    }
    if (error != null) {
      currentError = error;
      showErrorBar(error);
      onMessageChanged(messageInput.getText().toString());
    } else {
      hideErrorBar();
    }
  }

  private void onMessageReceived(ChatMessage chatMessage) {
    chatAdapter.addItem(chatMessage);
    chatMessagesView.scrollToPosition(chatAdapter.lastIndex());
  }

  private void onMessageChanged(String message) {
    if (currentError != null || message.trim().isEmpty()) {
      postMessageView.setEnabled(false);
      postMessageView.setImageDrawable(postMessageDisabledDrawable);
    } else {
      postMessageView.setEnabled(true);
      postMessageView.setImageDrawable(postMessageEnabledDrawable);
    }
  }

  public void showErrorBar(String text) {
    findViewById(R.id.status).setVisibility(View.VISIBLE);
    TextView textView = (TextView) findViewById(R.id.status_text);
    textView.setText(text);
  }

  public void hideErrorBar() {
    findViewById(R.id.status).setVisibility(View.GONE);
  }

  private void onPostMessageClick() {
    postMessage(messageInput.getText().toString());
    messageInput.setText("");
  }

  private void onRetryMessageClick(int position, ChatMessage chatMessage) {
    chatAdapter.removeItem(position);
    postMessage(chatMessage.getText());
  }

  private void postMessage(String text) {
    final ChatMessage chatMessage = new ChatMessage();
    chatMessage.setText(text);
    chatMessage.setStatus(ChatMessage.Status.sending);
    chatMessage.setDirection(ChatMessage.Direction.outgoing);
    chatMessage.setDate(new Date());
    final int position = chatAdapter.addItem(chatMessage);
    chatMessagesView.scrollToPosition(chatAdapter.lastIndex());
    PostMessagePayload payload = new PostMessagePayload(chatMessage.getText());
    ayroService.postMessage(ayroApp.getApiToken(), payload).enqueue(new Callback<ChatMessage>() {
      @Override
      public void onResponse(Call<ChatMessage> call, Response<ChatMessage> response) {
        chatMessage.setStatus(response.isSuccessful() ? ChatMessage.Status.sent : ChatMessage.Status.error);
        chatAdapter.reloadItem(position);
      }

      @Override
      public void onFailure(Call<ChatMessage> call, Throwable throwable) {
        chatMessage.setStatus(ChatMessage.Status.error);
        chatAdapter.reloadItem(position);
      }
    });
  }
}
