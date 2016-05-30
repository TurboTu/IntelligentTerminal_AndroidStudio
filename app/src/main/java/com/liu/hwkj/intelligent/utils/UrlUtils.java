/*
 * Hw Browser for Android
 * 
 * Copyright (C) 2010 J. Devauchelle and contributors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.liu.hwkj.intelligent.utils;


/**
 * Url management utils.
 */
public class UrlUtils {
	
	public static String replaceOrAddParas(String key, String value, String url) {
		String result = "";
		String[] urlParasArr = url.split("\\?");
		Boolean hasKey = false;
		if(urlParasArr.length < 2){
			return url + "?" + key + "=" + value;
		}
		String parasString = urlParasArr[1];
		
		if(parasString != null){
			String[] parasArr = parasString.split("\\&");
			for(int i = 0; i < parasArr.length; i++){
				String kvStr = parasArr[i];
				if(kvStr == null){
					continue;
				}
				String[] kvArr = kvStr.split("\\=");
				if(kvArr[0] != null && kvArr[0].equals(key)){
					kvArr[1] = value;
					hasKey = true;
				}
				parasArr[i] = kvArr[0] + "=" + kvArr[1];
				if(i == 0){
					result = parasArr[0];
				}
				else{
					result +=  "&" + parasArr[i];
				}
			}
			result = urlParasArr[0] + "?" + result;
			if(!hasKey){
				result += "&" + key + "=" + value;
			}
		}
		else{
			result = urlParasArr[0] + "?" + key + "=" + value;
		}
		return result;
	}
	
	public static String removeParas(String key, String value, String url) {
		String result = "";
		String[] urlParasArr = url.split("\\?");
		if(urlParasArr.length < 2){
			return url;
		}
		String parasString = urlParasArr[1];
		
		if(parasString != null){
			String[] parasArr = parasString.split("\\&");
			for(int i = 0; i < parasArr.length; i++){
				String kvStr = parasArr[i];
				if(kvStr != null && kvStr.indexOf(key) != -1){
					parasArr[i] = "";
				}
			}
			if(parasArr.length > 1){
				result = parasArr[0];
			}
			for(int i = 1; i < parasArr.length; i++){
				if(!"".equals(parasArr[i])){
					if("".equals(result)){
						result += parasArr[i];
					}
					else{
						result += "&" + parasArr[i];
					}
				}
			}
			if("".equals(result)){
				result = urlParasArr[0];
			}
			else{
				result = urlParasArr[0] + "?" + result;
			}
		}
		else{
			result = urlParasArr[0];
		}
		return result;
	}
}
