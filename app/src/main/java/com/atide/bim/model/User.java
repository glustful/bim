package com.atide.bim.model;

import org.json.JSONObject;

/**
 * Created by atide on 2016/3/24.
 */
public class User {

    private String userId;
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private static User lastUser;
    public static User getLoginUser(){
        if (lastUser == null)
            lastUser = new User();
        return lastUser;
    }

    public void init(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject info = jsonObject.optJSONObject("data");
            if (info == null)
                return;
            userId = info.optString("userid","");
            token = info.optString("token","");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
