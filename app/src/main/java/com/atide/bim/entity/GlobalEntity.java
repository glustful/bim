package com.atide.bim.entity;

import android.graphics.RectF;

/**
 * Created by atide on 2016/3/29.
 */
public class GlobalEntity {
    private static  GlobalEntity instance;

    private String sectId;
    private String partId;
    private String imageId;
    private int width;
    private int height;
    private String themeId = "1";


    public static GlobalEntity getInstance(){
        if (instance == null)
            instance = new GlobalEntity();
        return instance;
    }

    public String getSectId() {
        return sectId;
    }

    public void setSectId(String sectId) {
        this.sectId = sectId;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }
}
