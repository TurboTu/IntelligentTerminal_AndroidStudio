package com.liu.hwkj.intelligent.bean;

import java.io.Serializable;
//故障信息
public class FaultTypeBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public String ID = ""; //故障类型ID
	public String TypeName = ""; //故障类型名称

	public String getTypeName() {
		return TypeName;
	}

	public void setTypeName(String typeName) {
		TypeName = typeName;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}


}
