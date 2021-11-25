package com.qa.system.verifying;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;

import com.qa.util.DBUtils;

public class DemandVerifying {
	public static void listVerifying(ITestContext context, String resultData, String interfaceUrl, String auditState, String pageNumber, 
			String pageSize, String searchName , String searchNo, String serviceCate1, String serviceCate2, String startTime) {
		
		JSONObject jsonObject = new JSONObject(resultData);
		
		//code
		Assert.assertTrue(jsonObject.has("code"), interfaceUrl + ":返回json结果中没有code结果");
		Assert.assertEquals(jsonObject.get("code").toString(), "200");
		
		//data
		Assert.assertTrue(jsonObject.has("data"), interfaceUrl + ":返回json结果中没有data结果");
		
		//message
		Assert.assertTrue(jsonObject.has("message"), interfaceUrl + ":返回json结果中没有message结果");
		
		//success
		Assert.assertTrue(jsonObject.has("success"), interfaceUrl + ":返回json结果中没有success结果");
		Assert.assertEquals(jsonObject.get("success").toString().toLowerCase().toString(), "true");
		
		//url
		Assert.assertTrue(jsonObject.has("url"), interfaceUrl + ":返回json结果中没有url结果");
		
		if(jsonObject.get("success").toString().toLowerCase().equals("true")) {
			
//			JSONObject data = jsonObject.getJSONObject("data");
			
			net.sf.json.JSONArray fromDB = null;
			try {
				//链接数据库
				DBUtils.getDB(System.getProperty("sql.instance"));
				
				StringBuilder querySql = new StringBuilder("select");
				querySql.append(" da.id as id");
				querySql.append(" ,da.demand_no as demandNo");
				querySql.append(" ,sa1.service_cate_name as fieldStr");
				querySql.append(" ,sa2.service_cate_name as categoryStr");
				querySql.append(" ,da.demand_name as demandName");
				querySql.append(" ,mi.nickname as nickname");
				querySql.append(" ,da.audit_state as auditState");
				querySql.append(" ,da.bidding_state as biddingState");
				querySql.append(" ,case when ba.contract_signing IS NULL then \"-\" ELSE ba.contract_signing END \"contractSigningStr\"");
				querySql.append(" ,case when ba.payment_state IS NULL then \"-\" else ba.payment_state end \"paymentStateStr\"");
				querySql.append(" ,case when ba.receiving_state IS NULL then \"-\" else ba.receiving_state end \"receivingStateStr\"");
				querySql.append(" ,case when da.audit_state = 3 then da.audit_time else \"null\"  end \"auditTime\"");
				querySql.append(" from demand as da");
				querySql.append(" LEFT JOIN service_area as sa1 on sa1.id = da.service_cate_1");
				querySql.append(" LEFT JOIN service_area as sa2 on sa2.id = da.service_cate_2");
				querySql.append(" LEFT JOIN bidding_record as ba on ba.id = da.bidding_id");
				querySql.append(" LEFT JOIN member_info as mi on mi.id = da.member_id ");
				querySql.append(" WHERE da.del_flag = 0");
				
				if(!auditState.equals("")) {
					querySql.append(" and da.audit_state = ");
					querySql.append(auditState);
				}
				querySql.append(" ORDER BY (case when da.audit_state = 1 then 1"); 
				querySql.append(" when da.audit_state = 3 then 2"); 
				querySql.append(" when da.audit_state = 5 then 3 else 4 end)");
				querySql.append(" ,da.create_time DEsC");
				
				//获取数据库查询数据
				fromDB = DBUtils.resultJSONArray(querySql.toString());
				System.out.println(fromDB);
			}finally {
				//断开数据库连接
				DBUtils.closeDB(System.getProperty("sql.instance"));
			}
			
			JSONArray data = jsonObject.getJSONArray("data");
			
			if (jsonObject.get("data") != null) {
				int dataSize = data.length();
				if((fromDB == null && dataSize > 0) || (fromDB != null && fromDB.size() > 0) && dataSize == 0) {
					Assert.fail("接口返回数量与数据库中查询的数量不一致");
				}else if(fromDB != null && dataSize > 0) {
					Assert.assertEquals(jsonObject.getInt("total"), fromDB.size(), "数量不一致");
				}
			
				for(int i =0; i < data.length(); i++) {
					JSONObject info = data.getJSONObject(i);
					net.sf.json.JSONObject infoFromDB = fromDB.getJSONObject(i);
					
					//id
					Assert.assertTrue(info.has("id"), interfaceUrl + "：返回data结果信息中没有id");
					Assert.assertEquals(info.get("id").toString(), infoFromDB.get("id").toString());
					
					//auditState 审核状态
					Assert.assertTrue(info.has("auditState"), interfaceUrl + "：返回data结果信息中没有auditState");
					Assert.assertEquals(info.get("auditState").toString(), infoFromDB.get("auditState").toString());
					
					//biddingState 招标状态
					Assert.assertTrue(info.has("biddingState"), interfaceUrl + "：返回data结果信息中没有biddingState");
					Assert.assertEquals(info.get("biddingState").toString(), infoFromDB.get("biddingState").toString());
					
					//categoryStr 类别
					Assert.assertTrue(info.has("categoryStr"), interfaceUrl + "：返回data结果信息中没有categoryStr");
					Assert.assertEquals(info.get("categoryStr").toString(), infoFromDB.get("categoryStr").toString());
					
					//fieldStr 领域
					Assert.assertTrue(info.has("fieldStr"), interfaceUrl + "：返回data结果信息中没有fieldStr");
					Assert.assertEquals(info.get("fieldStr").toString(), infoFromDB.get("fieldStr").toString());
					
					//demandNo 需求编号
					Assert.assertTrue(info.has("demandNo"), interfaceUrl + "：返回data结果信息中没有demandNo");
					Assert.assertEquals(info.get("demandNo").toString(), infoFromDB.get("demandNo").toString());
					
					//demandName 需求名称
					Assert.assertTrue(info.has("demandName"), interfaceUrl + "：返回data结果信息中没有demandName");
					Assert.assertEquals(info.get("demandName").toString(), infoFromDB.get("demandName").toString());
					
					//createTime
//					Assert.assertTrue(info.has("createTime"), interfaceUrl + "：返回data结果信息中没有createTime");
//					Assert.assertEquals(info.get("createTime").toString(), infoFromDB.get("createTime").toString());
					
					//auditTime
					Assert.assertTrue(info.has("auditTime"), interfaceUrl + "：返回data结果信息中没有auditTime");
					Assert.assertEquals(info.get("auditTime").toString(), infoFromDB.get("auditTime").toString());
					
					//nickname 昵称
					Assert.assertTrue(info.has("nickname"), interfaceUrl + "：返回data结果信息中没有nickname");
					Assert.assertEquals(info.get("nickname").toString(), infoFromDB.get("nickname").toString(), "昵称不一样");
					
					//contractSigningStr 合同签订状态
					Assert.assertTrue(info.has("contractSigningStr"), interfaceUrl + "：返回data结果信息中没有contractSigningStr");
					Assert.assertEquals(info.get("contractSigningStr").toString(), infoFromDB.get("contractSigningStr").toString());
					
					//paymentStateStr 付款状态
					Assert.assertTrue(info.has("paymentStateStr"), interfaceUrl + "：返回data结果信息中没有paymentStateStr");
					Assert.assertEquals(info.get("paymentStateStr").toString(), infoFromDB.get("paymentStateStr").toString());
				
					//receivingStateStr 交付状态
					Assert.assertTrue(info.has("receivingStateStr"), interfaceUrl + "：返回data结果信息中没有receivingStateStr");
					Assert.assertEquals(info.get("receivingStateStr").toString(), infoFromDB.get("receivingStateStr").toString());
				}
			}
		}
		
		
	}

}





















