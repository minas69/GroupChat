package group.chat.server.models;

public class Message {

    public static final String TEXT = "TEXT";
    public static final String ADMIN = "ADMIN";

    public static final String USER_JOINED = "JOINED";
    public static final String USER_LEFT = "LEFT";

    private int id;

    private String messageType;

    private String customType;

    private long date;

    private int chatId;

    private Member author;

    private String body;

    public Message() {}

    public Message(int id, long date, int chatId, Member author,
                   String body, String messageType, String customType) {
        this.id = id;
        this.date = date;
        this.chatId = chatId;
        this.author = author;
        this.body = body;
        this.messageType = messageType;
        this.customType = customType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Member getAuthor() {
        return author;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }
}
