package com.example.machenike.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import java.net.URL;


import org.json.JSONObject;

public class HttpUtils {

//	 发送Http请求，处理服务器返回的消息
//	 @param url 服务器地址
//	 @param obj请求体数据json格式
//	 @return服务器返回的消息String类型
	public static String doPost(String url1, JSONObject obj){
		String result = null;
		HttpURLConnection httpURLConnection = null;
		InputStreamReader input = null;
		try {
			//定位要获取的资源网址，打开连接url
			URL url = new URL(url1);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			//进行各种设置 包括请求方法，请求头，请求体（HttpsURLConnection）
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
			//请求体的传输通过IO流
			OutputStreamWriter output =new OutputStreamWriter(httpURLConnection.getOutputStream());
			output.write(obj.toString());
			output.flush(); // 刷新
			output.close();	// 关闭

			//处理服务器响应消息(IO流)
			input = new InputStreamReader(httpURLConnection.getInputStream());
			BufferedReader reader = new BufferedReader(input);
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line = reader.readLine()) != null){
				sb.append(line);
			}
			result = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			//关闭
			if(httpURLConnection != null){
				httpURLConnection.disconnect();
			}
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}






	public static String doGet(String url1){
		String result = "";
		HttpURLConnection httpURLConnection = null;
		BufferedReader input = null;
		//1.URL 根据服务器地址 跟服务器取得连接
		try {
			URL url = new URL(url1);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			//进行各种设置 包括请求方法，请求头，请求体（HttpsURLConnection）
			httpURLConnection.setRequestMethod("GET");
			//httpURLConnection.setRequestProperty("Content-Type", "application/json");
			//httpURLConnection.connect();
			input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String line;
			while((line = input.readLine()) != null){
				result += line;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(httpURLConnection != null){
				httpURLConnection.disconnect();
			}
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		//2.请求方式 请求头

		//3.服务器返回的消息 处理String (IO流——》字符串)
		return result;
	}


















}
