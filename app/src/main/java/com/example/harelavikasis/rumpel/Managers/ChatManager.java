package com.example.harelavikasis.rumpel.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.harelavikasis.rumpel.Chat.RiddleChatActivity;
import com.example.harelavikasis.rumpel.Models.Chat;
import com.google.gson.Gson;

import es.dmoral.prefs.Prefs;

/**
 * Created by harelavikasis on 15/03/2017.
 */

public class ChatManager {

    private Context context;

    public static final String PREFS_NAME = "Rumpel_Chats";

    public ChatManager(Context context) {
        this.context = context;
    }


    public Chat fetchConversationByEndPoint(Chat c) {
        // here we need to reterive conversation if its exist or create new one
        Gson gson = new Gson();
        String key = c.getEndPoint();

        String json =  Prefs.with(context).read(key);//settings.getString(key, "");

        if (json.isEmpty()) {
            createNewChatRecord(c);
            return c;
        } else {
            Chat jsonChat = gson.fromJson(json, Chat.class);

            return jsonChat;
        }
    }

    public void createNewChatRecord(Chat c) {

        Gson gson = new Gson();
        String key = c.getEndPoint();
        String json = gson.toJson(c,Chat.class);
        Log.d("GSON",json);
        Prefs.with(context).write(key,json);
    }

    public void updateChatRecord(Chat c) {
        Gson gson = new Gson();
        String key = c.getEndPoint();
        String json = gson.toJson(c);
        Prefs.with(context).write(key,json);

    }
}
