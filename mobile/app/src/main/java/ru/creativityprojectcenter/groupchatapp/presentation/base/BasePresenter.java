package ru.creativityprojectcenter.groupchatapp.presentation.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V> implements IBasePresenter {

    public enum RequestState {
        IDLE,
        LOADING,
        COMPLETE,
        ERROR
    }

    protected final V view;

    private CompositeDisposable disposables = new CompositeDisposable();

    protected BasePresenter(V view) {
        this.view = view;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        disposables.clear();
    }

    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }
}