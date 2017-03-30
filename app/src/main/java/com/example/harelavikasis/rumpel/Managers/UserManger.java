package com.example.harelavikasis.rumpel.Managers;

import com.example.harelavikasis.rumpel.Models.User;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import com.example.harelavikasis.rumpel.Events.UserReadyEvent;

/**
 * Created by harelavikasis on 19/03/2017.
 */
public class UserManger {
    private static UserManger ourInstance = new UserManger();
    private String userId;
    private String userName;
    private Boolean isSet = false;
    private Boolean isChatMapSet = false;
    private String facebookId;
    private String userToken;
    boolean itHappenAlready = false;
    boolean contactListIsReady = false;

    private Map<String,String> chatIdMap =  new HashMap<String,String>();

    public static UserManger getInstance() {
        return ourInstance;
    }

    private UserManger() {
    }

    public void setWithUser(User user){
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.chatIdMap = user.getChatIdMap();
        this.facebookId = user.getFacebookId();
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
        checkIfUserSet();
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        checkIfUserSet();
    }

    public Map<String, String> getChatIdMap() {
        return chatIdMap;
    }

    public void setChatIdMap(Map<String, String> chatIdMap) {
        this.chatIdMap = chatIdMap;
        this.isChatMapSet = true;
    }

    public void addChatId(String chatId, String endPointUserId){
        this.chatIdMap.put(endPointUserId,chatId);
    }

    public String getChatIdWithendPointUserId(String endPointUserId) {
        return this.chatIdMap.get(endPointUserId);
    }
    public Boolean isChatMapSet(){
        return this.isChatMapSet;
    }
    public Boolean isSet(){
        return this.isSet;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
        checkIfUserSet();
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
        checkIfUserSet();
    }

    public void notifyContactListIsready()
    {
        this.contactListIsReady = true;
        checkIfUserSet();
    }
    private void checkIfUserSet() {
        if (userToken == null || facebookId == null || userName == null || userId == null || !this.contactListIsReady)
        {
            isSet = false;
        }
        else{
            isSet = true;
        }
        if (isSet() && !itHappenAlready){
            itHappenAlready = true;
            EventBus.getDefault().post(new UserReadyEvent());
        }
    }
}
