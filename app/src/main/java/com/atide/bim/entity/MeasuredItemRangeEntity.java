package com.atide.bim.entity;

import java.io.Serializable;

/**
 * Created by atide on 2016/4/19.
 */
public class MeasuredItemRangeEntity implements Serializable {
    private String measureddetailid;
    private String measureditemid;
    private String standardid;
    private String measureddetailname;
    private String standardvalue;
    private String remarks;
    private String calmethod;
    private String upperlogical;
    private String lowerlogical;
    private String assessmenttype;
    private String assessmentmethod;

    public String getMeasureddetailid() {
        return measureddetailid;
    }

    public void setMeasureddetailid(String measureddetailid) {
        this.measureddetailid = measureddetailid;
    }

    public String getMeasureditemid() {
        return measureditemid;
    }

    public void setMeasureditemid(String measureditemid) {
        this.measureditemid = measureditemid;
    }

    public String getStandardid() {
        return standardid;
    }

    public void setStandardid(String standardid) {
        this.standardid = standardid;
    }

    public String getMeasureddetailname() {
        return measureddetailname;
    }

    public void setMeasureddetailname(String measureddetailname) {
        this.measureddetailname = measureddetailname;
    }

    public String getStandardvalue() {
        return standardvalue;
    }

    public void setStandardvalue(String standardvalue) {
        this.standardvalue = standardvalue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCalmethod() {
        return calmethod;
    }

    public void setCalmethod(String calmethod) {
        this.calmethod = calmethod;
    }

    public String getUpperlogical() {
        return upperlogical;
    }

    public void setUpperlogical(String upperlogical) {
        this.upperlogical = upperlogical;
    }

    public String getLowerlogical() {
        return lowerlogical;
    }

    public void setLowerlogical(String lowerlogical) {
        this.lowerlogical = lowerlogical;
    }

    public String getAssessmenttype() {
        return assessmenttype;
    }

    public void setAssessmenttype(String assessmenttype) {
        this.assessmenttype = assessmenttype;
    }

    public String getAssessmentmethod() {
        return assessmentmethod;
    }

    public void setAssessmentmethod(String assessmentmethod) {
        this.assessmentmethod = assessmentmethod;
    }
}
