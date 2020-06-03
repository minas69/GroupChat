package ru.creativityprojectcenter.groupchatapp.service;

import ru.creativityprojectcenter.groupchatapp.data.model.Message;

public interface IMessageService {

    void sendMessage(Message newMessage);

}
