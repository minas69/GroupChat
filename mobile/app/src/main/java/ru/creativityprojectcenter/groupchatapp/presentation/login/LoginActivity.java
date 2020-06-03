package ru.creativityprojectcenter.groupchatapp.presentation.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import ru.creativityprojectcenter.groupchatapp.R;
import ru.creativityprojectcenter.groupchatapp.presentation.chats.ChatsActivity;
import ru.creativityprojectcenter.groupchatapp.presentation.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private final String TAG = getClass().getSimpleName();

    @Inject
    LoginContract.Presenter presenter;

    @BindView(R.id.login) EditText loginView;
    @BindView(R.id.password) EditText passwordView;
    @BindView(R.id.remember_pass) CheckBox checkBox;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logging in...");
        progressDialog.setOnCancelListener(dialog -> presenter.stop());
    }

    @OnClick(R.id.into_sign_up_page)
    public void toSignUpPage(View view) {
        navigateToSignUpPage();
    }

    @OnClick(R.id.log_in_button)
    public void logIn(View view) {
        String nickname = loginView.getText().toString();
        String password = passwordView.getText().toString();
        boolean rememberPassword = checkBox.isChecked();

        presenter.attemptLogin(nickname, password, rememberPassword);
    }

    @Override
    public void navigateToChats() {
        Intent i = new Intent(this, ChatsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void navigateToSignUpPage() {
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showError(String message) {
        Log.d(TAG, "Error: " + message);
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void nicknameIncorrect() {
        loginView.setError("Неправильное имя пользователя");
    }

    @Override
    public void passwordIncorrect() {
        passwordView.setError("Неверный пароль");
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }

}
