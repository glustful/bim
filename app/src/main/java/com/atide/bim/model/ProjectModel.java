package com.atide.bim.model;

import java.io.Serializable;

/**
 * Created by atide on 2016/3/18.
 */
public class ProjectModel implements Serializable{
    private String title;
    private String descript;
    private boolean hasChild;
    private int photoCount;
    private String _id;
    private String imageNums;

    public String getImageNums() {
        return imageNums;
    }

    public void setImageNums(String imageNums) {
        this.imageNums = imageNums;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
