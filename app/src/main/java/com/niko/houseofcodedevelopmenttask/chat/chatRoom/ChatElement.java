package com.niko.houseofcodedevelopmenttask.chat.chatRoom;

import android.text.format.DateFormat;

/**
 * Represents and element in the chat.
 */
public abstract class ChatElement {

    protected String messageUserID;
    protected String messageUser;
    protected long messageTime;

    public String getMessageUserID() {
        return messageUserID;
    }

    public void setMessageUserID(String userID) {
        this.messageUserID = userID;
    }

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

    public void passMessageTimeString(String time) {
        this.messageTime = Long.parseLong(time);
    }

    public String displayTime() {
        return (String) DateFormat.format("HH:mm:ss (dd-MM-yyyy)", messageTime);
    }

}
