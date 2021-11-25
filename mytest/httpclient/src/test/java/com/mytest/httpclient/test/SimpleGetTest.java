package com.mytest.httpclient.test; 
 
import java.io.IOException;  
import org.apache.http.HttpEntity;                                                                                                                                      import org.apache.http.client.ClientProtocolException; 
import org.apache.http.client.methods.CloseableHttpResponse; 
import org.apache.http.client.methods.HttpGet;                                                                                                                          import org.apache.http.impl.client.CloseableHttpClient; 
import org.apache.http.impl.client.HttpClients;                                                                                      import org.apache.http.util.EntityUtils; 
 
public class SimpleGetTest { 
 	String url; 
 	CloseableHttpClient client; 
 	CloseableHttpResponse response; 
 	HttpEntity responseBody; 
 	int responseCode; 
 
 	public void sendGet() throws Throwable { 
 	 	String url = "https://reqres.in/api/users?pages=2"; 
 	 	// 创建一个 httpClient 的实例 
 	 	client = HttpClients.createDefault(); 
 	 	// 创建一个 httpGet 请求实例 
 	 	HttpGet httpGet = new HttpGet(url); 
 	 	try { 
 	 	 	// 使用 httpClient 实例发送刚创建的 get 请求，并用httpResponse 将响应接收 
 	 	 	response = client.execute(httpGet); 
 	 	 	// 从响应中提取出状态码  
 	 	 	responseCode = response.getStatusLine().getStatusCode(); 
 	 	 	System.out.println(responseCode); 
 	 	 	// 从响应中提取出响应主体 
 	 	 	responseBody = response.getEntity(); 
 	 	 	// 转为字符串 
 	 	 	String responseBodyString = EntityUtils.toString(responseBody, "utf-8"); 
 	 	 	System.out.println(responseBodyString); 
 	 	 	EntityUtils.consume(responseBody); 
 	 	} catch (ClientProtocolException cpe) { 
 	 	 	cpe.printStackTrace(); 
 	 	} catch (IOException ioe) { 
 	 	 	ioe.printStackTrace(); 
 	 	}finally { 	 
 	 	 	client.close(); 
 	 	 	response.close(); 
 	 	}   
 	} 
 
 	public static void main(String[] args) throws Throwable { 
 	 	SimpleGetTest simpleGetTest = new SimpleGetTest(); 
 	 	simpleGetTest.sendGet(); 
 	} 
} 
