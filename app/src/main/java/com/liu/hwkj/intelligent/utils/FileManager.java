package com.liu.hwkj.intelligent.utils;


public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.liu.hwkj.easymanager/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.liu.hwkj.easymanager/files/";
		}
	}
}
