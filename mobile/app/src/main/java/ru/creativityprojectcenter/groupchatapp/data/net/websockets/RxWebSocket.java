package ru.creativityprojectcenter.groupchatapp.data.net.websockets;


import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class RxWebSocket {

    private Request request;

    private List<WebSocketConverter.Factory> converterFactories = new ArrayList<>();
    private List<WebSocketInterceptor> receiveInterceptors = new ArrayList<>();

    @Nullable
    private WebSocket originalWebSocket;

    private boolean userRequestedClose = false;

    public interface Event {
        RxWebSocket client();
    }

    public class Open implements Event {
        private final Maybe<Response> response;

        public Open(Response response) {
            this.response = Maybe.just(response);
        }

        public Open() {
            this.response = Maybe.empty();
        }

        @Nullable
        public Response response() {
            return response.blockingGet();
        }

        @Override
        public RxWebSocket client() {
            return RxWebSocket.this;
        }
    }

    public class Message implements Event {
        private final String message;
        private final ByteString messageBytes;

        public Message(String message) {
            this.message = message;
            this.messageBytes = null;
        }

        public Message(ByteString messageBytes) {
            this.messageBytes = messageBytes;
            this.message = null;
        }

        @Nullable
        public String data() {
            String interceptedMessage = message;
            for (WebSocketInterceptor interceptor : receiveInterceptors) {
                interceptedMessage = interceptor.intercept(interceptedMessage);
            }
            return interceptedMessage;
        }

        @Nullable
        public ByteString dataBytes() {
            return messageBytes;
        }

        @NonNull
        private String dataOrDataBytesAsString() {
            if (data() == null && dataBytes() == null) {
                return "";
            }
            if (dataBytes() == null) {
                return data();
            }

            if (data() == null) {
                return dataBytes() == null ? "" : dataBytes().utf8();
            }

            return "";
        }

        public <T> T data(Class<? extends T> type) throws Throwable {
            WebSocketConverter<String, T> converter = responseConverter(type);
            if (converter != null) {
                return converter.convert(dataOrDataBytesAsString());
            } else {
                throw new Exception("No converters available to convert the enqueued object");
            }
        }

        @Override
        public RxWebSocket client() {
            return RxWebSocket.this;
        }
    }

    public class QueuedMessage<T> implements Event {
        private final T message;

        public QueuedMessage(T message) {
            this.message = message;
        }


        @Nullable
        public T message() {
            return message;
        }

        @Override
        public RxWebSocket client() {
            return RxWebSocket.this;
        }
    }


    public class Closed extends Throwable implements Event {
        public static final int INTERNAL_ERROR = 500;
        private final String reason;
        private final int code;

        public Closed(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        public int code() {
            return code;
        }

        public String reason() {
            return reason;
        }

        @Override
        public String getMessage() {
            return reason();
        }

        @Override
        public RxWebSocket client() {
            return RxWebSocket.this;
        }
    }

    private PublishProcessor<Event> eventStream = PublishProcessor.create();

    public boolean isOpened() {
        return originalWebSocket != null;
    }

    public Single<Open> connect() {
        return Single.create(emitter -> {
            emitter.setDisposable(eventStream
                    .ofType(Open.class)
                    .subscribe(emitter::onSuccess, emitter::onError));
            doConnect();
        });
//        return eventStream()
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(d -> doConnect())
//                .ofType(Open.class)
//                .firstOrError();
    }

//    public Completable connect() {
//        return Completable.fromAction(this::doConnect);
//    }

    public Flowable<Message> listen() {
        return eventStream()
                .subscribeOn(Schedulers.io())
                .ofType(Message.class);
    }

    public Single<QueuedMessage> send(byte[] message) {
        return Single.create(emitter -> {
            emitter.setDisposable(eventStream
                    .ofType(QueuedMessage.class)
                    .subscribe(emitter::onSuccess, emitter::onError));
            doQueueMessage(message);
        });
//        return eventStream()
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(d -> doQueueMessage(message))
//                .ofType(QueuedMessage.class)
//                .firstOrError();
    }

//    public <T> Single<QueuedMessage> send(final T message) {
//        return Single.create(emitter -> {
//            emitter.setDisposable(eventStream
//                    .ofType(QueuedMessage.class)
//                    .subscribe(emitter::onSuccess, emitter::onError));
//            doQueueMessage(message);
//        });
////        return eventStream()
////                .subscribeOn(Schedulers.io())
////                .doOnSubscribe(d -> doQueueMessage(message))
////                .ofType(QueuedMessage.class)
////                .firstOrError();
//    }

    public <T> Completable send(final T message) {
        return Completable.fromAction(() -> doQueueMessage(message));
    }

    public Single<Closed> disconnect(int code, String reason) {
        return Single.create(emitter -> {
            emitter.setDisposable(eventStream
                    .ofType(Closed.class)
                    .subscribe(emitter::onSuccess, emitter::onError));
            doDisconnect(code, reason);
        });
//        return eventStream()
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(d -> doDisconnect(code, reason))
//                .ofType(Closed.class)
//                .firstOrError();
    }

    public Flowable<Event> eventStream() {
        return eventStream;
    }

    private void doConnect() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newWebSocket(request, webSocketListener());

        if (originalWebSocket != null) {
            if (eventStream.hasSubscribers()) {
                eventStream.onNext(new Open());
            }
        }
    }

    private void doDisconnect(int code, String reason) {
        requireNotNull(originalWebSocket, "Expected an open websocket");
        userRequestedClose = true;
        if (originalWebSocket != null) {
            originalWebSocket.close(code, reason);
        }
    }

    private void doQueueMessage(byte[] message) {
        requireNotNull(originalWebSocket, "Expected an open websocket");
        requireNotNull(message, "Expected a non null message");
        if (originalWebSocket.send(ByteString.of(message))) {
            if (eventStream.hasSubscribers()) {
                eventStream.onNext(new QueuedMessage(ByteString.of(message)));
            }
        }
    }

    private <T> void doQueueMessage(T message) {
        requireNotNull(originalWebSocket, "Expected an open websocket");
        requireNotNull(message, "Expected a non null message");

        WebSocketConverter<T, String> converter = requestConverter(message.getClass());
        if (converter != null) {
            try {
                if (originalWebSocket.send(converter.convert(message))) {
                    if (eventStream.hasSubscribers()) {
                        eventStream.onNext(new QueuedMessage(message));
                    }
                }
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        } else if (message instanceof String) {
            if (originalWebSocket.send((String) message)) {
                if (eventStream.hasSubscribers()) {
                    eventStream.onNext(new QueuedMessage(message));
                }
            }
        }
    }

    private void setClient(WebSocket originalWebSocket) {
        this.originalWebSocket = originalWebSocket;
        userRequestedClose = false;
    }

    private WebSocketListener webSocketListener() {
        return new WebSocketListener() {

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);

                setClient(webSocket);

                if (eventStream.hasSubscribers()) {
                    eventStream.onNext(new Open(response));
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String message) {
                super.onMessage(webSocket, message);
                if (eventStream.hasSubscribers()) {
                    eventStream.onNext(new Message(message));
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString messageBytes) {
                super.onMessage(webSocket, messageBytes);
                if (eventStream.hasSubscribers()) {
                    eventStream.onNext(new Message(messageBytes));
                }
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                if (userRequestedClose) {
                    if (eventStream.hasSubscribers()) {
                        eventStream.onNext(new Closed(code, reason));
                        eventStream.onComplete();
                    }
                } else {
                    if (eventStream.hasSubscribers()) {
                        eventStream.onError(new Closed(code, reason));
                    }
                }
                setClient(null);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
                if (eventStream.hasSubscribers()) {
                    eventStream.onError(t);
                }
                setClient(null);
            }
        };
    }

    private <T> WebSocketConverter<String, T> responseConverter(final Type type) {
        for (WebSocketConverter.Factory converterFactory : converterFactories) {
            WebSocketConverter<String, ?> converter =
                    converterFactory.responseBodyConverter(type);
            if (converter != null) {
                return (WebSocketConverter<String, T>) converter;
            }
        }
        return null;
    }

    private <T> WebSocketConverter<T, String> requestConverter(final Type type) {
        for (WebSocketConverter.Factory converterFactory : converterFactories) {
            WebSocketConverter<?, String> converter =
                    converterFactory.requestBodyConverter(type);
            if (converter != null) {
                return (WebSocketConverter<T, String>) converter;
            }
        }
        return null;
    }

    private static <T> T requireNotNull(T object, String message) {
        if (object == null) {
            throw new IllegalStateException(message);
        }
        return object;
    }

    /**
     * Builder class for creating rx websockets.
     */
    public static class Builder {
        private List<WebSocketConverter.Factory> converterFactories = new ArrayList<>();
        private List<WebSocketInterceptor> receiveInterceptors = new ArrayList<>();
        private Request request;

        @NonNull
        public Builder request(Request request) {
            this.request = request;
            return this;
        }

        @NonNull
        public Builder addConverterFactory(WebSocketConverter.Factory factory) {
            if (factory != null) {
                converterFactories.add(factory);
            }
            return this;
        }

        @NonNull
        public Builder addReceiveInterceptor(WebSocketInterceptor receiveInterceptor) {
            receiveInterceptors.add(receiveInterceptor);
            return this;
        }

        @NonNull
        public RxWebSocket build() throws IllegalStateException {
            if (request == null) {
                throw new IllegalStateException("Request cannot be null");
            }

            RxWebSocket rxWebSocket = new RxWebSocket();
            rxWebSocket.request = request;
            rxWebSocket.converterFactories = converterFactories;
            rxWebSocket.receiveInterceptors = receiveInterceptors;
            return rxWebSocket;
        }

        @NonNull
        public RxWebSocket build(@NonNull String wssUrl) {
            if (wssUrl == null || wssUrl.isEmpty()) {
                throw new IllegalStateException("Websocket address cannot be null or empty");
            }

            request = new Request.Builder().url(wssUrl).get().build();

            RxWebSocket rxWebSocket = new RxWebSocket();
            rxWebSocket.converterFactories = converterFactories;
            rxWebSocket.receiveInterceptors = receiveInterceptors;
            rxWebSocket.request = request;
            return rxWebSocket;
        }
    }
}