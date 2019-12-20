package com.sendMessage.message.controller;

import com.sendMessage.message.entity.Message;
import com.sendMessage.message.entity.User;
import com.sendMessage.message.util.JsonUtils;
import com.sendMessage.message.util.getAIAnswer;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;


import com.alibaba.fastjson.JSONArray;


@RestController
public class sendMessage {

    @Value("${server.send.url}")
    private String sendUrl;

    @Value("${chat.login.username}")
    private String username;

    @Value("${chat.login.password}")
    private String password;


    boolean connectStats = false;

    private  Socket socket;

    private String chatId = "";

    private String loginStatus = "";

    @RequestMapping("/send")
    public Map<String, String> sendToChat(@RequestParam("msg") String msg, @RequestParam("url") String url) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("url", url);
        map.put("msg", msg);
        try {

            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket"};
            options.reconnectionAttempts = 5;
            options.reconnectionDelay = 1000;//失败重连的时间间隔
            options.timeout = 500;//连接超时时间(ms)

            JSONObject userInfoObj = new JSONObject();
            userInfoObj.put("username", username);
            userInfoObj.put("password", password);
            userInfoObj.put("os", "Linux Redhat");
            userInfoObj.put("browser", "Chrome");
            userInfoObj.put("environment", "I am a robot");



            //避免创建多个socket连接 第一次执行的时候才进行初始化连接
            if(connectStats == false){
                socket = IO.socket(sendUrl, options);
                System.out.println("获得了socket " + socket);

            }else{

                JSONObject messageInfoObj = new JSONObject();

                messageInfoObj.put("to",chatId);
                messageInfoObj.put("type", "text");
                messageInfoObj.put("content", "【" + msg + "】" + url);

                System.out.println("client: " + "开始发送消息");
                socket.emit("sendMessage", messageInfoObj, new Ack() {
                    @Override
                    public void call(Object... args) {
                        System.out.println("client: " + "发送消息成功" + args[0]);
                    }
                });
            }

            socket.connect();






            //第一次登录并且发送消息
            socket.on(Socket.EVENT_CONNECT, objects -> {
                socket.emit("login", userInfoObj, new Ack() {
                    @Override
                    public void call(Object... args) {
                        System.out.println("client: " + "登录成功" + args[0]);
                        JSONObject messageInfoObj = new JSONObject();
                        try {
                            chatId = getChatGruopId(args[0].toString());

                            messageInfoObj.put("to",chatId);
                            messageInfoObj.put("type", "text");
                            messageInfoObj.put("content", "【" + msg + "】" + url);

                            System.out.println("client: " + "开始发送消息");
                            socket.emit("sendMessage", messageInfoObj, new Ack() {
                                @Override
                                public void call(Object... args) {
                                    System.out.println("client: " + "发送消息成功" + args[0]);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            });


            //调用AI智能聊天机器人，存在缺陷，暂时无法使用

/*            if(connectStats == true){
                while(true){

                Thread.sleep(1000*6);
                System.out.println("client: " +  "开始获取消息列表 ");
                JSONObject HistoryMessages = new JSONObject();
                HistoryMessages.put("existCount",1);
                socket.emit("getDefalutGroupHistoryMessages", HistoryMessages, new Ack() {
                    @Override
                    public void call(Object... args) {
                        System.out.println("client: " + "获取消息成功。。。" + args[0]);
                        getLastMsg(args[0].toString());

                        String AIMessage = getLastMsg(args[0].toString());

                        System.out.println("自动回复为： " + AIMessage);

                        JSONObject AImessageInfoObj = new JSONObject();
                        try {
                            AImessageInfoObj.put("to",chatId);
                            AImessageInfoObj.put("type", "text");
                            AImessageInfoObj.put("content",AIMessage);
                            if("".equals(AIMessage)==false){
                                sendToChat(AImessageInfoObj,socket);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });}
            }*/



            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
                @Override
                public void call(Object... arg0) {
                    System.out.println("client: " +  "连接成功");
                    connectStats = true;
                }

            });
            socket.on(Socket.EVENT_CONNECTING, new Emitter.Listener(){
                @Override
                public void call(Object... arg0) {
                    System.out.println("client: " +  "连接中 " + connectStats);

                }

            });
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener(){
                @Override
                public void call(Object... arg0) {
                    System.out.println("client: " +  "连接超时 " + connectStats);
                    connectStats = false;
                }

            });
            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... arg0) {
                    System.out.println("client: " + "连接失败 " + connectStats);
                    connectStats = false;
                }

            });
            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... arg0) {
                    System.out.println("client: " + "连接断开 " + connectStats);
                    connectStats = false;
                }

            });
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
            return map;
        }
    }
    //获取当前默认聊天室的ID
    private String getChatGruopId(String args) {

        User user = JsonUtils.string2Obj(args, User.class);
        String chatGroupId = user.getGroups().get(0).get_id();
        System.out.println("获取到聊天室ID ： " + chatGroupId);
        return chatGroupId;
    }

    //发送消息给聊天室
    private String sendToChat(JSONObject jsobj,Socket socket){
        socket.emit("sendMessage", jsobj, new Ack() {
            @Override
            public void call(Object... args) {
                System.out.println("client: " + "发送消息成功" + args[0]);
                loginStatus = args[0].toString();
            }
        });
        return loginStatus;
    }
    //登录聊天室
    private String loginTochat(JSONObject jsobj,Socket socket){
        socket.emit("login", jsobj, new Ack() {
            @Override
            public void call(Object... args) {
                System.out.println("client: " + "登录成功" + args[0]);
                chatId = getChatGruopId(args[0].toString());
            }
        });
         return chatId;
    }

    //获取聊天室最后一条消息
    private String getLastMsg(String args) {
        String AIMessage = "";
        List<Message> messageList = (List<Message>)JSONArray.parseArray(args.toString(), Message.class);
        System.out.println("获取到最后一条消息   " + messageList.get(messageList.size()-1).getContent());
        String lastMsg = messageList.get(messageList.size()-1).getContent();
        if(lastMsg.startsWith("@xiaom")){
            System.out.println("开始调用AI自动回复接口   ");
            AIMessage = getAIAnswer.getAnswer(lastMsg.replace("@xiaom",""));

        }
        return AIMessage;
    }
}
