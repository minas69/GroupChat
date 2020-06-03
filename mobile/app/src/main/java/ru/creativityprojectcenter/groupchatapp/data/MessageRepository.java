package ru.creativityprojectcenter.groupchatapp.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.creativityprojectcenter.groupchatapp.data.model.Message;
import ru.creativityprojectcenter.groupchatapp.data.net.MessageService;

@Singleton
public class MessageRepository {

    private final String TAG = getClass().getSimpleName();

    private final MessageService messageService;

    @Inject
    MessageRepository(MessageService messageService) {
        this.messageService = messageService;
    }

    public Completable syncMessages(int chatId) {

        return Completable.fromObservable(messageService.getMessages(chatId)
                .map(messages -> {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();

                    realm.insertOrUpdate(messages);

                    realm.commitTransaction();
                    realm.close();

                    return messages;
                }));

    }

    public RealmResults<Message> getMessages(int chatId) {

        RealmResults<Message> items;
        try (final Realm realm = Realm.getDefaultInstance()) {
            items = realm
                    .where(Message.class)
                    .equalTo("chatId", chatId)
                    .sort("date")
                    .findAll();
        }

        return items;

    }

}
