package com.sendMessage.message.entity;

public class AIData {
    private int type;

    private AIInfo info;

    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }
    public void setInfo(AIInfo info){
        this.info = info;
    }
    public AIInfo getInfo(){
        return this.info;
    }
}
