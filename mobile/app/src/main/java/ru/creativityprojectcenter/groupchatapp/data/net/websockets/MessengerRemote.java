package ru.creativityprojectcenter.groupchatapp.data.net.websockets;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.creativityprojectcenter.groupchatapp.BuildConfig;
import ru.creativityprojectcenter.groupchatapp.data.model.Chat;
import ru.creativityprojectcenter.groupchatapp.data.model.Message;
import ru.creativityprojectcenter.groupchatapp.data.model.User;

public class MessengerRemote {

    private final String TAG = getClass().getSimpleName();

    private List<ChatHandler> chatHandlers = new ArrayList<>();
    private Map<Long, SendMessageHandler> sendMessageHandlers = new LinkedHashMap<>();
    private Disposable chatHandlerDisposable;

    private RxWebSocket webSocket;

    private Gson gson;

    @Inject
    public MessengerRemote() {
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public boolean isOpened() {
        return webSocket != null && webSocket.isOpened();
    }

    public void connect(String accessToken, ConnectHandler handler) {
        webSocket = new RxWebSocket.Builder()
                .addConverterFactory(WebSocketConverterFactory.create(gson))
                .build(BuildConfig.WS_ENDPOINT + accessToken);

        setListeners();

        Disposable disposable = webSocket.connect()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(open -> handler.onConnect(), handler::onFailure);
    }

    public void sendMessage(Message message, SendMessageHandler handler) {
        webSocket.send(message).subscribe(() -> {}, Throwable::printStackTrace);
    }

    public void disconnect() {
        Disposable disposable = webSocket
                .disconnect(3000, "Disconnect")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(closed -> {}, Throwable::printStackTrace);
    }

    private void setListeners() {
        chatHandlerDisposable = webSocket
                .eventStream()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
                            if (event instanceof RxWebSocket.Message) {
                                onMessage((RxWebSocket.Message) event);
                            }
                        },
                        throwable -> {
                            Log.d(TAG, throwable.getMessage());
                        });
    }

    private void onMessage(RxWebSocket.Message rxMessage) {
        try {
            Message message = rxMessage.data(Message.class);
            for (ChatHandler handler : chatHandlers) {
                handler.onMessageReceived(message);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void addChatHandler(ChatHandler handler) {
        chatHandlers.add(handler);
    }

    public void removeChatHandler(ChatHandler handler) {
        chatHandlers.remove(handler);
    }

    public interface ChatHandler {

        void onMessageReceived(Message message);

        void onUserJoined(Chat chat, User user);

        void onUserLeft(Chat chat, User user);

    }

    public interface ConnectHandler {

        void onConnect();

        void onFailure(Throwable t);

    }

    public interface SendMessageHandler {

        void onSent(Message message);

        void onFailure(Throwable t);

    }

}
