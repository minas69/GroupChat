package ru.creativityprojectcenter.groupchatapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.realm.Realm;
import ru.creativityprojectcenter.groupchatapp.data.model.Chat;
import ru.creativityprojectcenter.groupchatapp.data.model.Message;
import ru.creativityprojectcenter.groupchatapp.data.model.User;
import ru.creativityprojectcenter.groupchatapp.data.net.websockets.MessengerRemote;
import ru.creativityprojectcenter.groupchatapp.util.Preferences;

public class MessageService extends Service implements IMessageService {

    private final String TAG = getClass().getSimpleName();

    @Inject
    public Preferences pref;

    @Inject
    public MessengerRemote messengerRemote;

    private final IBinder binder = new MessageBinder();

//    RxWebSocket webSocket;
//    CompositeDisposable disposables = new CompositeDisposable();

    public MessageService() {
    }

    public class MessageBinder extends Binder {
        public IMessageService getService() {
            return MessageService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!messengerRemote.isOpened()) {
            messengerRemote.connect(pref.getToken(), new MessengerRemote.ConnectHandler() {
                @Override
                public void onConnect() {
                    Log.d(TAG, "Connected");
                }

                @Override
                public void onFailure(Throwable t) {
                    onError(t);
                }
            });

            messengerRemote.addChatHandler(new MessengerRemote.ChatHandler() {
                @Override
                public void onMessageReceived(Message message) {
                    addMessage(message);
                }

                @Override
                public void onUserJoined(Chat chat, User user) {

                }

                @Override
                public void onUserLeft(Chat chat, User user) {

                }
            });
        }

        return START_STICKY;
    }

    private void onError(Throwable t) {
        Log.d(TAG, t.getMessage());
    }

    private void onClose() {
        Log.d(TAG, "Connection closed");
    }

    private void addMessage(Message message) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();

            Chat realmChat = realm
                    .where(Chat.class)
                    .equalTo("id", message.getChatId())
                    .findFirst();
            if (realmChat != null) {
                if (message.getCustomType() != null &&
                        message.getCustomType().equals(Message.USER_JOINED)) {
                    realmChat.incrementMembersCount();
                }
                Chat chat = realm.copyFromRealm(realmChat);
                if (chat.isEphemeral()) {
                    message.setId(pref.getLocalMessageId());
                }
                chat.setLastMessage(message);
                realm.insertOrUpdate(chat);
            }

            realm.commitTransaction();
        } finally {
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            realm.close();
        }
    }

    @Override
    public void sendMessage(Message newMessage) {
        messengerRemote.sendMessage(newMessage, new MessengerRemote.SendMessageHandler() {
            @Override
            public void onSent(Message message) {
                addMessage(message);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onDestroy() {
        messengerRemote.disconnect();
    }

}
