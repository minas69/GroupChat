package ru.creativityprojectcenter.groupchatapp.presentation.chats;

import android.app.Activity;
import android.util.Log;

import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.realm.RealmResults;
import ru.creativityprojectcenter.groupchatapp.data.ChatRepository;
import ru.creativityprojectcenter.groupchatapp.data.model.Chat;
import ru.creativityprojectcenter.groupchatapp.presentation.addchat.AddChatActivity;
import ru.creativityprojectcenter.groupchatapp.presentation.base.BasePresenter;
import ru.creativityprojectcenter.groupchatapp.util.SchedulersFacade;

public class ChatsPresenter
        extends BasePresenter<ChatsContract.View>
        implements ChatsContract.Presenter{

    private final String TAG = getClass().getSimpleName();

    private final SchedulersFacade schedulersFacade;

    private final BehaviorRelay<RequestState> requestStateObserver
            = BehaviorRelay.createDefault(RequestState.IDLE);

    private final ChatRepository chatRepository;

    @Inject
    ChatsPresenter(ChatsContract.View view,
                   ChatRepository chatRepository,
                   SchedulersFacade schedulersFacade) {
        super(view);
        this.chatRepository = chatRepository;
        this.schedulersFacade = schedulersFacade;

        observeRequestState();
    }

    @Override
    public void start() {
        getChats();
        syncChats();
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (AddChatActivity.REQUEST_ADD_CHAT == requestCode && Activity.RESULT_OK == resultCode) {
            view.showSuccessfullyAddedChat();
        }
    }

    @Override
    public void getChats() {
        RealmResults<Chat> chats = chatRepository.getChats();
        view.showChats(chats);
    }

    @Override
    public void syncChats() {

        addDisposable(chatRepository
                .syncChats()
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe(s -> publishRequestState(RequestState.LOADING))
                .doOnComplete(() -> publishRequestState(RequestState.COMPLETE))
                .doOnError(t -> publishRequestState(RequestState.ERROR))
                .subscribe(() -> {}, throwable -> view.showError(throwable.getMessage())));
    }

    @Override
    public void addNewChat() {
        view.showAddNewChat();
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
