package com.example.backend.model;

public class ChatResponse {

    private String chatResponse;
    private String conversationId;

    public String getChatResponse() {
        return chatResponse;
    }
    public void setChatResponse(String chatResponse) {
        this.chatResponse = chatResponse;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
