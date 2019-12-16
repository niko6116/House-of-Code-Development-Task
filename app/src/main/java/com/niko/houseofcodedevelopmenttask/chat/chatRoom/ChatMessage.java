package com.niko.houseofcodedevelopmenttask.chat.chatRoom;

import java.util.Date;

/**
 * Represents a message in the chat.
 */
public class ChatMessage extends ChatElement {

    private String messageText;

    public ChatMessage(String user, long time, String text) {
        super.messageUser = user;
        super.messageTime = time;
        this.messageText = text;
    }

    public ChatMessage(String user, String text) {
        super.messageUser = user;
        this.messageText = text;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage() {

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String text) {
        this.messageText = text;
    }

    public String messageToHtml() {
        String message = "<b>" + messageUser
                + "</b><br><i>" + displayTime()
                + "</i><br>" + messageText;

        message = message.replace("\n", "<br>");

        return message;
    }

}
