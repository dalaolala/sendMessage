package com.sendMessage.message.util;


import com.sendMessage.message.entity.AIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class getAIAnswer {
    //注入
    @Autowired
    private static RestTemplate restTemplate =  new RestTemplate();

    public static  String   getAnswer(String inputMsg ){
        String apiURL = "https://api.ownthink.com/bot";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);



        Map<String, Object> requestParam = new HashMap<>();

        requestParam.put("spoken", inputMsg);
        requestParam.put("appid", "e1ce7450460a0c8a4df80dee0830c07f");
        requestParam.put("userid", "9vP0OzMv");
        HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestParam, headers);


        ResponseEntity<String> entity = restTemplate.postForEntity(apiURL, request, String.class);


        String body = entity.getBody();

        AIResponse aire = JsonUtils.string2Obj(body, AIResponse.class);
        String resultTXT = aire.getData().getInfo().getText();
        System.out.println("回复内容TXT " + resultTXT);
        return  resultTXT;

    }

    public static void main(String[] args) {

    }
}
