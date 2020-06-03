package ru.creativityprojectcenter.groupchatapp.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.creativityprojectcenter.groupchatapp.data.model.Chat;
import ru.creativityprojectcenter.groupchatapp.data.model.Message;
import ru.creativityprojectcenter.groupchatapp.data.net.ChatService;

@Singleton
public class ChatRepository {

    private final String TAG = getClass().getSimpleName();

    private final ChatService chatService;

    @Inject
    ChatRepository(ChatService chatService) {
        this.chatService = chatService;
    }

    public Completable syncChats() {

        return Completable.fromObservable(chatService.getChats()
                .map(chats -> {
                    final Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();

                        for (int i = 0; i < chats.size(); i++) {
                            Chat chat = chats.get(i);
                            Chat realmChat = realm
                                    .where(Chat.class)
                                    .equalTo("id", chat.getId())
                                    .findFirst();
                            if (realmChat != null) {
                                chat.setJoined(realmChat.isJoined());
                                if (chat.isEphemeral() && realmChat.getLastMessage() != null) {
                                    Message lastMessage = realm
                                            .copyFromRealm(realmChat.getLastMessage());
                                    chat.setLastMessage(lastMessage);
                                }
                            }
                            realm.insertOrUpdate(chat);
                        }

                        realm.commitTransaction();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    } finally {
                        if (realm.isInTransaction()) {
                            realm.cancelTransaction();
                        }
                        realm.close();
                    }

                    return chats;
                }));

    }

    public RealmResults<Chat> getChats() {

        RealmResults<Chat> items;
        try (final Realm realm = Realm.getDefaultInstance()) {
            items = realm
                    .where(Chat.class)
                    .findAll();
        }

        return items;

    }

    public Chat getChat(int chatId) {

        Realm realm = Realm.getDefaultInstance();
        Chat item = realm
                .where(Chat.class)
                .equalTo("id", chatId)
                .findFirst();
        realm.close();

        return item;

    }

    public Completable addChat(String title, String description, boolean isEphemeral) {

        return Completable.fromObservable(chatService.addChat(title, description, isEphemeral)
                .map(chat -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(chat);
                    realm.commitTransaction();
                    realm.close();

                    return chat;
                }));

    }

    public Completable joinChat(Chat chat) {

        return chatService.joinChat(chat.getId());

    }

    public Observable<Boolean> isJoined(int chatId) {

        return chatService.isJoined(chatId)
                .map(isMemberResponse -> {
                    boolean isMember = isMemberResponse.isMember();

                    final Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();

                        Chat realmChat = realm
                                .where(Chat.class)
                                .equalTo("id", chatId)
                                .findFirst();
                        if (realmChat != null) {
                            realmChat.setJoined(isMember);
                        }

                        realm.commitTransaction();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    } finally {
                        if (realm.isInTransaction()) {
                            realm.cancelTransaction();
                        }
                        realm.close();
                    }

                    return isMember;
                });

    }

}
