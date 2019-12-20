package com.sendMessage.message.entity;

public class Message {

    private String createTime;

    private From from;

    private String _id;

    private String type;

    private String content;

    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    public String getCreateTime(){
        return this.createTime;
    }
    public void setFrom(From from){
        this.from = from;
    }
    public From getFrom(){
        return this.from;
    }
    public void set_id(String _id){
        this._id = _id;
    }
    public String get_id(){
        return this._id;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
}
