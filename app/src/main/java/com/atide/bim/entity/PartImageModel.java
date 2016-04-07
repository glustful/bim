package com.atide.bim.entity;

/**
 * Created by atide on 2016/3/25.
 */
public class PartImageModel {
    private String id;
    private String fileId;
    private String sectNo;
    private String descript;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getSectNo() {
        return sectNo;
    }

    public void setSectNo(String sectNo) {
        this.sectNo = sectNo;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
