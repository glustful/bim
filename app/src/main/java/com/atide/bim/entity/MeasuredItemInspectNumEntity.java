package com.atide.bim.entity;

import java.io.Serializable;

/**
 * Created by atide on 2016/4/18.
 */
public class MeasuredItemInspectNumEntity implements Serializable {
    private String InspectID;
    private String MeasuredItemID;
    private String DatumID;
    private String InspectNum;
    private String InputType;
    private String IsDeviation;
    private String IsAttachRep;

    public String getInspectID() {
        return InspectID;
    }

    public void setInspectID(String inspectID) {
        InspectID = inspectID;
    }

    public String getMeasuredItemID() {
        return MeasuredItemID;
    }

    public void setMeasuredItemID(String measuredItemID) {
        MeasuredItemID = measuredItemID;
    }

    public String getDatumID() {
        return DatumID;
    }

    public void setDatumID(String datumID) {
        DatumID = datumID;
    }

    public String getInspectNum() {
        return InspectNum;
    }

    public void setInspectNum(String inspectNum) {
        InspectNum = inspectNum;
    }

    public String getInputType() {
        return InputType;
    }

    public void setInputType(String inputType) {
        InputType = inputType;
    }

    public String getIsDeviation() {
        return IsDeviation;
    }

    public void setIsDeviation(String isDeviation) {
        IsDeviation = isDeviation;
    }

    public String getIsAttachRep() {
        return IsAttachRep;
    }

    public void setIsAttachRep(String isAttachRep) {
        IsAttachRep = isAttachRep;
    }
}
