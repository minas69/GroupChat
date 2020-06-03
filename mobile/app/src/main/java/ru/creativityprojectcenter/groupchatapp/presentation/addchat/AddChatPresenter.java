package ru.creativityprojectcenter.groupchatapp.presentation.addchat;

import android.util.Log;

import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.creativityprojectcenter.groupchatapp.data.ChatRepository;
import ru.creativityprojectcenter.groupchatapp.presentation.base.BasePresenter;
import ru.creativityprojectcenter.groupchatapp.util.SchedulersFacade;

public class AddChatPresenter
        extends BasePresenter<AddChatContract.View>
        implements AddChatContract.Presenter {

    private final String TAG = getClass().getSimpleName();

    private final SchedulersFacade schedulersFacade;

    private final BehaviorRelay<RequestState> requestStateObserver
            = BehaviorRelay.createDefault(RequestState.IDLE);

    private final ChatRepository chatRepository;

    @Inject
    AddChatPresenter(AddChatContract.View view,
                     ChatRepository chatRepository,
                     SchedulersFacade schedulersFacade) {
        super(view);
        this.chatRepository = chatRepository;
        this.schedulersFacade = schedulersFacade;

        observeRequestState();
    }

    @Override
    public void addChat(String title, String description, boolean isEphemeral) {
        addDisposable(chatRepository
                .addChat(title, description, isEphemeral)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe(s -> publishRequestState(RequestState.LOADING))
                .doOnComplete(() -> publishRequestState(RequestState.COMPLETE))
                .doOnError(t -> publishRequestState(RequestState.ERROR))
                .subscribe(view::showChatsList,
                        throwable -> view.showError(throwable.getMessage())));
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
