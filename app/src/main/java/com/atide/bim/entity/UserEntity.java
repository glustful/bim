package com.atide.bim.entity;

/**
 * Created by atide on 2016/4/1.
 */
public class UserEntity {
    private String unitid;
    private String unitname;
    private String userid;
    private String username;
    private String pos;
    private String mobilephone;
    private String editmode;

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getEditmode() {
        return editmode;
    }

    public void setEditmode(String editmode) {
        this.editmode = editmode;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof  UserEntity){
            UserEntity entity = (UserEntity)o;
            if (entity.getUserid().equals(userid))
                return true;
            else {
                return false;
            }
        }
        return super.equals(o);
    }

}
