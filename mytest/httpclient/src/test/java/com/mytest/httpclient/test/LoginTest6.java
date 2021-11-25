package com.mytest.httpclient.test;

import java.io.IOException;
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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test; 
import com.mytest.httpclient.Util;
import com.alibaba.fastjson.JSON; 
import com.alibaba.fastjson.JSONObject; 

public class LoginTest6 {
	private static Logger log = Logger.getLogger(LoginTest6.class.getName());

}
