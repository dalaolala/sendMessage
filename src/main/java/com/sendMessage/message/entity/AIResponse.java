package com.sendMessage.message.entity;

public class AIResponse {
    private String message;

    private AIData data;

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setData(AIData data){
        this.data = data;
    }
    public AIData getData(){
        return this.data;
    }
}
