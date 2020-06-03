package ru.creativityprojectcenter.groupchatapp.data;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import okhttp3.ResponseBody;
import ru.creativityprojectcenter.groupchatapp.data.net.model.ErrorBody;
import ru.creativityprojectcenter.groupchatapp.data.net.model.TokenResponse;
import ru.creativityprojectcenter.groupchatapp.data.net.AccountService;
import ru.creativityprojectcenter.groupchatapp.util.Preferences;
import rx.functions.Func1;

@Singleton
public class AccountRepository {

    private final String TAG = getClass().getSimpleName();

    private final AccountService accountService;
    private final Preferences pref;

    private ConnectableObservable<TokenResponse> loginRequest;

    @Inject
    public AccountRepository(AccountService accountService,
                             Preferences preferences) {
        this.accountService = accountService;
        pref = preferences;
    }

    public Observable<TokenResponse> login(String nickname,
                                           String password,
                                           final boolean rememberSession) {
        return accountService
                .login(nickname, password)
                .doOnNext(tokenResponse -> {
                    pref.setUserId(tokenResponse.getUserId());
                    pref.setToken(tokenResponse.getToken());
                    pref.setIsSessionRemembered(rememberSession);
                });
    }

    public Observable<TokenResponse> signUp(String nickname,
                                            String email,
                                            String password,
                                            final boolean rememberSession) {
        return accountService
                .signUp(nickname, email, password)
                .doOnNext(tokenResponse -> {
                    pref.setUserId(tokenResponse.getUserId());
                    pref.setToken(tokenResponse.getToken());
                    pref.setIsSessionRemembered(rememberSession);
                });
    }

    public <T> ObservableTransformer<T, T> parseHttpErrors() {
        return upstream -> upstream.onErrorResumeNext((Function<? super Throwable, ? extends ObservableSource<? extends T>>) throwable -> {
            if (throwable instanceof HttpException) {
                ErrorBody errorBody = getErrorBody(
                        ((HttpException) throwable).response().errorBody());
                return Observable.error(errorBody);
            }
            return Observable.error(throwable);
        });
    }

    private ErrorBody getErrorBody(ResponseBody responseBody) {
        try {
            return new Gson().fromJson(responseBody.string(), ErrorBody.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorBody(400, "API error");
        }
    }

}
