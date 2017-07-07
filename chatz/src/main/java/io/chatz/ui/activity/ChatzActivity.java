package io.chatz.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.chatz.Chatz;
import io.chatz.R;
import io.chatz.model.ChatMessage;
import io.chatz.service.ChatzService;
import io.chatz.service.Services;
import io.chatz.service.payload.PostMessagePayload;
import io.chatz.ui.adapter.ChatAdapter;
import io.chatz.util.Constants;
import io.chatz.util.UIUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatzActivity extends AppCompatActivity {

  private static final List<String> ACTIONS = new ArrayList<>();
  static {
    ACTIONS.add(Constants.INTENT_ACTION_MESSAGE_RECEIVED);
  }

  private ChatzService chatzService;
  private String apiToken;

  private EditText messageInput;
  private RecyclerView chatMessagesView;
  private View postMessageView;
  private ChatAdapter chatAdapter;
  private BroadcastReceiver broadcastReceiver;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    chatzService = Services.getInstance().getChatzService();
    apiToken = Chatz.getInstance(this).getApiToken();

    setupMessageInput();
    setupSendMessageButton();
    setupAdapter();
    setupBroadcastReceiver();

    UIUtils.defaultToolbar(this);
    setTitle(getString(R.string.chatz_activity_title));
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

  private void setupAdapter() {
    chatAdapter = new ChatAdapter(this);
    chatMessagesView = (RecyclerView) findViewById(R.id.chat_messages);
    chatMessagesView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
    chatMessagesView.setAdapter(chatAdapter);
  }

  private void setupBroadcastReceiver() {
    broadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        Object data = intent.getSerializableExtra(Constants.INTENT_ACTION_EXTRA_DATA);
        switch(intent.getAction()) {
          case Constants.INTENT_ACTION_MESSAGE_RECEIVED:
            onMessageReceived((ChatMessage) data);
            break;
        }
      }
    };
  }

  @Override
  protected void onResume() {
    super.onResume();
    registerReceiver();
    loadChatMessages();
    Chatz.getInstance(this).setChatOpened(true);
  }

  @Override
  protected void onPause() {
    super.onPause();
    unregisterReceiver();
    Chatz.getInstance(this).setChatOpened(false);
  }

  private void registerReceiver() {
    IntentFilter intentFilter = new IntentFilter();
    for(String action : ACTIONS) {
      intentFilter.addAction(action);
    }
    registerReceiver(broadcastReceiver, intentFilter);
  }

  private void unregisterReceiver() {
    unregisterReceiver(broadcastReceiver);
  }

  private void onMessageReceived(ChatMessage chatMessage) {
    chatAdapter.addItem(chatMessage);
    chatMessagesView.scrollToPosition(0);
  }

  private void onMessageChanged(String message) {
    if(message.trim().isEmpty()) {
      postMessageView.setEnabled(false);
    } else {
      postMessageView.setEnabled(true);
    }
  }

  private void onPostMessageClick() {
    final ChatMessage chatMessage = new ChatMessage();
    chatMessage.setText(messageInput.getText().toString());
    chatMessage.setStatus(ChatMessage.Status.SENDING);
    chatMessage.setDirection(ChatMessage.Direction.OUTGOING);
    chatMessage.setDate(new Date());
    final int position = chatAdapter.addItem(chatMessage);
    messageInput.setText("");
    chatMessagesView.scrollToPosition(0);

    PostMessagePayload payload = new PostMessagePayload(chatMessage.getText());
    chatzService.postMessage(apiToken, payload).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if(response.isSuccessful()) {
          chatMessage.setStatus(ChatMessage.Status.SENT);
        } else {
          chatMessage.setStatus(ChatMessage.Status.ERROR_SENDING);
        }
        chatAdapter.reloadItem(position);
      }

      @Override
      public void onFailure(Call<Void> call, Throwable throwable) {
        chatMessage.setStatus(ChatMessage.Status.ERROR_SENDING);
        chatAdapter.reloadItem(position);
      }
    });
  }

  private void loadChatMessages() {
    chatzService.listMessages(apiToken).enqueue(new Callback<List<ChatMessage>>() {
      @Override
      public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
        if(response.isSuccessful()) {
          chatAdapter.setItems(response.body());
        }
      }

      @Override
      public void onFailure(Call<List<ChatMessage>> call, Throwable t) {

      }
    });
  }
}