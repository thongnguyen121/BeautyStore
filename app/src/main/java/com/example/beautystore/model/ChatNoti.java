package com.example.beautystore.model;

public class ChatNoti {
    private String consultant_id, message, seen, date, chatId;

    public ChatNoti() {
    }

    public ChatNoti(String consultant_id, String message, String seen, String date, String chatId) {
        this.consultant_id = consultant_id;
        this.message = message;
        this.seen = seen;
        this.date = date;
        this.chatId = chatId;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getConsultant_id() {
        return consultant_id;
    }

    public void setConsultant_id(String consultant_id) {
        this.consultant_id = consultant_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
