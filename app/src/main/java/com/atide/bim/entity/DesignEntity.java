package com.atide.bim.entity;

import java.io.Serializable;

/**
 * 质检设计标准值
 * Created by atide on 2016/4/13.
 */
public class DesignEntity implements Serializable{
    private String upperlimitvalue;
    private String lowerlimitvalue;
    private String datacollcode;
    private String datatype; //当DataType ==0(数值)时，将值存入UpperLimitValue, LowerLimitValue为空
    private String datacollid;
    private String datacollname;
    private String datacollsn;
    private String datasysbom;

    public String getUpperlimitvalue() {
        return upperlimitvalue;
    }

    public void setUpperlimitvalue(String upperlimitvalue) {
        this.upperlimitvalue = upperlimitvalue;
    }

    public String getLowerlimitvalue() {
        return lowerlimitvalue;
    }

    public void setLowerlimitvalue(String lowerlimitvalue) {
        this.lowerlimitvalue = lowerlimitvalue;
    }

    public String getDatacollcode() {
        return datacollcode;
    }

    public void setDatacollcode(String datacollcode) {
        this.datacollcode = datacollcode;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getDatacollid() {
        return datacollid;
    }

    public void setDatacollid(String datacollid) {
        this.datacollid = datacollid;
    }

    public String getDatacollname() {
        return datacollname;
    }

    public void setDatacollname(String datacollname) {
        this.datacollname = datacollname;
    }

    public String getDatacollsn() {
        return datacollsn;
    }

    public void setDatacollsn(String datacollsn) {
        this.datacollsn = datacollsn;
    }

    public String getDatasysbom() {
        return datasysbom;
    }

    public void setDatasysbom(String datasysbom) {
        this.datasysbom = datasysbom;
    }
}
