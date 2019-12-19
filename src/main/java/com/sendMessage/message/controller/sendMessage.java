package com.sendMessage.message.controller;

import com.sendMessage.message.entity.User;
import com.sendMessage.message.util.JsonUtils;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

@RestController
public class sendMessage {

    @Value("${server.send.url}")
    private String sendUrl;

    @Value("${chat.login.username}")
    private String username;

    @Value("${chat.login.password}")
    private String password;

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
            //par1 是任意参数
            final Socket socket = IO.socket(sendUrl, options);

            socket.connect();


            JSONObject userInfoObj = new JSONObject();
            userInfoObj.put("username", username);
            userInfoObj.put("password", password);
            userInfoObj.put("os", "Linux Redhat");
            userInfoObj.put("browser", "Chrome");
            userInfoObj.put("environment", "I am a robot");


            socket.on(Socket.EVENT_CONNECT, objects -> {
                System.out.println("client: " + "登录成功");
                socket.emit("login", userInfoObj, new Ack() {
                    @Override
                    public void call(Object... args) {
                        System.out.println("client: " + "登录成功" + args[0]);
                        JSONObject messageInfoObj = new JSONObject();
                        try {
                            String chatId = getChatGruopId(args[0].toString());

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


            socket.on(Socket.EVENT_CONNECTING, objects -> System.out.println("client: " + "连接中"));
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, objects -> System.out.println("client: " + "连接超时"));
            socket.on(Socket.EVENT_CONNECT_ERROR, objects -> System.out.println("client: " + "连接失败"));

            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
            return map;
        }
    }

    private String getChatGruopId(String args) {

        User user = JsonUtils.string2Obj(args, User.class);
        String chatGroupId = user.getGroups().get(0).get_id();
        System.out.println("获取到聊天室ID ： " + chatGroupId);
        return chatGroupId;
    }
}
