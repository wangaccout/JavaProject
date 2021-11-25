package com.qa.system.testcases;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import com.qa.system.verifying.DemandVerifying;
import com.qa.util.CaseInfo;
import com.qa.util.HttpClientUtil;


public class DemandTest {
	
	private static Logger log = Logger.getLogger(DemandTest.class.getName());
	
	@Test(dataProvider = "ListDataFromExcel", dataProviderClass = com.qa.system.data.DemandDataProvider.class)
	public static void listTest(ITestContext context, CaseInfo c) {
		Map<String, String> map = c.getCaseParam();
		String auditState = map.get("auditState");
		String pageNumber = map.get("pageNumber");
		String pageSize = map.get("pageSize");
		String searchName = map.get("searchName");
		String searchNo = map.get("searchNo");
		String serviceCate1 = map.get("serviceCate1");
		String serviceCate2 = map.get("serviceCate2");
		String startTime = map.get("startTime");
		String describe = map.get("describe");
		
		StringBuilder url = new StringBuilder(System.getProperty("admin.URL"));
		String interfaceUrl = "/systemAuthor/systemDemand/getDemandList.json";
		String interfaceDesc = url + " : 需求列表接口";
		
		log.info(interfaceDesc + interfaceUrl + "开始>>>>>>>>>>");
		System.out.println("测试场景" + interfaceDesc + " ------------------------>" + describe);
		
		url.append(interfaceUrl);
		System.out.println(url.toString());
		
		//封装请求参数
		JSONObject jsonObjectParams = new JSONObject();
		jsonObjectParams.put("auditState", auditState);
		jsonObjectParams.put("pageNumber", pageNumber);
		jsonObjectParams.put("pageSize", pageSize);
		jsonObjectParams.put("searchName", searchName);
		jsonObjectParams.put("searchNo", searchNo);
		jsonObjectParams.put("serviceCate1", serviceCate1);
		jsonObjectParams.put("serviceCate2", serviceCate2);
		jsonObjectParams.put("startTime", startTime);
		System.out.println(jsonObjectParams.toString());
		
		//请求头
		Map<String, String> headerMap = new TreeMap<String, String>();
		headerMap.put("Authorization", context.getAttribute("token").toString());
		headerMap.put("Content-Type", "application/json; charset=utf-8");
		
		//响应
		String resultData = HttpClientUtil.httpPostCookieUtil(context, url.toString(), jsonObjectParams, headerMap);
		System.out.println("返回内容" + resultData);
		System.out.println("结果*************");
		
		//校验
		DemandVerifying.listVerifying(context, resultData, interfaceUrl.toString(), auditState, pageNumber, pageSize, 
				searchName, searchNo, serviceCate1, serviceCate2, startTime);
		System.out.println("校验结果*************");
		
		log.info(interfaceDesc + interfaceUrl + "结束>>>>>>>>>>>>>"); 
	}
}

















