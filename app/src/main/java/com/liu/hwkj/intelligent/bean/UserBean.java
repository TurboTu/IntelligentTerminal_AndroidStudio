package com.liu.hwkj.intelligent.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 车辆信息
 *
 * @author LIU
 *
 */
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String Result = ""; //:0登录正常   1账户或者密码错误  2账户被锁定
	private String USER_ID = ""; //用户ID
	private String USER_ACCOUNT = ""; //用户账户
	private String USER_PWD = ""; //用户密码
	private String USER_NAME = ""; //用户名称
	private String DELETEMARK = ""; //账户状态
	private String USERGROUP_ID = ""; //用户分组ID

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String USER_ID) {
		this.USER_ID = USER_ID;
	}

	public String getUSER_ACCOUNT() {
		return USER_ACCOUNT;
	}

	public void setUSER_ACCOUNT(String USER_ACCOUNT) {
		this.USER_ACCOUNT = USER_ACCOUNT;
	}

	public String getUSER_PWD() {
		return USER_PWD;
	}

	public void setUSER_PWD(String USER_PWD) {
		this.USER_PWD = USER_PWD;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String USER_NAME) {
		this.USER_NAME = USER_NAME;
	}

	public String getDELETEMARK() {
		return DELETEMARK;
	}

	public void setDELETEMARK(String DELETEMARK) {
		this.DELETEMARK = DELETEMARK;
	}

	public String getUSERGROUP_ID() {
		return USERGROUP_ID;
	}

	public void setUSERGROUP_ID(String USERGROUP_ID) {
		this.USERGROUP_ID = USERGROUP_ID;
	}


}
