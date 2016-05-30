package com.liu.hwkj.intelligent.bean;

import java.io.Serializable;

//故障处理中的故障类
public class FaultDealBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String ID = ""; //故障信息ID
	private String TCID = ""; //任务ID
	private String TCPID = ""; //巡检点ID
	private String FaultTitle = ""; //故障信息标题
	private String FaultType = ""; //故障类型ID
	private String FaultContent = ""; //故障描述
	private String CreateTime = ""; //添加时间
	private String UserID = ""; //故障上报人
	private String HasSend = ""; //分发标识
	private String SUserID = ""; //故障处理巡检员ID
	private String SendTime = ""; //分发时间
	private String HasDeal = ""; //处理状态标识 0未处理 1已处理
	private String DealContent = ""; //故障处理描述
	private String DealUID = ""; //故障处理人ID
	private String DealTime = ""; //故障处理时间
	private String SetNoAlert = ""; //故障消警状态 0未消警  1已消警
	private String SetUID = ""; //消警人ID
	private String SetTime = ""; //消警时间
	private String DelMark = ""; //删除标识

	public String getDelMark() {
		return DelMark;
	}

	public void setDelMark(String delMark) {
		DelMark = delMark;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getTCID() {
		return TCID;
	}

	public void setTCID(String TCID) {
		this.TCID = TCID;
	}

	public String getTCPID() {
		return TCPID;
	}

	public void setTCPID(String TCPID) {
		this.TCPID = TCPID;
	}

	public String getFaultTitle() {
		return FaultTitle;
	}

	public void setFaultTitle(String faultTitle) {
		FaultTitle = faultTitle;
	}

	public String getFaultType() {
		return FaultType;
	}

	public void setFaultType(String faultType) {
		FaultType = faultType;
	}

	public String getFaultContent() {
		return FaultContent;
	}

	public void setFaultContent(String faultContent) {
		FaultContent = faultContent;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getHasSend() {
		return HasSend;
	}

	public void setHasSend(String hasSend) {
		HasSend = hasSend;
	}

	public String getSUserID() {
		return SUserID;
	}

	public void setSUserID(String SUserID) {
		this.SUserID = SUserID;
	}

	public String getSendTime() {
		return SendTime;
	}

	public void setSendTime(String sendTime) {
		SendTime = sendTime;
	}

	public String getHasDeal() {
		return HasDeal;
	}

	public void setHasDeal(String hasDeal) {
		HasDeal = hasDeal;
	}

	public String getDealContent() {
		return DealContent;
	}

	public void setDealContent(String dealContent) {
		DealContent = dealContent;
	}

	public String getDealUID() {
		return DealUID;
	}

	public void setDealUID(String dealUID) {
		DealUID = dealUID;
	}

	public String getDealTime() {
		return DealTime;
	}

	public void setDealTime(String dealTime) {
		DealTime = dealTime;
	}

	public String getSetNoAlert() {
		return SetNoAlert;
	}

	public void setSetNoAlert(String setNoAlert) {
		SetNoAlert = setNoAlert;
	}

	public String getSetUID() {
		return SetUID;
	}

	public void setSetUID(String setUID) {
		SetUID = setUID;
	}

	public String getSetTime() {
		return SetTime;
	}

	public void setSetTime(String setTime) {
		SetTime = setTime;
	}


}
