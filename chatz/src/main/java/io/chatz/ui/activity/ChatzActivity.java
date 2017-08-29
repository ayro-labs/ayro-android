package io.chatz.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import io.chatz.core.ChatzApp;
import io.chatz.R;
import io.chatz.enums.UserStatus;
import io.chatz.model.ChatMessage;
import io.chatz.model.User;
import io.chatz.service.ChatzService;
import io.chatz.service.payload.PostMessagePayload;
import io.chatz.task.TaskCallback;
import io.chatz.ui.adapter.ChatAdapter;
import io.chatz.util.Constants;
import io.chatz.util.UIUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatzActivity extends AppCompatActivity {

  private static final IntentFilter INTENT_FILTER = new IntentFilter();

  static {
    INTENT_FILTER.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    INTENT_FILTER.addAction(Constants.INTENT_ACTION_MESSAGE_RECEIVED);
    INTENT_FILTER.addAction(Constants.INTENT_ACTION_TASKS_CHANGED);
  }

  private ChatzService chatzService;
  private ChatzApp chatzApp;

  private EditText messageInput;
  private RecyclerView chatMessagesView;
  private View postMessageView;
  private ChatAdapter chatAdapter;
  private BroadcastReceiver broadcastReceiver;
  private String currentError;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    chatzService = ChatzService.getInstance();
    chatzApp = ChatzApp.getInstance(this);

    setupBroadcastReceiver();
    setupAdapter();
    setupMessageInput();
    setupSendMessageButton();
    loadContent();

    UIUtils.defaultToolbar(this);
    setTitle(getString(R.string.chatz_activity_title));
  }

  @Override
  protected void onResume() {
    super.onResume();
    chatzApp.setChatOpened(true);
    registerReceiver(broadcastReceiver, INTENT_FILTER);
    updateConnectionStatus();
  }

  @Override
  protected void onPause() {
    super.onPause();
    chatzApp.setChatOpened(false);
    unregisterReceiver(broadcastReceiver);
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

  private void setupSendMessageButton() {
    postMessageView = findViewById(R.id.post_message);
    postMessageView.setEnabled(false);
    postMessageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onPostMessageClick();
      }
    });
  }

  private void loadContent() {
    if (!UserStatus.LOGGED_IN.equals(chatzApp.getUserStatus())) {
      chatzApp.login(chatzApp.getUser(), new TaskCallback<User>() {
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
    chatzService.listMessages(chatzApp.getApiToken()).enqueue(new Callback<List<ChatMessage>>() {
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
        error = getString(R.string.chatz_status_no_internet_connection);
      }
    }
    if (chatzApp.hasPendingTasks()) {
      error = getString(R.string.chatz_status_connecting_to_the_server);
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
    } else {
      postMessageView.setEnabled(true);
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
    final ChatMessage chatMessage = new ChatMessage();
    chatMessage.setText(messageInput.getText().toString());
    chatMessage.setStatus(ChatMessage.Status.sending);
    chatMessage.setDirection(ChatMessage.Direction.outgoing);
    chatMessage.setDate(new Date());
    final int position = chatAdapter.addItem(chatMessage);
    messageInput.setText("");
    chatMessagesView.scrollToPosition(chatAdapter.lastIndex());
    PostMessagePayload payload = new PostMessagePayload(chatMessage.getText());
    chatzService.postMessage(chatzApp.getApiToken(), payload).enqueue(new Callback<ChatMessage>() {
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