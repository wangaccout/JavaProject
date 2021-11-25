package com.qa.system.data;

import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;

import com.qa.util.CaseHelper;
import com.qa.util.Constants;
import com.qa.util.DBUtils;
import com.qa.util.ReadExcel;
import com.qa.util.ReadSqlData;

public class DemandDataProvider {
//	protected static String caseExcelPath = Constants.ProjectPath + "\\data\\system\\DemandData.xlsx";
//	
//	@DataProvider(name = "ListDataFromExcel")
//	public static Object[][] ListDataFromExcelInfo(){
//		System.out.println(caseExcelPath);
//		Object[][] myObj = null;
//		List<Map<String, String>> list = ReadExcel.readStringXlsx(caseExcelPath, "ListData");
//		myObj = CaseHelper.getObjArrByListOnlyParam(list);
//		return myObj;
//	}
	protected static String caseExcelPath = Constants.ProjectPath + "\\data\\system\\DemandData.xlsx";

	/*
	 * http://hyl-test.shanghai.cosmoplat.com/admin：企业资质审核：列表页
	 */
	@DataProvider(name = "ListDataFromExcel")
	public static Object[][] ListDataFromExcelInfo() {
		System.out.println(caseExcelPath);
		Object[][] myObj = null;
		List<Map<String, String>> list = ReadExcel.readStringXlsx(caseExcelPath, "ListData");
		myObj = CaseHelper.getObjArrByListOnlyParam(list);
		return myObj;
	}
}














