package com.atide.bim.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by atide on 2016/4/14.
 */
public class QualityHeaderEntity implements Serializable{
    @SerializedName("measuredpartid")
    private String MeasuredPartID;
    @SerializedName("partno")
    private String PartNo;
    @SerializedName("datumid")
    private String DatumID;
    @SerializedName("inspectpartname")
    private String InspectPartName;
    @SerializedName("inspectpickets")
    private String InspectPickets;
    @SerializedName("tableno")
    private String TableNo;
    @SerializedName("partname")
    private String PartName;
    @SerializedName("divisionpartname")
    private String DivisionPartName;
    @SerializedName("inspectdate")
    private String InspectDate;
    @SerializedName("appendixparttype")
    private String AppendixPartType;
    private transient String identity;

    public String getMeasuredPartID() {
        return MeasuredPartID;
    }

    public void setMeasuredPartID(String measuredPartID) {
        MeasuredPartID = measuredPartID;
    }

    public String getPartNo() {
        return PartNo;
    }

    public void setPartNo(String partNo) {
        PartNo = partNo;
    }

    public String getDatumID() {
        return DatumID;
    }

    public void setDatumID(String datumID) {
        DatumID = datumID;
    }

    public String getInspectPartName() {
        return InspectPartName;
    }

    public void setInspectPartName(String inspectPartName) {
        InspectPartName = inspectPartName;
    }

    public String getInspectPickets() {
        return InspectPickets;
    }

    public void setInspectPickets(String inspectPickets) {
        InspectPickets = inspectPickets;
    }

    public String getTableNo() {
        return TableNo;
    }

    public void setTableNo(String tableNo) {
        TableNo = tableNo;
    }

    public String getPartName() {
        return PartName;
    }

    public void setPartName(String partName) {
        PartName = partName;
    }

    public String getDivisionPartName() {
        return DivisionPartName;
    }

    public void setDivisionPartName(String divisionPartName) {
        DivisionPartName = divisionPartName;
    }

    public String getInspectDate() {
        return InspectDate;
    }

    public void setInspectDate(String inspectDate) {
        InspectDate = inspectDate;
    }

    public String getAppendixPartType() {
        return AppendixPartType;
    }

    public void setAppendixPartType(String appendixPartType) {
        AppendixPartType = appendixPartType;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
