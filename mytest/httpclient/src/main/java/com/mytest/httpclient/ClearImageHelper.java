package com.mytest.httpclient;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.ImageIO;

public class ClearImageHelper
{
  public static void main(String[] args)
    throws IOException
  {
    String destDir = Constants.ProjectPath + "\\imgs";
    File testDataDir = new File(destDir);
    for (File file : testDataDir.listFiles()) {
      cleanImage(file, destDir);
    }
  }
  
  public static void cleanImage(File sfile, String destDir)
    throws IOException
  {
    File destF = new File(destDir);
    if (!destF.exists()) {
      destF.mkdirs();
    }
    BufferedImage bufferedImage = ImageIO.read(sfile);
    int h = bufferedImage.getHeight();
    int w = bufferedImage.getWidth();
    

    int[][] gray = new int[w][h];
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++)
      {
        int argb = bufferedImage.getRGB(x, y);
        
        int r = (int)((argb >> 16 & 0xFF) * 1.1D + 30.0D);
        int g = (int)((argb >> 8 & 0xFF) * 1.1D + 30.0D);
        int b = (int)((argb >> 0 & 0xFF) * 1.1D + 30.0D);
        if (r >= 255) {
          r = 255;
        }
        if (g >= 255) {
          g = 255;
        }
        if (b >= 255) {
          b = 255;
        }
        gray[x][y] = ((int)Math.pow(
          Math.pow(r, 2.2D) * 0.2973D + Math.pow(g, 2.2D) * 0.6274D + Math.pow(b, 2.2D) * 0.07530000000000001D, 0.4545454545454545D));
      }
    }
    int threshold = ostu(gray, w, h);
    BufferedImage binaryBufferedImage = new BufferedImage(w, h, 12);
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++)
      {
        if (gray[x][y] > threshold) {
          gray[x][y] |= 0xFFFF;
        } else {
          gray[x][y] &= 0xFF0000;
        }
        binaryBufferedImage.setRGB(x, y, gray[x][y]);
      }
    }
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        if (isBlack(binaryBufferedImage.getRGB(x, y))) {
          System.out.print("*");
        } else {
          System.out.print(" ");
        }
      }
    }
    ImageIO.write(binaryBufferedImage, "jpg", new File(destDir, sfile.getName()));
  }
  
  public static boolean isBlack(int colorInt)
  {
    Color color = new Color(colorInt);
    if (color.getRed() + color.getGreen() + color.getBlue() <= 300) {
      return true;
    }
    return false;
  }
  
  public static boolean isWhite(int colorInt)
  {
    Color color = new Color(colorInt);
    if (color.getRed() + color.getGreen() + color.getBlue() > 300) {
      return true;
    }
    return false;
  }
  
  public static int isBlackOrWhite(int colorInt)
  {
    if ((getColorBright(colorInt) < 30) || (getColorBright(colorInt) > 730)) {
      return 1;
    }
    return 0;
  }
  
  public static int getColorBright(int colorInt)
  {
    Color color = new Color(colorInt);
    return color.getRed() + color.getGreen() + color.getBlue();
  }
  
  public static int ostu(int[][] gray, int w, int h)
  {
    int[] histData = new int[w * h];
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++)
      {
        int red = 0xFF & gray[x][y];
        histData[red] += 1;
      }
    }
    int total = w * h;
    
    float sum = 0.0F;
    for (int t = 0; t < 256; t++) {
      sum += t * histData[t];
    }
    float sumB = 0.0F;
    int wB = 0;
    int wF = 0;
    
    float varMax = 0.0F;
    int threshold = 0;
    for (int t = 0; t < 256; t++)
    {
      wB += histData[t];
      if (wB != 0)
      {
        wF = total - wB;
        if (wF == 0) {
          break;
        }
        sumB += t * histData[t];
        
        float mB = sumB / wB;
        float mF = (sum - sumB) / wF;
        

        float varBetween = wB * wF * (mB - mF) * (mB - mF);
        if (varBetween > varMax)
        {
          varMax = varBetween;
          threshold = t;
        }
      }
    }
    return threshold;
  }
}

