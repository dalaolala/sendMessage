package com.sendMessage.message.entity;

import java.util.List;

public class AIInfo
{
    private String text;

    private List<String> heuristic;

    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return this.text;
    }

    public void setHeuristic(List<String> heuristic){
        this.heuristic = heuristic;
    }
    public List<String> getHeuristic(){
        return this.heuristic;
    }
}
