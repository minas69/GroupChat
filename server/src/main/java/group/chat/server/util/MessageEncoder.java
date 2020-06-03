package group.chat.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.*;
import group.chat.server.models.DummyMessage;
import group.chat.server.models.Message;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.lang.reflect.Type;

public class MessageEncoder implements Encoder.Text<Message> {

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(final Message message) throws EncodeException {
        Gson gson = new Gson();
        return gson.toJson(message);
    }

}
