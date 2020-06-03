package ru.creativityprojectcenter.groupchatapp.presentation.login;

import android.util.Log;

import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import ru.creativityprojectcenter.groupchatapp.data.AccountRepository;
import ru.creativityprojectcenter.groupchatapp.data.net.model.ErrorBody;
import ru.creativityprojectcenter.groupchatapp.data.net.model.TokenResponse;
import ru.creativityprojectcenter.groupchatapp.injection.scope.ActivityScoped;
import ru.creativityprojectcenter.groupchatapp.presentation.base.BasePresenter;
import ru.creativityprojectcenter.groupchatapp.util.Constants;
import ru.creativityprojectcenter.groupchatapp.util.SchedulersFacade;

@ActivityScoped
public class LoginPresenter
        extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private final String TAG = getClass().getSimpleName();

    private final SchedulersFacade schedulersFacade;

    private final BehaviorRelay<RequestState> requestStateObserver
            = BehaviorRelay.createDefault(RequestState.IDLE);

    private AccountRepository accountRepository;

    @Inject
    LoginPresenter(LoginContract.View view,
                   AccountRepository accountRepository,
                   SchedulersFacade schedulersFacade) {
        super(view);
        this.accountRepository = accountRepository;
        this.schedulersFacade = schedulersFacade;

        observeRequestState();
    }

    @Override
    public void attemptLogin(String nickname, String password, boolean rememberPassword) {
        addDisposable(accountRepository
                .login(nickname, password, rememberPassword)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe(s -> publishRequestState(RequestState.LOADING))
                .doOnComplete(() -> publishRequestState(RequestState.COMPLETE))
                .doOnError(t -> publishRequestState(RequestState.ERROR))
                .compose(accountRepository.parseHttpErrors())
                .subscribe(tokenResponse -> view.navigateToChats(),
                        throwable -> {
                            if (throwable instanceof ErrorBody) {
                                int code = ((ErrorBody) throwable).getCode();
                                String message = ((ErrorBody) throwable).getMessage();
                                switch (code) {
                                    case Constants.Code.INCORRECT_NICKNAME:
                                        view.nicknameIncorrect();
                                        break;
                                    case Constants.Code.INCORRECT_PASSWORD:
                                        view.passwordIncorrect();
                                        break;
                                        default:
                                            view.showError(message);
                                            break;

                                }
                            } else {
                                view.showError(throwable.getMessage());
                            }
                        }));
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
