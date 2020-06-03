package ru.creativityprojectcenter.groupchatapp.presentation.addchat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import ru.creativityprojectcenter.groupchatapp.R;

public class AddChatActivity extends AppCompatActivity implements AddChatContract.View {

    private final String TAG = getClass().getSimpleName();

    public static final int REQUEST_ADD_CHAT = 1;

    @Inject
    AddChatContract.Presenter presenter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.chat_title_edit_text) EditText chatTitleEditText;
    @BindView(R.id.description_edit_text) EditText descriptionEditText;
    @BindView(R.id.is_persistent) Switch isPersistentSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.fab_add_chat)
    public void addChat(View view) {
        String title = chatTitleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        boolean isEphemeral = !isPersistentSwitch.isChecked();
        presenter.addChat(title, description, isEphemeral);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showChatsList() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

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
