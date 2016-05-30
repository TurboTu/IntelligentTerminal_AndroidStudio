package com.liu.hwkj.intelligent.net;

import android.content.Context;

import com.google.gson.Gson;
import com.liu.hwkj.intelligentterminal.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceManager {

	public static final String TIME_OUT = "timeout"; // 连接超时
	public static final String NET_ERROR = "netError"; // 网络异常

	private Context context;

	private String nameSpace = "";
	private String endPoint = "";

	public WebServiceManager(Context context){
		this.context = context;
		nameSpace = context.getString(R.string.NAMESPACE);
        endPoint = context.getString(R.string.ENDPOINT);
	}

	/**
	 * 从服务器端获取数据
	 * @param methodName 服务器端方法
	 * @param paras
	 * @param resultClass
	 * @return
	 */
	public Object GetRemoteData(String methodName, String[][] paras, Class resultClass, String testStr){
		String result = WebServiceConnect(methodName, paras);
//        String result = testStr;
        Object obj = null;
        if(TIME_OUT.equals(result) || NET_ERROR.equals(result)){
            return obj;
        }

        Gson gson = new Gson();
        try{
            obj = gson.fromJson(result, resultClass);
        } catch (Exception e){
			return result;
        }
		return obj;
	}

    /**
     * 通过设备唯一号获取应用ID;
     *
     * @param macCode
     * @return
     */
    public String getAppKey(String macCode) {
        String[][] paras = { { "macCode", macCode } };
        // String result = PhoneWebServiceConnect("getAppKey", paras);
        // if (resultRight) {
        // return result;
        // }
        return "";
    }

    /**
     * 将获取到的地理信息插入到数据库中
     *
     * @param strPhone
     * @param lon
     * @param lat
     * @param appKey
     * @return
     */
    public String getMobileLBS(String strPhone, String lon, String lat, String appKey) {
        String[][] paras = { { "strPhone", strPhone }, { "lon", lon }, { "lat", lat }, { "appKey", appKey } };
        // String result = PhoneWebServiceConnect("getMobileLBS", paras);
        // if (resultRight) {
        // return result;
        // }
        return "";
    }

    /**
	 * 利用反射机制将json串转换成对象
	 * @param jsonString
	 * @param objClass
	 * @return
	 */
	public Object Json2Bean(String jsonString, Class objClass) {
		Object object = null;
		try {
			JSONObject json = new JSONObject(jsonString);
			object = objClass.newInstance();
			Field[] fields = objClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				if (json.has(fieldName)) {
					Method method = objClass.getDeclaredMethod(
							"set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), String.class);
					method.invoke(object, json.getString(fieldName).trim());
				}
				;
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return object;
	}


	/**
	 * 连接web服务
	 * @param methodName
	 * @param paras
	 * @return
	 */
	private String WebServiceConnect(String methodName, String[][] paras) {

		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);
		SoapSerializationEnvelope envelope = null;
		for (int i = 0; i < paras.length; i++) {
			rpc.addProperty(paras[i][0], paras[i][1]);
		}
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);

		try {
			// 调用WebService
			transport.call(nameSpace + methodName, envelope);
		} catch (SocketTimeoutException e) {
			// 连接超时
			return TIME_OUT;
		} catch (ConnectException e) {
			return NET_ERROR;
		} catch (Exception e) {
			return e.getMessage();
		}

		// 获取返回的数据
		String result = null;
		SoapObject object = null;

		try {
			object = (SoapObject) envelope.bodyIn;
			// 获取返回的结果
			if (object != null) {
				result = object.getProperty(0).toString();
				return result;
			}
			return "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
}
