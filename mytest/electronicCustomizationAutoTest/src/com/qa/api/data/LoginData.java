package com.qa.api.data;

import org.apache.http.Consts;
import org.testng.annotations.DataProvider;

import com.qa.util.DataInfo;

public class LoginData {
	@DataProvider(name = "LoginData")
	public Object[][] getLoginData(){
		return new DataInfo().getData("LoginData", "LoginData", "data/api/");
	}
}
