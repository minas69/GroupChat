package ru.creativityprojectcenter.groupchatapp.presentation.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View {

    private final String TAG = getClass().getSimpleName();

    @Inject
    SignUpContract.Presenter presenter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.login) EditText loginView;
    @BindView(R.id.password) EditText passwordView;
    @BindView(R.id.email) EditText emailView;
    @BindView(R.id.remember_pass) CheckBox checkBox;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setTitle("Регистрация");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing up...");
        progressDialog.setOnCancelListener(dialog -> presenter.stop());
    }

    @OnClick(R.id.sign_up_button)
    public void signUp(View view) {
        String nickname = loginView.getText().toString();
        String password = passwordView.getText().toString();
        String email = emailView.getText().toString();
        boolean rememberPassword = checkBox.isChecked();

        presenter.attemptSignUp(nickname, password, email, rememberPassword);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void navigateToChats() {
        Intent i = new Intent(this, ChatsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        Toast.makeText(this, "Неправильное имя пользователя", Toast.LENGTH_LONG).show();
    }

    @Override
    public void emailIncorrect() {
        Toast.makeText(this, "Неверный e-mail", Toast.LENGTH_LONG).show();
    }

    @Override
    public void passwordIncorrect() {
        Toast.makeText(this, "Неверный пароль", Toast.LENGTH_LONG).show();
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
