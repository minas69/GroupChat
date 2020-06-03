package ru.creativityprojectcenter.groupchatapp.data.net.websockets;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;

public final class WebSocketConverterFactory extends WebSocketConverter.Factory {
    public static WebSocketConverterFactory create() {
        return create(new Gson());
    }

    public static WebSocketConverterFactory create(Gson gson) {
        return new WebSocketConverterFactory(gson);
    }

    private final Gson gson;

    private WebSocketConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson is null");
        this.gson = gson;
    }

    @Override
    public WebSocketConverter<String, ?> responseBodyConverter(Type type) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonResponseConverter(gson, adapter);
    }

    @Override
    public WebSocketConverter<?, String> requestBodyConverter(Type type) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type)).nullSafe();
        return new GsonRequestConverter<>(gson, adapter);
    }

    private class GsonResponseConverter<T> implements WebSocketConverter<String, T> {

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GsonResponseConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override public T convert(String value) throws IOException {
            JsonReader jsonReader = gson.newJsonReader(new StringReader(value));
            try {
                return adapter.read(jsonReader);
            } finally {
                jsonReader.close();
            }
        }

    }

    private class GsonRequestConverter<T> implements WebSocketConverter<T, String> {

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GsonRequestConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public String convert(T value) throws IOException {
            return gson.toJson(value);
        }

    }
}