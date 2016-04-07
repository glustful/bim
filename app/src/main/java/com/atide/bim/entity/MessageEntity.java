package com.atide.bim.entity;

/**
 * Created by atide on 2016/4/1.
 */
public class MessageEntity {
    private String messageid;
    private String themeid;
    private String drawingimageid;
    private String senduserid;
    private String message;
    private String senddate;

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getThemeid() {
        return themeid;
    }

    public void setThemeid(String themeid) {
        this.themeid = themeid;
    }

    public String getDrawingimageid() {
        return drawingimageid;
    }

    public void setDrawingimageid(String drawingimageid) {
        this.drawingimageid = drawingimageid;
    }

    public String getSenduserid() {
        return senduserid;
    }

    public void setSenduserid(String senduserid) {
        this.senduserid = senduserid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenddate() {
        return senddate;
    }

    public void setSenddate(String senddate) {
        this.senddate = senddate;
    }
}
