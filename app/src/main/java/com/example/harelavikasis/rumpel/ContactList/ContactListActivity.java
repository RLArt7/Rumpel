package com.example.harelavikasis.rumpel.ContactList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.harelavikasis.rumpel.Chat.RecyclerChatAdapterList;
import com.example.harelavikasis.rumpel.Chat.RiddleChatActivity;
import com.example.harelavikasis.rumpel.Listeners.OnContactClicked;
import com.example.harelavikasis.rumpel.Models.Contact;
import com.example.harelavikasis.rumpel.R;
import com.example.harelavikasis.rumpel.Settings.SettingsActivity;
import com.facebook.FacebookContentProvider;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactListActivity extends AppCompatActivity implements OnContactClicked{

    @Bind(R.id.contact_list)
    RecyclerView contactsList;

    private List<Contact> friends = new ArrayList<>();
    private ContactsAdapterList adapter;

    private FacebookContentProvider facebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");

        fetchContactList(jsondata);
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new ContactsAdapterList(this);
//        llm.setStackFromEnd(true);
//        riddleList.setLayoutManager(llm);
        contactsList.setLayoutManager(new LinearLayoutManager(this));
        contactsList.setHasFixedSize(true);
        contactsList.setAdapter(adapter);
        adapter.setContacts(friends,this);
    }

    private void fetchContactList(String jsondata) {
        JSONArray friendslist;
        try {
            friendslist = new JSONArray(jsondata);
            if (friendslist != null) {
                for (int l = 0; l < friendslist.length(); l++) {
                    friends.add(new Contact(friendslist.getJSONObject(l).getString("name"), friendslist.getJSONObject(l).getString("id")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.settings_btn)
    public void onSettingsClick() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void notifyThatContactClicked(Contact contact) {
        Intent intent = new Intent(this,RiddleChatActivity.class);
        intent.putExtra("name", contact.getName());
        intent.putExtra("endPointId", contact.getId());
        startActivity(intent);
    }
}
