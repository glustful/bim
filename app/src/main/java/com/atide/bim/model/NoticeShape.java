package com.atide.bim.model;

import android.graphics.Bitmap;

import com.atide.bim.R;

/**
 * Created by atide on 2016/3/21.
 */
public class NoticeShape extends PointShape {
    private String content;
    private boolean isBorder;
    private int font = 10;//10pt,12pt,14pt,16pt
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isBorder() {
        return isBorder;
    }

    public void setIsBorder(boolean isBorder) {
        this.isBorder = isBorder;
    }

    public int getFont() {
        return font;
    }

    public void setFont(int font) {
        this.font = font;
    }

    @Override
    public int getIcon() {
        return R.drawable.notice_icon;
    }
}
