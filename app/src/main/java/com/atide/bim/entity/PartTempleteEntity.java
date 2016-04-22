package com.atide.bim.entity;

import java.io.Serializable;

/**
 * Created by atide on 2016/4/13.
 */
public class PartTempleteEntity implements Serializable{
    private String templateid;
    private String templatename;
    private String templatesource;
    private String constructflow;
    private String objectkey;
    private String parttempid;

    public String getTemplateid() {
        return templateid;
    }

    public void setTemplateid(String templateid) {
        this.templateid = templateid;
    }

    public String getTemplatename() {
        return templatename;
    }

    public void setTemplatename(String templatename) {
        this.templatename = templatename;
    }

    public String getTemplatesource() {
        return templatesource;
    }

    public void setTemplatesource(String templatesource) {
        this.templatesource = templatesource;
    }

    public String getConstructflow() {
        return constructflow;
    }

    public void setConstructflow(String constructflow) {
        this.constructflow = constructflow;
    }

    public String getObjectkey() {
        return objectkey;
    }

    public void setObjectkey(String objectkey) {
        this.objectkey = objectkey;
    }

    public String getParttempid() {
        return parttempid;
    }

    public void setParttempid(String parttempid) {
        this.parttempid = parttempid;
    }
}
