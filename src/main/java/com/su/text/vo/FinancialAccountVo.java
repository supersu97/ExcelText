package com.su.text.vo;


import com.alibaba.fastjson.annotation.JSONField;
import com.gzhc365.common.page.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 财务账单
 */
public class FinancialAccountVo extends PageParam {

    private static final long serialVersionUID = 1444891456720895877L;
    /**
     * 账单id
     */
    private String id;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 发薪号
     */
    private String payNo;

    /**
     * 支付时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    /**
     * 缴费金额
     */
    private String totalFee;

    /**
     * 交易类型
     */
    private String bizType;

    /**
     * 审核状态
     */
    private String auditStatus;

    private String payType;

    private String startTime;

    private String endTime;

    /**
     * 交易流水号
     */
    private String payOrdNum;

    /**
     * 支付订单号
     */
    private String payNum;

    /**
     * 数量
     */
    private String unit;

    //扩展字段(json格式)
    private String extFieldsViews;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayOrdNum() {
        return payOrdNum;
    }

    public void setPayOrdNum(String payOrdNum) {
        this.payOrdNum = payOrdNum;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExtFieldsViews() {
        return extFieldsViews;
    }

    public void setExtFieldsViews(String extFieldsViews) {
        this.extFieldsViews = extFieldsViews;
    }

    public String getPayNum() {
        return payNum;
    }

    public void setPayNum(String payNum) {
        this.payNum = payNum;
    }
}

