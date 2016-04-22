package com.atide.bim.ui.quality;

/**
 * Created by atide on 2016/4/19.
 */
public enum  DatumDataStatus {
    DECLARESTATUS("0","待申报"),
    RETURNSTATUS("7","退回"),
    RETURN2STATUS("-7","由上级审核部门退回"),
    APPROVALSTATUS("1","待审核"),
    FINISHSTATUS("9","审核完成");
    private String status;
    private String statusName;

    private DatumDataStatus(String status,String statusName){
        this.status = status;
        this.statusName = statusName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public static DatumDataStatus get(String status){
        switch (status){
            case "0":
                return DECLARESTATUS;

            case "7":
                return RETURNSTATUS;
            case "-7":
                return RETURN2STATUS;
            case "1":
                return APPROVALSTATUS;
            case "9":
                return FINISHSTATUS;
            default:
                return DECLARESTATUS;
        }

    }
}
