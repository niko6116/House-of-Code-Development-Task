package com.niko.houseofcodedevelopmenttask.chat.chatRoom;

import android.text.format.DateFormat;

/**
 * Represents and element in the chat.
 */
public abstract class ChatElement {

    protected String messageUser;
    protected long messageTime;

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String user) {
        this.messageUser = user;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long time) {
        this.messageTime = time;
    }

    public String displayTime() {
        return (String) DateFormat.format("dd-MM-yyyy (HH:mm:ss)", messageTime);
    }

}
