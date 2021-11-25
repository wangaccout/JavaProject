package com.mytest.httpclient.test;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
//import java.lang.System.Logger;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException; 
import org.apache.http.client.methods.CloseableHttpResponse; 
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mytest.httpclient.HttpClientUtil;
import com.mytest.httpclient.Util;
import com.alibaba.fastjson.JSON; 
import com.alibaba.fastjson.JSONObject; 

import org.testng.annotations.Test;

public class LoginTest {
	
	@DataProvider(name = "SystemLoginData")
	public Object[][] provideData(){
		  return new Object[][] {{"admin",123456}};
	}
	
	private static Logger log = Logger.getLogger(LoginTest6.class.getName());
	
	@Test(dataProvider = "SystemLoginData")
  	public void loginTest(ITestContext context, String userName, String password) {
		String interfaceUrl = "/system/admin/login.json";
		String interfaceDesc = "enterprise-admin-test.shanghai.cosmoplat.com：系统管理：后台管理员登录接口";

		log.info(interfaceDesc + interfaceUrl + "  开始>>>>>>>>>>>");
		System.out.println("测试场景：" + interfaceDesc + " ---------------------------------> ");

		String domainName = System.getProperty("admin.URL");
		StringBuilder url = new StringBuilder(domainName);
		url.append(interfaceUrl);
		System.out.println(url.toString());
		/*
		 * 封装请求参数-
		 */
		JSONObject jsonObjectParams = new JSONObject();
		jsonObjectParams.put("userName", userName);
		String encrypted = AESUtil.encrypt(password, System.getProperty("uuc.KEY"), System.getProperty("uuc.IV"));
		jsonObjectParams.put("password", encrypted);
		System.out.println(jsonObjectParams.toString());
		/*
		 * 请求头
		 */
		Map<String, String> headerMap = new TreeMap<String, String>();
		headerMap.put("Content-Type", "application/json; charset=utf-8");
		/*
		 * 请求、响应
		 */
		String resultData = HttpClientUtil.httpPostCookieUtil(context, url.toString(), jsonObjectParams, headerMap);
		System.out.println("返回内容：" + resultData);

  }
}
