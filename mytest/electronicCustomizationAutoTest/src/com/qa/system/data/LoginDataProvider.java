package com.qa.system.data;

import org.testng.annotations.DataProvider;

import com.qa.util.DataInfo;

/**
 * 
 * @author cn
 * 
 *         系统管理：后台管理员登录
 *
 */
public class LoginDataProvider {

	/*
	 * enterprise-admin-test.shanghai.cosmoplat.com：系统管理：后台管理员登录
	 */
	@DataProvider(name = "SystemLoginData")
	public static Object[][] loginDataInfo() {
		return new DataInfo().getData("LoginData", "LoginData", "data/system/");
	}
}
