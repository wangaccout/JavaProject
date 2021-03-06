package com.qa.system.testcases;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.qa.system.verifying.LoginVerifying;
import com.qa.util.AESUtil;
import com.qa.util.HttpClientUtil;

/**
 * 
 * @author cn
 * 
 *         系统管理：后台管理员登录
 *
 */
public class LoginTest {

	private static Logger log = Logger.getLogger(LoginTest.class.getName());

	/*
	 * enterprise-admin-test.shanghai.cosmoplat.com：系统管理：后台管理员登录
	 */
	@Test(dataProvider = "SystemLoginData", dataProviderClass = com.qa.system.data.LoginDataProvider.class)
	public static void loginTest(ITestContext context, String account, String password) {
		String interfaceUrl = "/admin/login.json";
		String interfaceDesc = "test-electronic.shanghai.cosmoplat.com：系统管理：后台管理员登录接口";

		log.info(interfaceDesc + interfaceUrl + "  开始>>>>>>>>>>>");
		System.out.println("测试场景：" + interfaceDesc + " ---------------------------------> ");

		String domainName = System.getProperty("admin.URL"); //获取系统参数
		StringBuilder url = new StringBuilder(domainName);  //可更改的string
		url.append(interfaceUrl);
		System.out.println(url.toString());


		/*
		 * 封装请求参数-
		 */
		JSONObject jsonObjectParams = new JSONObject();
		jsonObjectParams.put("account", account);
//		String encrypted = AESUtil.encrypt(password, System.getProperty("uuc.KEY"), System.getProperty("uuc.IV"));
		jsonObjectParams.put("password", password);
		System.out.println(jsonObjectParams.toString());
		
//		List<NameValuePair> params = Lists.newArrayList();
//		params.add(new BasicNameValuePair("userName", userName));
//		params.add(new BasicNameValuePair("passWord", password));
//
//		try {
//			if (params.size() != 0) {
//				// 转换为键值对
//				String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
//				url.append("?").append(str);
//			}
//			System.out.println(url.toString());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		/*
		 * 请求头
		 */
		Map<String, String> headerMap = new TreeMap<String, String>();
		headerMap.put("Content-Type", "application/json; charset=utf-8");
		System.out.println(headerMap);

		/*
		 * 请求、响应
		 */
		String resultData = HttpClientUtil.httpPostCookieUtil(context, url.toString(), jsonObjectParams, headerMap);
		System.out.println("返回内容：" + resultData);
		
//		String resultData = HttpClientUtil.httpGetCookieUtil(context, url.toString(), null);
//		System.out.println("返回内容：" + resultData);

		/*
		 * 校验
		 */
		LoginVerifying.loginVerifying(context, resultData, interfaceUrl.toString(),account);

	}

}