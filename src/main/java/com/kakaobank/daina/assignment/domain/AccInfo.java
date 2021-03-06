package com.kakaobank.daina.assignment.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class AccInfo {
    private String baccId;
    private String ctmId;
    private String ctmName;
    private LocalDate baccDate;
    private LocalTime baccTime;
    private String baccPart;
    private Long baccBalance;
    private String cardIssue;
    private String cardNum;
    private Long baccPass;
    private String baccStatus;
    private Long cntVer;

    public AccInfo() {
    }

    public AccInfo(String baccId, String ctmId, String ctmName, LocalDate baccDate, LocalTime baccTime, String baccPart, Long baccBalance, String cardIssue, String cardNum, Long baccPass, String baccStatus, Long cntVer) {
        this.baccId = baccId;
        this.ctmId = ctmId;
        this.baccDate = baccDate;
        this.baccTime = baccTime;
        this.baccPart = baccPart;
        this.baccBalance = baccBalance;
        this.cardIssue = cardIssue;
        this.cardNum = cardNum;
        this.baccPass = baccPass;
        this.baccStatus = baccStatus;
        this.cntVer = cntVer;
        this.ctmName = ctmName;
    }

    public String getCtmName() {
        return ctmName;
    }

    public void setCtmName(String ctmName) {
        this.ctmName = ctmName;
    }

    public Long getCntVer() {
        return cntVer;
    }

    public void setCntVer(Long cntVer) {
        this.cntVer = cntVer;
    }

    public String getBaccId() {
        return baccId;
    }

    public void setBaccId(String baccId) {
        this.baccId = baccId;
    }

    public String getCtmId() {
        return ctmId;
    }

    public void setCtmId(String ctmId) {
        this.ctmId = ctmId;
    }

    public LocalDate getBaccDate() {
        return baccDate;
    }

    public void setBaccDate(LocalDate baccDate) {
        this.baccDate = baccDate;
    }

    public LocalTime getBaccTime() {
        return baccTime;
    }

    public void setBaccTime(LocalTime baccTime) {
        this.baccTime = baccTime;
    }

    public String getBaccPart() {
        return baccPart;
    }

    public void setBaccPart(String baccPart) {
        this.baccPart = baccPart;
    }

    public Long getBaccBalance() {
        return baccBalance;
    }

    public void setBaccBalance(Long baccBalance) {
        this.baccBalance = baccBalance;
    }

    public String getCardIssue() {
        return cardIssue;
    }

    public void setCardIssue(String cardIssue) {
        this.cardIssue = cardIssue;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public Long getBaccPass() {
        return baccPass;
    }

    public void setBaccPass(Long baccPass) {
        this.baccPass = baccPass;
    }

    public String getBaccStatus() {
        return baccStatus;
    }

    public void setBaccStatus(String baccStatus) {
        this.baccStatus = baccStatus;
    }

    public void editMoney(Long baccBalance){
        this.baccBalance = baccBalance;
    }

    public void editCntVer(Long cntVer){
        this.cntVer = cntVer;
    }
}
