package com.mytest.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException; 
import org.apache.http.client.config.RequestConfig;  
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete; 
import org.apache.http.client.methods.HttpGet; 
import org.apache.http.client.methods.HttpPost; 
import org.apache.http.client.methods.HttpPut; 
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients; 
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON; 
import com.alibaba.fastjson.JSONObject; 
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.Map.Entry;
import org.apache.http.Header;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.CharsetUtils;
import org.testng.Assert;
import org.testng.ITestContext;

public class HttpClientUtil {
	private CloseableHttpClient httpClient;
	private CloseableHttpResponse response;
	private RequestConfig requestConfig;
	private String HTTPSTATUS = "HttpStatus";
	
	public HttpClientUtil() {
		requestConfig = RequestConfig.custom().setConnectTimeout(5000).
				setConnectionRequestTimeout(1000).setSocketTimeout(10000).build();
	}
	
    /**
    *
    *	@param connectTimeout	设置连接超时时间，单位毫秒。
    *	@param connectionRequestTimeout 设置从connect Manager(连接池)获取 Connection
    *	超时时间，单位毫秒。这个属性是新加的属性，
    因为目前版本是可以共享连接池的。
    * @param socketTimeout	请求获取数据的超时时间(即响应时间)，单位 毫秒。
    *	如果访问一个接口，多少时间内无法返回数据， 就直接放弃此次调用。
    */

	public HttpClientUtil(int connectTimeout, int connectionRequestTimeout, int socketTimeout) {
		requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout)
				.setConnectionRequestTimeout(connectionRequestTimeout)
				.setSocketTimeout(socketTimeout).build();
	}
	
	public JSONObject sendGet(String url, HashMap<String, String> params, HashMap<String, String> headers) throws Exception {
		httpClient = HttpClients.createDefault();
		//拼接URL
		if (params != null) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			for (Map.Entry<String,String> entry:params.entrySet()) {
				String value = entry.getValue();
				if (!value.isEmpty()){
					pairs.add(new BasicNameValuePair(entry.getKey(),value));
				}
			}
		url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs),"UTF-8");
		}
		HttpGet httpGet = new HttpGet(url);
		try {
			httpGet.setConfig(requestConfig);
		    //加载请求头到httpget对象
			if(headers != null) {
				for (Map.Entry<String, String> entry:headers.entrySet()) {
					httpGet.setHeader(entry.getKey(), entry.getValue());;
				}
			}
			response = httpClient.execute(httpGet);
			//获取返回参数
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			//释放请求，关闭连接
			EntityUtils.consume(entity);
			JSONObject jsonobj = JSON.parseObject(result);
			jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
			return jsonobj;
	}finally {
		httpClient.close();
		response.close();
		}
	}
	public JSONObject sendGet(String url, HashMap<String, String> params)throws Exception {
		 return this.sendGet(url, params, null);
    }
	public JSONObject sendGet(String url) throws Exception {
		return this.sendGet(url, null, null);
	}
	
//	****************************************post
	public JSONObject sendPostByJson(String url, Object object, HashMap<String, String> headers) throws Exception {
	    httpClient = HttpClients.createDefault(); 
	    HttpPost httpPost = new HttpPost(url); 
	    try {
	        httpPost.setConfig(requestConfig);
	        String json = JSON.toJSONString(object);
	        StringEntity entity = new StringEntity(json, "utf-8");         
	        entity.setContentType("application/json"); httpPost.setEntity(entity);
	        if (headers != null) {
	            // 加载请求头到httppost对象
	            for (Map.Entry<String, String> entry : headers.entrySet()) {
	                httpPost.setHeader(entry.getKey(), entry.getValue());
	            }
	        }
	        response = httpClient.execute(httpPost); 
	        HttpEntity responseEntity = response.getEntity(); String result = null;
	        if (responseEntity != null) {
	            result = EntityUtils.toString(responseEntity, "UTF-8");
	        }
	        EntityUtils.consume(responseEntity); 
	        JSONObject jsonobj = JSON.parseObject(result);
	        jsonobj.put(HTTPSTATUS, response.getStatusLine().getStatusCode());
	        return jsonobj;
	    } finally {
	        httpClient.close(); 
	        response.close();
	    }
	}
	
	public static String httpPostUtil(ITestContext context, String url, Object ObjectParams) {
		String resultData = null;

		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;

		HttpPost httpPost = new HttpPost(url.toString());

		try {
			/*
			 * 设置参数
			 */
			if (ObjectParams != null) {// JSONObject
				if (ObjectParams instanceof JSONObject) {
					httpPost.setEntity(new StringEntity(ObjectParams.toString(), "UTF-8"));
				} else if (ObjectParams instanceof List) {// List
					@SuppressWarnings("unchecked")
					UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
							(List<? extends NameValuePair>) ObjectParams, "UTF-8");
					formEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

					httpPost.setEntity(formEntity);
				} else if (ObjectParams instanceof MultipartEntity) { // MultipartEntity
					/*
					 * 添加文本类型参数
					 */
					httpPost.setEntity((MultipartEntity) ObjectParams);
				}
			}

			/*
			 * 设置请求头消息agent
			 */
			httpPost.addHeader("Accept", "application/json, text/plain, */*");
			httpPost.addHeader("Accept-Encoding", "gzip, deflate");
			httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			httpPost.addHeader("User-Agent",
					"Mozilla/5.0 (Linux; Android 7.0; SM-G892A Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/67.0.3396.87 Mobile Safari/537.36");

			if (context.getAttribute("sid") != null) {
				httpPost.addHeader("Cookie", "sid=" + context.getAttribute("sid").toString());
			}

			/*
			 * 执行post请求
			 */
			response = httpClient.execute(httpPost);

			/*
			 * 返回服务器响应
			 */
			HttpEntity entity = response.getEntity();

			/*
			 * 响应状态码
			 */
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println("响应状态：" + statusCode);

			/*
			 * 断言响应的状态码
			 */
			String result = Integer.toString(statusCode);
			Assert.assertEquals(result, "200");
			if (statusCode != HttpStatus.SC_OK) {
				Assert.fail("返回状态码为：" + result);
			}

			/*
			 * 断言：返回内容是否为空
			 */
			if (entity == null || entity.getContent() == null) {
				Assert.fail("响应返回内容为空。");
			}

			/*
			 * 读取
			 */
			resultData = CommonTools.readerResult(entity);

		} catch (java.net.SocketException e) {
			Assert.fail("java.net.SocketException: Connection reset");
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 消耗实体内容
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// 关闭响应，丢弃http连接
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return resultData;
	}

	public static String httpPostCookieUtil(ITestContext context, String url, Object ObjectParams, Map<String, String> headerMap)
	  {
	    String resultData = null;
	    
	    CloseableHttpClient httpClient = HttpClients.createDefault();
	    CloseableHttpResponse response = null;
	    
	    HttpPost httpPost = new HttpPost(url.toString());
	    try
	    {
	      UrlEncodedFormEntity formEntity;
	      if (ObjectParams != null) {
	        if (((ObjectParams instanceof org.json.JSONObject)) || ((ObjectParams instanceof net.sf.json.JSONObject)))
	        {
	          httpPost.setEntity(new StringEntity(ObjectParams.toString(), "UTF-8"));
	        }
	        else if ((ObjectParams instanceof List))
	        {
	          formEntity = new UrlEncodedFormEntity((List)ObjectParams, "UTF-8");
	          
	          formEntity.setContentEncoding(new BasicHeader("Content-Type", "application/json"));
	          
	          httpPost.setEntity(formEntity);
	        }
	        else if ((ObjectParams instanceof MultipartEntity))
	        {
	          httpPost.setEntity((MultipartEntity)ObjectParams);
	        }
	        else if ((ObjectParams instanceof HttpEntity))
	        {
	          httpPost.setEntity((HttpEntity)ObjectParams);
	        }
	        else if ((ObjectParams instanceof String))
	        {
	          httpPost.setEntity(new StringEntity(ObjectParams.toString(), "UTF-8"));
	        }
	      }
	      httpPost.addHeader("Accept", "application/json, text/plain, */*");
	      httpPost.addHeader("Accept-Encoding", "gzip, deflate");
	      httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
	      httpPost.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 7.0; SM-G892A Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/67.0.3396.87 Mobile Safari/537.36");
	      if ((headerMap != null) && (headerMap.size() > 0)) {
	        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
	          httpPost.addHeader((String)entry.getKey(), (String)entry.getValue());
	        }
	      }
	      response = httpClient.execute(httpPost);
	      



	      HttpEntity entity = response.getEntity();
	      



	      int statusCode = response.getStatusLine().getStatusCode();
	      System.out.println("响应状态：" + statusCode);
	      



	      String result = Integer.toString(statusCode);
	      Assert.assertEquals(result, "200");
	      if (statusCode != 200) {
	        Assert.fail("返回状态码为：" + result);
	      }
	      if ((entity == null) || (entity.getContent() == null)) {
	        Assert.fail("响应返回内容为空。");
	      }
	      return CommonTools.readerResult(entity);
	    }
	    catch (SocketException e)
	    {
	      Assert.fail("java.net.SocketException: Connection reset");
	    }
	    catch (ParseException e)
	    {
	      e.printStackTrace();
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    finally
	    {
	      if (response != null) {
	        try
	        {
	          response.close();
	        }
	        catch (IOException e)
	        {
	          e.printStackTrace();
	        }
	      }
	      if (httpClient != null) {
	        try
	        {
	          httpClient.close();
	        }
	        catch (IOException e)
	        {
	          e.printStackTrace();
	        }
	      }
	    }
	    return resultData;
	  }
	
	
}
















