package com.atide.bim.entity;

import java.io.Serializable;

/**
 * Created by atide on 2016/4/18.
 */
public class MeasuredItemValueEntity implements Serializable {
    private String ItemValueID;
    private String MeasuredItemID;
    private String AssessmentValueID;
    private String SectNo;
    private String PartNo;
    private String ItemValue;
    private String ItemCharValue;
    private String ItemValueSN;
    private String ProcessID;
    private String DatumID;
    private String AttachDataId;

    public String getItemValueID() {
        return ItemValueID;
    }

    public void setItemValueID(String itemValueID) {
        ItemValueID = itemValueID;
    }

    public String getMeasuredItemID() {
        return MeasuredItemID;
    }

    public void setMeasuredItemID(String measuredItemID) {
        MeasuredItemID = measuredItemID;
    }

    public String getAssessmentValueID() {
        return AssessmentValueID;
    }

    public void setAssessmentValueID(String assessmentValueID) {
        AssessmentValueID = assessmentValueID;
    }

    public String getSectNo() {
        return SectNo;
    }

    public void setSectNo(String sectNo) {
        SectNo = sectNo;
    }

    public String getPartNo() {
        return PartNo;
    }

    public void setPartNo(String partNo) {
        PartNo = partNo;
    }

    public String getItemValue() {
        return ItemValue;
    }

    public void setItemValue(String itemValue) {
        ItemValue = itemValue;
    }

    public String getItemCharValue() {
        return ItemCharValue;
    }

    public void setItemCharValue(String itemCharValue) {
        ItemCharValue = itemCharValue;
    }

    public String getItemValueSN() {
        return ItemValueSN;
    }

    public void setItemValueSN(String itemValueSN) {
        ItemValueSN = itemValueSN;
    }

    public String getProcessID() {
        return ProcessID;
    }

    public void setProcessID(String processID) {
        ProcessID = processID;
    }

    public String getDatumID() {
        return DatumID;
    }

    public void setDatumID(String datumID) {
        DatumID = datumID;
    }

    public String getAttachDataId() {
        return AttachDataId;
    }

    public void setAttachDataId(String attachDataId) {
        AttachDataId = attachDataId;
    }
}
