package com.utdisaster.utdmer.models;

public class Conversation {
    private String id;
    private Sms[] messages;

    public Sms[] getMessages() {
        return messages;
    }

    public void setMessages(Sms[] messages) {
        this.messages = messages;
    }


}
