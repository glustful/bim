package com.atide.bim.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by atide on 2016/4/18.
 */
public class QualityDatumEntity implements Serializable {
    @SerializedName("datumid")
    private String DatumID;
    @SerializedName("ruleid")
    private String RuleID;
    @SerializedName("partno")
    private String PartNo;
    @SerializedName("sectno")
    private String SectNo;
    @SerializedName("datumname")
    private String DatumName;
    @SerializedName("usedunit")
    private String UsedUnit;
    @SerializedName("approvalstatus")
    private String ApprovalStatus;
    @SerializedName("approvalunitid")
    private String ApprovalUnitID;
    @SerializedName("approvalunitstep")
    private String ApprovalUnitStep;
    @SerializedName("approvalgrpid")
    private String ApprovalGrpID;
    @SerializedName("approvalgrpstep")
    private String ApprovalGrpStep;
    @SerializedName("confirmdate")
    private String ConfirmDate;
    @SerializedName("declaredate")
    private String DeclareDate;
    @SerializedName("inputuserid")
    private String InputUserID;
    @SerializedName("inputdate")
    private String InputDate;
    @SerializedName("flowid")
    private String FlowID;
    @SerializedName("templateid")
    private String TemplateID;
    @SerializedName("templatesource")
    private String TemplateSource;
    @SerializedName("parentid")
    private String ParentID;
    @SerializedName("ordersn")
    private String OrderSN;
    @SerializedName("processpartno")
    private String ProcessPartNo;
    @SerializedName("reldatumid")
    private String RelDatumID;
    @SerializedName("datumtime")
    private String DatumTime;
    private transient String collId;

    public String getDatumID() {
        return DatumID;
    }

    public void setDatumID(String datumID) {
        DatumID = datumID;
    }

    public String getRuleID() {
        return RuleID;
    }

    public void setRuleID(String ruleID) {
        RuleID = ruleID;
    }

    public String getPartNo() {
        return PartNo;
    }

    public void setPartNo(String partNo) {
        PartNo = partNo;
    }

    public String getSectNo() {
        return SectNo;
    }

    public void setSectNo(String sectNo) {
        SectNo = sectNo;
    }

    public String getDatumName() {
        return DatumName;
    }

    public void setDatumName(String datumName) {
        DatumName = datumName;
    }

    public String getUsedUnit() {
        return UsedUnit;
    }

    public void setUsedUnit(String usedUnit) {
        UsedUnit = usedUnit;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public String getApprovalUnitID() {
        return ApprovalUnitID;
    }

    public void setApprovalUnitID(String approvalUnitID) {
        ApprovalUnitID = approvalUnitID;
    }

    public String getApprovalUnitStep() {
        return ApprovalUnitStep;
    }

    public void setApprovalUnitStep(String approvalUnitStep) {
        ApprovalUnitStep = approvalUnitStep;
    }

    public String getApprovalGrpID() {
        return ApprovalGrpID;
    }

    public void setApprovalGrpID(String approvalGrpID) {
        ApprovalGrpID = approvalGrpID;
    }

    public String getApprovalGrpStep() {
        return ApprovalGrpStep;
    }

    public void setApprovalGrpStep(String approvalGrpStep) {
        ApprovalGrpStep = approvalGrpStep;
    }

    public String getConfirmDate() {
        return ConfirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        ConfirmDate = confirmDate;
    }

    public String getDeclareDate() {
        return DeclareDate;
    }

    public void setDeclareDate(String declareDate) {
        DeclareDate = declareDate;
    }

    public String getInputUserID() {
        return InputUserID;
    }

    public void setInputUserID(String inputUserID) {
        InputUserID = inputUserID;
    }

    public String getInputDate() {
        return InputDate;
    }

    public void setInputDate(String inputDate) {
        InputDate = inputDate;
    }

    public String getFlowID() {
        return FlowID;
    }

    public void setFlowID(String flowID) {
        FlowID = flowID;
    }

    public String getTemplateID() {
        return TemplateID;
    }

    public void setTemplateID(String templateID) {
        TemplateID = templateID;
    }

    public String getTemplateSource() {
        return TemplateSource;
    }

    public void setTemplateSource(String templateSource) {
        TemplateSource = templateSource;
    }

    public String getParentID() {
        return ParentID;
    }

    public void setParentID(String parentID) {
        ParentID = parentID;
    }

    public String getOrderSN() {
        return OrderSN;
    }

    public void setOrderSN(String orderSN) {
        OrderSN = orderSN;
    }

    public String getProcessPartNo() {
        return ProcessPartNo;
    }

    public void setProcessPartNo(String processPartNo) {
        ProcessPartNo = processPartNo;
    }

    public String getRelDatumID() {
        return RelDatumID;
    }

    public void setRelDatumID(String relDatumID) {
        RelDatumID = relDatumID;
    }

    public String getDatumTime() {
        return DatumTime;
    }

    public void setDatumTime(String datumTime) {
        DatumTime = datumTime;
    }

    public String getCollId() {
        return collId;
    }

    public void setCollId(String collId) {
        this.collId = collId;
    }
}
