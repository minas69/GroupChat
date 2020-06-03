package ru.creativityprojectcenter.groupchatapp.service;

import com.google.gson.annotations.JsonAdapter;

import java.util.Date;

import ru.creativityprojectcenter.groupchatapp.data.model.User;
import ru.creativityprojectcenter.groupchatapp.data.net.DateTypeAdapter;

public class MessageUpdate {

    private int localMessageId;

    private int serverMessageId;

    private User sender;

    @JsonAdapter(DateTypeAdapter.class)
    private Date date;

    public int getLocalMessageId() {
        return localMessageId;
    }

    public void setLocalMessageId(int localMessageId) {
        this.localMessageId = localMessageId;
    }

    public int getServerMessageId() {
        return serverMessageId;
    }

    public void setServerMessageId(int serverMessageId) {
        this.serverMessageId = serverMessageId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
