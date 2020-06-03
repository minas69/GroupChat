package group.chat.server.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class DummyMessage {

    @JsonProperty("username")
    private final String userName;
    @JsonProperty
    private final String message;

    @JsonCreator
    public DummyMessage(@JsonProperty("username") final String userName,
                        @JsonProperty("message") final String message) {
        Objects.requireNonNull(userName);
        Objects.requireNonNull(message);

        this.userName = userName;
        this.message = message;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getMessage() {
        return this.message;
    }
}