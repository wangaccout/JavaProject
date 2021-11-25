package com.qa.system.verifying;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;

public class LoginVerifying {

	/*
	 * enterprise-admin-test.shanghai.cosmoplat.com：系统管理：后台管理员登录
	 */
	public static void loginVerifying(ITestContext context, String resultData, String interfaceUrl, String account) {
		// 创建一个JSON对象
		JSONObject jsonObject = new JSONObject(resultData);

		// message
		Assert.assertTrue(jsonObject.has("message"), interfaceUrl + " ：返回json结果中没有message结果");

		// success
		Assert.assertTrue(jsonObject.has("success"), interfaceUrl + " ：返回json结果中没有success结果");
		Assert.assertEquals(jsonObject.get("success").toString().toLowerCase().toString(), "true");

		// total
		Assert.assertTrue(jsonObject.has("total"), interfaceUrl + " ：返回json结果中没有total结果");
		Assert.assertEquals(jsonObject.get("total").toString(), "null");

		// code
		Assert.assertTrue(jsonObject.has("code"), interfaceUrl + " ：返回json结果中没有code结果");

		if (jsonObject.get("success").toString().toLowerCase().equals("true")) {
			// data
			Assert.assertTrue(jsonObject.has("data"), interfaceUrl + " ：返回json结果中没有data结果");
			if (jsonObject.get("data") != null && jsonObject.get("data").toString() != ""
					&& !jsonObject.get("data").toString().equals("null")) {
				JSONObject data = jsonObject.getJSONObject("data");

				// id
//				Assert.assertTrue(data.has("id"), interfaceUrl + " ：返回json结data果中没有id结果");
//				Assert.assertNotNull(data.get("id"));
//				context.setAttribute("sysUserId", data.get("id").toString());

				// userType
//				Assert.assertTrue(data.has("userType"), interfaceUrl + " ：返回json结果data中没有userType结果");

				// userName
				Assert.assertTrue(data.has("userName"), interfaceUrl + " ：返回json结果data中没有userName结果");
				Assert.assertNotNull(data.get("userName"));
				Assert.assertEquals(data.get("userName").toString(), account);
				context.setAttribute("userName", data.get("userName").toString());

				// name
//				Assert.assertTrue(data.has("userName"), interfaceUrl + " ：返回json结果data中没有name结果");

				// isDelete
//				Assert.assertTrue(data.has("isDelete"), interfaceUrl + " ：返回json结果data中没有isDelete结果");

				// token
				Assert.assertTrue(data.has("token"), interfaceUrl + " ：返回json结果data中没有token结果");
				Assert.assertNotNull(data.get("token"));
				context.setAttribute("token", data.get("token").toString());
			}
		}
	}

}

