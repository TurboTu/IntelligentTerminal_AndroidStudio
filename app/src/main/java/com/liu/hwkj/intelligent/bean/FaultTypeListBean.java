package com.liu.hwkj.intelligent.bean;

import java.io.Serializable;
import java.util.List;

//故障类型列表
public class FaultTypeListBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private List<FaultTypeBean> FaultTypeList;

	public List<FaultTypeBean> getFaultTypeList() {
		return FaultTypeList;
	}

	public void setFaultTypeList(List<FaultTypeBean> faultTypeList) {
		FaultTypeList = faultTypeList;
	}
}
