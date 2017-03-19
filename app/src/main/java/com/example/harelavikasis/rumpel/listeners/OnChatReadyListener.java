package com.example.harelavikasis.rumpel.listeners;

import com.example.harelavikasis.rumpel.Models.Chat;

/**
 * Created by Dudu_Cohen on 01/11/2016.
 *
 * Post Ready Listener
 */

public interface OnChatReadyListener {
    void onAddPost(Chat chat);
    void onUpdatedPost(Chat chat, int index);
    void onRemovedPost(int index);
}