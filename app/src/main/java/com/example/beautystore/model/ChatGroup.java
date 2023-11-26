package com.example.beautystore.model;

public class ChatGroup {
    private String id;
    private String date;
    private String status;
    private String latestMessage;
    private String inChat;

    public ChatGroup(String id, String date, String status, String latestMessage, String inChat) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.latestMessage = latestMessage;
        this.inChat = inChat;
    }

    public ChatGroup() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public String getInChat() {
        return inChat;
    }

    public void setInChat(String inChat) {
        this.inChat = inChat;
    }
}
