package group.chat.server.util;

import group.chat.server.models.Message;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class MessageDecoder implements Decoder.Text<Message> {

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public Message decode(final String arg0) throws DecodeException {
        try {
            return Constants.MAPPER.readValue(arg0, Message.class);
        } catch (IOException e) {
            throw new DecodeException(arg0, "Unable to decode text to Message", e);
        }
    }

    @Override
    public boolean willDecode(final String arg0) {
        return arg0.contains(Constants.CHAT_ID_KEY) &&
                arg0.contains(Constants.BODY_KEY);
    }

}
