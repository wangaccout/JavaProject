package com.mytest.httpclient.test;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
//import org.apache.commons.codec.binary.Base64;

public class AESUtil
{
  public static String encrypt(String data, String key, String iv)
  {
    try
    {
      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      int blockSize = cipher.getBlockSize();
      
      byte[] dataBytes = data.getBytes();
      int plaintextLength = dataBytes.length;
      if (plaintextLength % blockSize != 0) {
        plaintextLength += blockSize - plaintextLength % blockSize;
      }
      byte[] plaintext = new byte[plaintextLength];
      System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
      
      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
      IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
      
      cipher.init(1, keyspec, ivspec);
      byte[] encrypted = cipher.doFinal(plaintext);
      
      return new Base64().encodeToString(encrypted);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static String desEncrypt(String data, String key, String iv)
  {
    try
    {
      if (data.isEmpty()) {
        return data;
      }
      byte[] encrypted1 = new Base64().decode(data);
      
      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
      IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
      
      cipher.init(2, keyspec, ivspec);
      
      byte[] original = cipher.doFinal(encrypted1);
      return new String(original);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static String encryptData(String data, String hexKey)
  {
    try
    {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      
      SecretKeySpec key = new SecretKeySpec(hexKey.getBytes(), "AES");
      
      cipher.init(1, key);
      
      byte[] encrypted = cipher.doFinal(data.getBytes());
      
      return new BASE64Encoder().encode(encrypted);
//      return Base64.encodeBase64String(encrypted);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static String decryptData(String base64Data, String hexKey)
    throws Exception
  {
    try
    {
      if (base64Data.isEmpty()) {
        return base64Data;
      }
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      
      SecretKeySpec key = new SecretKeySpec(hexKey.getBytes(), "AES");
      
      cipher.init(2, key);
      
      byte[] original = cipher.doFinal(new BASE64Decoder().decodeBuffer(base64Data));
//      byte[] original = cipher.doFinal(new BASE64Decoder().decodeBuffer(base64Data));
      return new String(original);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  private static byte[] hex2byte(String s)
  {
    if (s.length() % 2 == 0) {
      return hex2byte(s.getBytes(), 0, s.length() >> 1);
    }
    return hex2byte("0" + s);
  }
  
  private static byte[] hex2byte(byte[] b, int offset, int len)
  {
    byte[] d = new byte[len];
    for (int i = 0; i < len * 2; i++)
    {
      int shift = i % 2 == 1 ? 0 : 4; int 
        tmp35_34 = (i >> 1); byte[] tmp35_30 = d;tmp35_30[tmp35_34] = ((byte)(tmp35_30[tmp35_34] | Character.digit((char)b[(offset + i)], 16) << shift));
    }
    return d;
  }
  
  public static String encrypt(String data, String key)
  {
    try
    {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      int blockSize = cipher.getBlockSize();
      
      byte[] dataBytes = data.getBytes();
      int plaintextLength = dataBytes.length;
      if (plaintextLength % blockSize != 0) {
        plaintextLength += blockSize - plaintextLength % blockSize;
      }
      byte[] plaintext = new byte[plaintextLength];
      System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
      
      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
      
      cipher.init(1, keyspec);
      byte[] encrypted = cipher.doFinal(plaintext);
      
      return new Base64().encodeToString(encrypted);
    }
    catch (Exception e) {}
    return null;
  }
  
  public static String desEncrypt(String data, String key)
  {
    try
    {
      byte[] encrypted1 = new Base64().decode(data);
      
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
      
      cipher.init(2, keyspec);
      
      byte[] original = cipher.doFinal(encrypted1);
      return new String(original, "utf-8").trim();
    }
    catch (Exception e) {}
    return null;
  }
}

