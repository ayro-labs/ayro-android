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
import io.chatz.database.ChatMessageDatabase;
import io.chatz.model.ChatMessage;
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

  private EditText messageInput;
  private RecyclerView chatMessagesView;
  private View sendMessageView;

  private ChatAdapter chatAdapter;
  private ChatMessageDatabase chatMessageDatabase;
  private BroadcastReceiver broadcastReceiver;


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    chatMessageDatabase = new ChatMessageDatabase(this);

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
    sendMessageView = findViewById(R.id.send_message);
    sendMessageView.setEnabled(false);
    sendMessageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onSendMessageClick();
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
      sendMessageView.setEnabled(false);
    } else {
      sendMessageView.setEnabled(true);
    }
  }

  private void onSendMessageClick() {
    Chatz chatz = Chatz.getInstance(this);
    final ChatMessage chatMessage = new ChatMessage();
    chatMessage.setUserName(chatz.getUser().getFullName());
    chatMessage.setText(messageInput.getText().toString());
    chatMessage.setStatus(ChatMessage.Status.SENDING);
    chatMessage.setDirection(ChatMessage.Direction.OUTGOING);
    chatMessage.setDate(new Date());
    final int position = chatAdapter.addItem(chatMessage);
    messageInput.setText("");
    chatMessagesView.scrollToPosition(0);
    Chatz.getInstance(this).postMessage(chatMessage).enqueue(new Callback<Void>() {
      @Override
      public void onResponse(Call<Void> call, Response<Void> response) {
        if(response.isSuccessful()) {
          chatMessage.setStatus(ChatMessage.Status.SENT);
          chatAdapter.reloadItem(position);
          chatMessageDatabase.insert(chatMessage);
        } else {
          chatMessage.setStatus(ChatMessage.Status.ERROR_SENDING);
          chatAdapter.reloadItem(position);
        }
      }

      @Override
      public void onFailure(Call<Void> call, Throwable throwable) {
        chatMessage.setStatus(ChatMessage.Status.ERROR_SENDING);
        chatAdapter.reloadItem(position);
      }
    });
  }

  private void loadChatMessages() {
    chatAdapter.setItems(chatMessageDatabase.list());
  }
}