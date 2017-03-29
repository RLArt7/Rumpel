package com.example.harelavikasis.rumpel.Models;

import com.example.harelavikasis.rumpel.Managers.UserManger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by harelavikasis on 26/03/2017.
 */

public class User {

    private String userId;
    private String userName;
    private String facebookId;
    private String userToken;

    //    private String facebookToken;
    private Map<String,String> chatIdMap =  new HashMap<String,String>();

    public User(){}

    public User(String str){
        this.userId = UserManger.getInstance().getUserId();
        this.userName = UserManger.getInstance().getUserName();
        this.chatIdMap = UserManger.getInstance().getChatIdMap();
        this.facebookId = UserManger.getInstance().getFacebookId();
        this.userToken = UserManger.getInstance().getUserToken();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Map<String, String> getChatIdMap() {
        return chatIdMap;
    }

    public void setChatIdMap(Map<String, String> chatIdMap) {
        this.chatIdMap = chatIdMap;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
