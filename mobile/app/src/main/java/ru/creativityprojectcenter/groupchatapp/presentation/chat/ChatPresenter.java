package ru.creativityprojectcenter.groupchatapp.presentation.chat;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;
import ru.creativityprojectcenter.groupchatapp.data.ChatRepository;
import ru.creativityprojectcenter.groupchatapp.data.MessageRepository;
import ru.creativityprojectcenter.groupchatapp.data.model.Chat;
import ru.creativityprojectcenter.groupchatapp.data.model.Message;
import ru.creativityprojectcenter.groupchatapp.presentation.base.BasePresenter;
import ru.creativityprojectcenter.groupchatapp.service.IMessageService;
import ru.creativityprojectcenter.groupchatapp.util.SchedulersFacade;

public class ChatPresenter
        extends BasePresenter<ChatContract.View>
        implements ChatContract.Presenter {

    private final String TAG = getClass().getSimpleName();

    private final SchedulersFacade schedulersFacade;

    private final BehaviorRelay<RequestState> requestStateObserver
            = BehaviorRelay.createDefault(RequestState.IDLE);

    private Chat openedChat;
    private ChatRepository chatRepository;
    private MessageRepository messageRepository;
    private IMessageService messageService;

    @Inject
    ChatPresenter(ChatContract.View view,
                  ChatRepository chatRepository,
                  MessageRepository messageRepository,
                  SchedulersFacade schedulersFacade) {
        super(view);
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.schedulersFacade = schedulersFacade;

        observeRequestState();
    }

    @Override
    public void start() {
        view.showChatTitle(openedChat.getTitle());
        view.showChatMembersCount(openedChat.getMembersCount());
        isJoined();
        getMessages();
        syncMessages();
    }

    private void isJoined() {
        if (openedChat == null) {
            return;
        }

        view.setMessageInput(openedChat.isJoined());

        addDisposable(chatRepository
                .isJoined(openedChat.getId())
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(
                        view::setMessageInput,
                        throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void setChat(int chatId) {
        openedChat = chatRepository.getChat(chatId);
        openedChat.addChangeListener((RealmChangeListener<Chat>) chat -> {
            view.showChatTitle(chat.getTitle());
            view.showChatMembersCount(chat.getMembersCount());
        });
    }

    @Override
    public void getMessages() {
        if (openedChat != null) {
            RealmResults<Message> messages = messageRepository.getMessages(openedChat.getId());
            view.showMessages(messages);
        }
    }

    @Override
    public void syncMessages() {
        if (openedChat == null || openedChat.isEphemeral()) {
            return;
        }

        view.setLoadingIndicator(true);

        addDisposable(messageRepository
                .syncMessages(openedChat.getId())
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe(s -> publishRequestState(RequestState.LOADING))
                .doOnComplete(() -> publishRequestState(RequestState.COMPLETE))
                .doOnError(t -> publishRequestState(RequestState.ERROR))
                .subscribe(() -> {}, throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void sendMessage(String text) {
        if (!TextUtils.isEmpty(text) && messageService != null) {
            Message message = new Message();
            message.setMessageType(Message.TEXT);
            message.setBody(text);
            message.setChatId(openedChat.getId());

            messageService.sendMessage(message);
        }
    }

    @Override
    public void serviceConnected(IMessageService service) {
        messageService = service;
    }

    @Override
    public void serviceDisconnected() {
        messageService = null;
    }

    @Override
    public void joinChat() {
        if (openedChat == null) {
            return;
        }

        addDisposable(chatRepository
                .joinChat(openedChat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> view.setMessageInput(true),
                        throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void leaveChat() {

    }

    private void publishRequestState(RequestState requestState) {
        addDisposable(Observable.just(requestState)
                .observeOn(schedulersFacade.ui())
                .subscribe(requestStateObserver));
    }

    private void observeRequestState() {
        Disposable d = requestStateObserver.subscribe(requestState -> {
            switch (requestState) {
                case IDLE:
                    break;
                case LOADING:
                    view.setLoadingIndicator(true);
                    break;
                case COMPLETE:
                    view.setLoadingIndicator(false);
                    break;
                case ERROR:
                    view.setLoadingIndicator(false);
                    break;
            }
        }, throwable -> Log.d(TAG, throwable.getMessage()));
    }

}
