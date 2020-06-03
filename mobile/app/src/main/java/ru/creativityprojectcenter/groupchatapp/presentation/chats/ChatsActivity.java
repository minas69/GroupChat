package ru.creativityprojectcenter.groupchatapp.presentation.chats;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import io.realm.Realm;
import io.realm.RealmResults;
import ru.creativityprojectcenter.groupchatapp.R;
import ru.creativityprojectcenter.groupchatapp.data.model.Chat;
import ru.creativityprojectcenter.groupchatapp.presentation.addchat.AddChatActivity;
import ru.creativityprojectcenter.groupchatapp.presentation.chat.ChatActivity;
import ru.creativityprojectcenter.groupchatapp.presentation.login.LoginActivity;
import ru.creativityprojectcenter.groupchatapp.service.MessageService;
import ru.creativityprojectcenter.groupchatapp.util.Preferences;

public class ChatsActivity extends AppCompatActivity implements ChatsContract.View {

    private final String TAG = getClass().getSimpleName();

    @Inject
    ChatsContract.Presenter presenter;

    @Inject
    public Preferences pref;

    private Realm realm;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.chats_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    private ChatsAdapter chatsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.chats_list_title);

        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        setupDrawerContent(navigationView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatsAdapter = new ChatsAdapter(this, null);
        chatsAdapter.setOnClickListener(chat -> {
            Intent i = new Intent(this, ChatActivity.class);
            i.putExtra("chatId", chat.getId());
            startActivity(i);
        });
        recyclerView.setAdapter(chatsAdapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.syncChats());

        Intent i = new Intent(this, MessageService.class);
        startService(i);
    }

    @OnClick(R.id.fab_add_chat)
    public void addNewChat(View view) {
        presenter.addNewChat();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.chats_navigation_menu_item:
                            // Мы уже на этом экране
                            break;
                        case R.id.log_out_navigation_menu_item:
                            pref.setUserId(-1);
                            pref.setToken(null);
                            pref.setIsSessionRemembered(false);

                            Intent serviceIntent = new Intent(this, MessageService.class);
                            stopService(serviceIntent);

                            Intent i = new Intent(this, LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            break;
                        default:
                            break;
                    }

                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;
                });
    }

    @Override
    public void showChats(RealmResults<Chat> chats) {
        chatsAdapter.updateData(chats);
    }

    @Override
    public void showError(String message) {
        Log.d(TAG, "Error: " + message);
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(active));
    }

    @Override
    public void showAddNewChat() {
        Intent i = new Intent(this, AddChatActivity.class);
        startActivityForResult(i, AddChatActivity.REQUEST_ADD_CHAT);
    }

    @Override
    public void showSuccessfullyAddedChat() {
        Snackbar.make(swipeRefreshLayout,
                "Chat has been successfully added",
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stop();
    }

}
