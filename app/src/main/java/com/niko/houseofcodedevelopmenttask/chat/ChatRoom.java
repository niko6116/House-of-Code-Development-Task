package com.niko.houseofcodedevelopmenttask.chat;

import android.content.Context;
import android.view.View;

import com.google.firebase.database.DataSnapshot;

public class ChatRoom {

    private String key;
    private String name;
    private String description;
    private DataSnapshot messages;

    public ChatRoom(String key, String name, String description, DataSnapshot messages) {
        this.key = key;
        this.name = name;
        this.description = description;
        this.messages = messages;
    }

    public ChatRoom() {

    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMessages(DataSnapshot messages) {
        this.messages = messages;
    }

    /**
     * @return Returns a String representing the room in HTML.
     */
    public String roomToHtml() {
        String string = "<b>" + name + "</b>"
                + "<br>" + description;

        string = string.replace("\n", "<br>");

        return string;
    }

    /**
     * @param activity
     * @return a listener that redirects the user to this objects chat room;
     */
    public View.OnClickListener getOnClickListener(final ChatActivity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.openChatRoomActivity(ChatRoom.this.key);
            }
        };
    }

}
