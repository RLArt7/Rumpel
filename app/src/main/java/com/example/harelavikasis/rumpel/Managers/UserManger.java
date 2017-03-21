package com.example.harelavikasis.rumpel.Managers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by harelavikasis on 19/03/2017.
 */
public class UserManger {
    private static UserManger ourInstance = new UserManger();
    private String userId;
    private String userName;
    private String facebookToken;
    private Map<String,String> chatIdMap =  new HashMap<String,String>();

    public static UserManger getInstance() {
        return ourInstance;
    }

    private UserManger() {
    }

    public static UserManger getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(UserManger ourInstance) {
        UserManger.ourInstance = ourInstance;
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

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public Map<String, String> getChatIdMap() {
        return chatIdMap;
    }

    public void setChatIdMap(Map<String, String> chatIdMap) {
        this.chatIdMap = chatIdMap;
    }

    public void addChatId(String chatId, String endPointUserId){
        this.chatIdMap.put(endPointUserId,chatId);
    }

    public String getChatIdWithendPointUserId(String endPointUserId) {
        return this.chatIdMap.get(endPointUserId);
    }
}
