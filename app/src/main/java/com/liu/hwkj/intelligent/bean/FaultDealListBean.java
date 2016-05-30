package com.liu.hwkj.intelligent.bean;

import java.io.Serializable;
import java.util.List;

//故障处理中的故障列表
public class FaultDealListBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private List<FaultDealBean> PointFault;

	public List<FaultDealBean> getPointFault() {
		return PointFault;
	}

	public void setPointFault(List<FaultDealBean> pointFault) {
		PointFault = pointFault;
	}

}
