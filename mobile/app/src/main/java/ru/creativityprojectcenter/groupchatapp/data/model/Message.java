package ru.creativityprojectcenter.groupchatapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import ru.creativityprojectcenter.groupchatapp.data.net.DateTypeAdapter;

public class Message extends RealmObject {

    public static final String TEXT = "TEXT";
    public static final String ADMIN = "ADMIN";

    public static final String USER_JOINED = "JOINED";
    public static final String USER_LEFT = "LEFT";

    @PrimaryKey
    @Expose(serialize = false)
    private int id;

    @Expose
    private String messageType;

    @Expose
    private String customType;

    @Index
    @Expose
    private int chatId;

    @JsonAdapter(DateTypeAdapter.class)
    @Expose(serialize = false)
    private Date date;

    @SerializedName("author")
    @Expose(serialize = false)
    private User sender;

    @Expose
    private String body;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String text) {
        this.body = text;
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
