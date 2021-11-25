package com.qa.api.testcases;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.json.JSONObject; 

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.qa.api.verifying.*;
import com.qa.util.HttpClientUtil;

public class LoginTest {
	private static Logger log = Logger.getLogger(LoginTest.class.getName());
	
	@Test(dataProvider = "LoginData", dataProviderClass = com.qa.api.data.LoginData.class)
	public static void loginTest(ITestContext context, String phone, String password, String desc) {
		String results = login(context, phone, password, desc);
		
		if (desc.equals("用户名密码都为空时") || desc.equals("用户名输入密码不输入") || desc.equals("用户名不输入密码输入")) {
			LoginVerifying.emptyLoginVerifying(context, results, desc);
		}else if(desc.equals("用户名正确密码错误")) {
			LoginVerifying.errLoginVerifying(context, results);
		}else {
			LoginVerifying.rightLoginVerifying(context, results);
		}
	}

	private static String login(ITestContext context, String phone, String password, String desc) {
		String interfaceUrl = "/regist/passwordLogin";
		String interfaceDesc = "测试场景：前端登录test-electronic.shanghai.cosmoplat.com";
		log.info(interfaceDesc + interfaceUrl + "开始>>>>>>>>>>>>>>>>>" + desc);
		
		StringBuilder url = new StringBuilder(System.getProperty("admin.URL"));
		url.append(interfaceUrl);
		System.out.println("url--" + url);
		
		JSONObject params = new JSONObject();
		params.put("phone", phone);
		params.put("password", password);
		System.out.println(params.toString());
		
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json;charset=UTF-8");
		System.out.println(headerMap);
		
		String results = HttpClientUtil.httpPostCookieUtil(context, url.toString(), params, headerMap);
		System.out.println("result = " + results);
		System.out.println( "结束>>>>>>>>>>>>>");
		
		return results;
	}
	

}
