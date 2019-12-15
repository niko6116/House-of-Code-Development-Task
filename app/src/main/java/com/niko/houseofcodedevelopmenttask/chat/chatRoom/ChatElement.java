package com.niko.houseofcodedevelopmenttask.chat.chatRoom;

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

}
