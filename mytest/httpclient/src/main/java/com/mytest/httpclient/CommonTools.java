package com.mytest.httpclient;

import com.asprise.ocr.Ocr;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.poi.util.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;

public class CommonTools
{
  public static Date strToDate(String strDate, String pattern)
  {
    Date date = null;
    if (strDate == null) {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    try
    {
      date = sdf.parse(strDate);
    }
    catch (ParseException e)
    {
      System.out.println(e.getMessage());
    }
    return date;
  }
  
  public static String dateToStr(Date date, String pattern)
  {
    if (date == null) {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    try
    {
      return sdf.format(date);
    }
    catch (Exception e) {}
    return null;
  }
  
  public static String moveDay(String fromStrDate, String pattern, int day)
  {
    Date fromDate = strToDate(fromStrDate, pattern);
    Calendar cal = Calendar.getInstance();
    cal.setTime(fromDate);
    cal.add(5, day);
    Date toDate = cal.getTime();
    String toStrDate = dateToStr(toDate, pattern);
    return toStrDate;
  }
  
  public static String moveDay(Date beforeDate, String pattern, int day)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(beforeDate);
    cal.add(5, day);
    Date toDate = cal.getTime();
    String toStrDate = dateToStr(toDate, pattern);
    return toStrDate;
  }
  
  public static int obtaintRandom(int number)
  {
    Random random = new Random();
    return random.nextInt(number);
  }
  
  public static Date randomDate(String beginDate, String endDate, String pattern)
  {
    SimpleDateFormat format = new SimpleDateFormat(pattern);
    Date start = null;
    Date end = null;
    try
    {
      start = format.parse(beginDate);
      end = format.parse(endDate);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    if (start.getTime() >= end.getTime()) {
      return null;
    }
    long date = start.getTime() + (Math.random() * (end.getTime() - start.getTime()));
    return new Date(date);
  }
  
  public static String readerResult(HttpEntity entity)
    throws IOException
  {
    StringBuilder result = new StringBuilder();
    BufferedReader reader = null;
    try
    {
      reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }
    }
    finally
    {
      if (reader != null) {
        reader.close();
      }
    }
    String line;
    return result.toString();
  }
  
  protected void obtainJSONObject(String result)
  {
    try
    {
      JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
      

      JSONArray jsonArray = jsonObject.getJSONArray("children");
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < jsonArray.length(); i++)
      {
        JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i);
        builder.append(jsonObject2.getString("id"));
        builder.append(jsonObject2.getString("title"));
        builder.append(jsonObject2.getString("name"));
      }
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }
  
  public static List<Object> rsConvertList(ResultSet rs)
    throws SQLException
  {
    List<Object> list = new ArrayList();
    ResultSetMetaData md = rs.getMetaData();
    int columnCount = md.getColumnCount();
    while (rs.next())
    {
      Map<String, Object> rowData = new HashMap();
      for (int i = 1; i <= columnCount; i++)
      {
        if (md.getColumnTypeName(i) == "INT")
        {
          int value = rs.getInt(i);
          rowData.put(md.getColumnLabel(i), Integer.valueOf(value));
        }
        if (md.getColumnTypeName(i) == "DECIMAL")
        {
          double value = rs.getDouble(i);
          DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
          String valueStr = decimalFormat.format(value);
          rowData.put(md.getColumnLabel(i), valueStr);
        }
        else
        {
          rowData.put(md.getColumnLabel(i), rs.getString(i));
        }
      }
      list.add(rowData);
    }
    return list;
  }
  
  public static List<Object> rsColumnNotNullConvertList(ResultSet rs)
    throws SQLException
  {
    List<Object> list = new ArrayList();
    ResultSetMetaData md = rs.getMetaData();
    int columnCount = md.getColumnCount();
    while (rs.next())
    {
      Map<String, Object> rowData = new HashMap();
      for (int i = 1; i <= columnCount; i++) {
        if (md.getColumnTypeName(i) == "INT")
        {
          int value = rs.getInt(i);
          rowData.put(md.getColumnLabel(i), Integer.valueOf(value));
        }
        else if (md.getColumnTypeName(i) == "DECIMAL")
        {
          double value = rs.getDouble(i);
          DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
          String valueStr = decimalFormat.format(value);
          rowData.put(md.getColumnLabel(i), valueStr);
        }
        else if (rs.getString(i) != null)
        {
          rowData.put(md.getColumnLabel(i), rs.getString(i));
        }
      }
      list.add(rowData);
    }
    return list;
  }
  
  public static Timestamp getTimestamp(Date date)
  {
    return new Timestamp(date.getTime());
  }
  
  public static Timestamp parseTimestamp(String dateStr, String pattern)
  {
    Date date = strToDate(dateStr, pattern);
    return getTimestamp(date);
  }
  
  public static long dateStrToTimestamp(String dateStr, String pattern)
  {
    System.out.println(dateStr);
    Date date = strToDate(dateStr, pattern);
    
    long timestamp = date.getTime();
    timestamp /= 1000L;
    return timestamp;
  }
  
  public static String getTime(String user_time, String pattern)
  {
    String re_time = null;
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    try
    {
      Date d = sdf.parse(user_time);
      long l = d.getTime();
      String str = String.valueOf(l);
      re_time = str.substring(0, 10);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return re_time;
  }
  
  public static String getTimestampToTime(String timestamp, String pattern)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    
    String time = sdf.format(new Date(Long.parseLong(timestamp) * 1000L));
    
    return time;
  }
  
  public static String dateToStamp(String s, String pattern)
    throws ParseException
  {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    Date date = simpleDateFormat.parse(s);
    long ts = date.getTime() / 1000L;
    String res = String.valueOf(ts);
    return res;
  }
  
  public static String stampToDate(String s, String pattern)
  {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    long lt = new Long(s).longValue();
    Date date = new Date(lt);
    String res = simpleDateFormat.format(date);
    return res;
  }
  
  public static Date dataToDate(Date date, String pattern)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    String dateStr = sdf.format(date);
    Date thisDate = null;
    try
    {
      thisDate = sdf.parse(dateStr);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return thisDate;
  }
  
  public static String obtainIdentifyingCode(GetMethod getMethod, String ProjectPath, String tessdataPath)
    throws IOException
  {
    String picName = ProjectPath + "\\imgs\\";
    File filepic = new File(picName);
    if (!filepic.exists()) {
      filepic.mkdir();
    }
    String fileName = picName + new Date().getTime() + ".jpg";
    File filepicF = new File(fileName);
    InputStream inputStream = getMethod.getResponseBodyAsStream();
    OutputStream outStream = new FileOutputStream(filepicF);
    IOUtils.copy(inputStream, outStream);
    outStream.close();
    
    String resultCode = null;
    Tesseract tessreact = new Tesseract();
    tessreact.setDatapath(tessdataPath);
    ClearImageHelper.cleanImage(filepicF, picName);
    File fileAfterClear = new File(fileName);
    
    Ocr.setUp();
    Ocr ocr = new Ocr();
    ocr.startEngine("eng", "fastest", new Object[0]);
    String result = ocr.recognize(new File[] { fileAfterClear }, "text", "text", new Object[0]);
    if (result.contains("error: failed to read file:"))
    {
      Assert.fail("没有返回图形验证码。");
    }
    else
    {
      System.out.println("Result: " + result);
      

      resultCode = result.replace(",", "").replace("1", "i").replace(" ", "").replace("'", "").replace("“", "").replace("0", "O").replace("6", "g").replace("8", "B").replace("5", "S").replace("2", "Z").replace("]", "J");
      System.out.println("图片文字为:" + resultCode);
    }
    ocr.stopEngine();
    
    return resultCode;
  }
  
  public static String obtainIdentifyingCodeByOcr(GetMethod getMethod, String ProjectPath)
    throws IOException
  {
    String picName = ProjectPath + "\\imgs\\";
    File filepic = new File(picName);
    if (!filepic.exists()) {
      filepic.mkdir();
    }
    String fileName = picName + new Date().getTime() + ".jpg";
    File filepicF = new File(fileName);
    InputStream inputStream = getMethod.getResponseBodyAsStream();
    OutputStream outStream = new FileOutputStream(filepicF);
    IOUtils.copy(inputStream, outStream);
    outStream.close();
    
    Ocr.setUp();
    Ocr ocr = new Ocr();
    ocr.startEngine("eng", "fastest", new Object[0]);
    String result = ocr.recognize(new File[] { filepicF }, "text", "text", new Object[0]);
    String resultCode = null;
    if (result.contains("error: failed to read file:"))
    {
      Assert.fail("没有返回图形验证码。");
    }
    else
    {
      System.out.println("Result: " + result);
      






      resultCode = result.replace(",", "").replace("1", "i").replace(" ", "").replace("'", "").replace("“", "").replace("0", "O").replace("6", "g").replace("8", "B").replace("5", "S").replace("2", "Z").replace("]", "J");
      System.out.println("图片文字为:" + resultCode);
    }
    ocr.stopEngine();
    
    return resultCode;
  }
  
  public static String obtainIdentifyingCodeByTesseract(GetMethod getMethod, String ProjectPath, String tessdataPath)
    throws IOException
  {
    String picName = ProjectPath + "\\imgs\\";
    File filepic = new File(picName);
    if (!filepic.exists()) {
      filepic.mkdir();
    }
    String fileName = picName + new Date().getTime() + ".jpg";
    File filepicF = new File(fileName);
    InputStream inputStream = getMethod.getResponseBodyAsStream();
    OutputStream outStream = new FileOutputStream(filepicF);
    IOUtils.copy(inputStream, outStream);
    outStream.close();
    
    String resultCode = null;
    Tesseract tessreact = new Tesseract();
    tessreact.setDatapath(tessdataPath);
    try
    {
      ClearImageHelper.cleanImage(filepicF, picName);
      File fileAfterClear = new File(fileName);
      resultCode = tessreact.doOCR(fileAfterClear);
      System.out.println("图片文字为:" + resultCode);
    }
    catch (TesseractException e)
    {
      System.err.println(e.getMessage());
    }
    return resultCode;
  }
  
  public static String encodingName(String name)
  {
    String returnName = "";
    try
    {
      returnName = URLEncoder.encode(name, "UTF-8");
      returnName = StringUtils.replace(returnName, "+", "%20");
      if (returnName.length() > 150)
      {
        returnName = new String(name.getBytes("GB2312"), "ISO8859-1");
        returnName = StringUtils.replace(returnName, " ", "%20");
      }
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    return returnName;
  }
  
  @Deprecated
  public static String listToString(List list, char separator)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < list.size(); i++)
    {
      sb.append(list.get(i));
      if (i < list.size() - 1) {
        sb.append(separator);
      }
    }
    return sb.toString();
  }
}

