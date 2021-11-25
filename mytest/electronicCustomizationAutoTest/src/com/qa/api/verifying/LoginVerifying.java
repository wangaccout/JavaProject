package com.qa.api.verifying;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;

import com.qa.util.DBUtils;

public class LoginVerifying {
	
	//用户名密码都为空、用户名输入密码不输入、用户名不输入密码输入
	public static void emptyLoginVerifying(ITestContext context, String result, String desc) {
		JSONObject results = new JSONObject(result);
		
		Assert.assertTrue(results.has("success"));
		Assert.assertEquals(results.get("success"), false);
		
		Assert.assertTrue(results.has("data"));
		
		Assert.assertEquals(results.get("code").toString(), "null");
		
//		JSONObject data = results.getJSONObject("data");
		
		if(desc.equals("用户名输入密码不输入")) {
			Assert.assertEquals(results.getString("message"), "请输入密码");
		}else {
			Assert.assertEquals(results.getString("message"), "请输入手机号");
		}
	}
	
	//用户名正确密码错误
	public static void errLoginVerifying(ITestContext context, String result) {
		JSONObject results = new JSONObject(result);
		
		Assert.assertTrue(results.has("success"));
		Assert.assertEquals(results.get("success"), false);
		
		Assert.assertTrue(results.has("data"));
		
		Assert.assertEquals(results.get("code").toString(), "null");
		Assert.assertEquals(results.getString("message"), "用户名或密码错误");
		
//		JSONObject data = results.getJSONObject("data");
//		
//		Assert.assertEquals(data.getString("code"), "null");
//		Assert.assertEquals(data.getString("message"), "用户名或密码错误");
	}
	
	//用户名密码都正确
	public static void rightLoginVerifying(ITestContext context, String result) {
		JSONObject results = new JSONObject(result);
		
		Assert.assertTrue(results.has("success"));
		Assert.assertEquals(results.get("success"), true);
		
		Assert.assertTrue(results.has("data"));
		
//        JSONObject data = results.getJSONObject("data");
		
		Assert.assertEquals(results.getString("code"), "200");
	}
}














