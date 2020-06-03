package ru.creativityprojectcenter.groupchatapp.presentation.chat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.creativityprojectcenter.groupchatapp.R;
import ru.creativityprojectcenter.groupchatapp.data.model.Message;
import ru.creativityprojectcenter.groupchatapp.service.MessageService;
import ru.creativityprojectcenter.groupchatapp.util.Preferences;

public class ChatActivity extends AppCompatActivity implements ChatContract.View {

    private final String TAG = getClass().getSimpleName();

    @Inject
    ChatContract.Presenter presenter;

    @Inject
    public Preferences pref;

    private Realm realm;
    private boolean bound;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.messages_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.message_input_layout) LinearLayout messageInputLayout;
    @BindView(R.id.join_chat_button) Button joinChatButton;
    @BindView(R.id.new_message_text) EditText newMessageEditText;
    MessagesAdapter messagesAdapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        messagesAdapter = new MessagesAdapter(this, pref, null);
        recyclerView.setAdapter(messagesAdapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        messagesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                layoutManager.smoothScrollToPosition(recyclerView,
                        null, messagesAdapter.getItemCount());
            }
        });

        joinChatButton.setVisibility(View.INVISIBLE);
        messageInputLayout.setVisibility(View.INVISIBLE);

        int chatId = getIntent().getIntExtra("chatId", 0);
        presenter.setChat(chatId);

        Intent i = new Intent(this, MessageService.class);
        bindService(i, connection, Context.BIND_AUTO_CREATE);
    }

    @OnClick(R.id.join_chat_button)
    public void joinChat(View view) {
        presenter.joinChat();
    }

    @OnClick(R.id.send_button)
    public void sendMessage(View view) {
        String text = newMessageEditText.getText().toString();
        presenter.sendMessage(text);
        newMessageEditText.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMessages(RealmResults<Message> messages) {
        messagesAdapter.updateData(messages);
    }

    @Override
    public void showError(String message) {
        Log.d(TAG, "Error: " + message);
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void setMessageInput(boolean active) {
        if (active) {
            joinChatButton.setVisibility(View.INVISIBLE);
            messageInputLayout.setVisibility(View.VISIBLE);
        } else {
            joinChatButton.setVisibility(View.VISIBLE);
            messageInputLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showChatTitle(String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }

    @Override
    public void showChatMembersCount(int count) {
        toolbar.setSubtitle(String.valueOf(count) + " members");
        setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
        if (bound) {
            unbindService(connection);
            bound = false;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder binder) {
            presenter.serviceConnected(((MessageService.MessageBinder) binder).getService());
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            presenter.serviceDisconnected();
            bound = false;
        }
    };

}
