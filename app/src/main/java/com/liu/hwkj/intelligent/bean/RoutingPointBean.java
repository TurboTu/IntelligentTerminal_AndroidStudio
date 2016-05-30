package com.liu.hwkj.intelligent.bean;

import java.io.Serializable;

public class RoutingPointBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ID = ""; //巡检点ID
	private String TCID = ""; //任务ID
	private String PointName = ""; //巡检点名称
	private String PointContent = ""; //巡检点巡检内容
	private String PointRFID = ""; //巡检点标签代码，NFC通讯用
	private String PointLa = ""; //经度坐标
	private String PointLo = ""; //维度坐标
	private String PointOrder = ""; //巡检点巡检顺序，按从小到大排列
	private String UserID = ""; //巡检员ID
	private String HasState = ""; //巡检标识，0未巡检 1已巡检
	private String XunJianTime = ""; //巡检时间
	private String HasFault = ""; //故障状态，0无故障 1有故障
	private String CreateTime = ""; //添加时间
	private String DelMark = ""; //
	private String WenDu = ""; //温度报警值
	private String QiTi = ""; //气体浓度报警值

	public String getQiTi() {
		return QiTi;
	}

	public void setQiTi(String qiTi) {
		QiTi = qiTi;
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

	public String getPointName() {
		return PointName;
	}

	public void setPointName(String pointName) {
		PointName = pointName;
	}

	public String getPointContent() {
		return PointContent;
	}

	public void setPointContent(String pointContent) {
		PointContent = pointContent;
	}

	public String getPointRFID() {
		return PointRFID;
	}

	public void setPointRFID(String pointRFID) {
		PointRFID = pointRFID;
	}

	public String getPointLa() {
		return PointLa;
	}

	public void setPointLa(String pointLa) {
		PointLa = pointLa;
	}

	public String getPointLo() {
		return PointLo;
	}

	public void setPointLo(String pointLo) {
		PointLo = pointLo;
	}

	public String getPointOrder() {
		return PointOrder;
	}

	public void setPointOrder(String pointOrder) {
		PointOrder = pointOrder;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getHasState() {
		return HasState;
	}

	public void setHasState(String hasState) {
		HasState = hasState;
	}

	public String getXunJianTime() {
		return XunJianTime;
	}

	public void setXunJianTime(String xunJianTime) {
		XunJianTime = xunJianTime;
	}

	public String getHasFault() {
		return HasFault;
	}

	public void setHasFault(String hasFault) {
		HasFault = hasFault;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getDelMark() {
		return DelMark;
	}

	public void setDelMark(String delMark) {
		DelMark = delMark;
	}

	public String getWenDu() {
		return WenDu;
	}

	public void setWenDu(String wenDu) {
		WenDu = wenDu;
	}



}
