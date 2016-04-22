package com.atide.bim.entity;

import java.io.Serializable;

/**
 * Created by atide on 2016/4/18.
 */
public class DesignValueEntity implements Serializable {
    private String ValueCollID;
    private String DataCollID;
    private String SectNo;
    private String PartNo;
    private String UpperLimitValue;
    private String LowerLimitValue;
    private String FormatText;

    public String getValueCollID() {
        return ValueCollID;
    }

    public void setValueCollID(String valueCollID) {
        ValueCollID = valueCollID;
    }

    public String getDataCollID() {
        return DataCollID;
    }

    public void setDataCollID(String dataCollID) {
        DataCollID = dataCollID;
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

    public String getUpperLimitValue() {
        return UpperLimitValue;
    }

    public void setUpperLimitValue(String upperLimitValue) {
        UpperLimitValue = upperLimitValue;
    }

    public String getLowerLimitValue() {
        return LowerLimitValue;
    }

    public void setLowerLimitValue(String lowerLimitValue) {
        LowerLimitValue = lowerLimitValue;
    }

    public String getFormatText() {
        return FormatText;
    }

    public void setFormatText(String formatText) {
        FormatText = formatText;
    }
}
