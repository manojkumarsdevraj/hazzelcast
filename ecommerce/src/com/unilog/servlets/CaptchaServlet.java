package com.unilog.servlets;



import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.math.BigInteger;
import java.security.SecureRandom;

import com.google.code.appengine.imageio.ImageIO;
import com.unilog.utility.CommonUtility;
import com.google.code.appengine.awt.AlphaComposite;
import com.google.code.appengine.awt.BasicStroke;
import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Font;
import com.google.code.appengine.awt.GradientPaint;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.RenderingHints;
import com.google.code.appengine.awt.geom.AffineTransform;
import com.google.code.appengine.awt.image.BufferedImage;


public class CaptchaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Random rand = new Random(System.currentTimeMillis());
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		int width = 250;
	    int height = 120;
		Color background = Color.DARK_GRAY;
		int complexity = 4;
		int size = 30;
		 
	
	    SecureRandom random = new SecureRandom();
	    String temp = nextString(random);
	    temp = temp.substring(0, 6);
	    char data[] = temp.toCharArray();
	    BufferedImage bufferedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2d = bufferedImage.createGraphics();
	    g2d = (Graphics2D)bufferedImage.getGraphics();
	    Font font = new Font("Courier",Font.BOLD ,size);
	    g2d.setFont(font);
	    RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
	    g2d.setRenderingHints(rh);
	    GradientPaint gp = new GradientPaint(0, 0,
	    Color.white, 0, height, Color.LIGHT_GRAY, true);
	    g2d.setPaint(gp);
	    g2d.fillRect(0, 0, width, height);
	    g2d.setColor(background);
	    Random r = new Random();
	   
	    String captcha = String.copyValueOf(data);
	    System.out.println("typ ---> "+request.getParameter("typ"));
	    if(CommonUtility.validateString(request.getParameter("typ")).equalsIgnoreCase("eauth")) {
	    	request.getSession().setAttribute("eauthCaptcha", captcha );
	    }else {
	    	request.getSession().setAttribute("captcha", captcha );
	    }
	    
	    int x = 0;
	    int y = 0;

	 for (int i=0; i<data.length; i++) {
	        x += 20 + (Math.abs(r.nextInt()) % 25);
	        y =55 + Math.abs(r.nextInt()) % 45;
	        System.out.println(x+":"+y);
	        g2d.drawChars(data, i, 1, x,y);
	    }
	 
	 // copy to buffer2 and use cos/sin to distort
        BufferedImage buffer2 = new BufferedImage(
        		bufferedImage.getWidth(), 
        		bufferedImage.getHeight(), 
        		bufferedImage.getType());
        Graphics2D g2d2 = (Graphics2D)buffer2.getGraphics();
        g2d2.drawImage(bufferedImage, 0, 0, null);
        double seed = rand.nextDouble() * 3d + 5d;
        
        for (int xX = 0; xX < bufferedImage.getWidth(); xX++) {
            for (int yY = 0; yY < bufferedImage.getHeight(); yY++) {
                int xx = xX + (int)(Math.cos((double)yY/seed) * ((double)complexity/2d));
                int yy = yY + (int)(Math.sin((double)xX/(seed+1)) * ((double)complexity/2d));
                xx = Math.abs(xx % bufferedImage.getWidth());
                yy = Math.abs(yy % bufferedImage.getHeight());
                bufferedImage.setRGB(xX, yY, buffer2.getRGB(xx, yy));
            }
        }
        
        // draw lines
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2d.setTransform(AffineTransform.getRotateInstance(rand.nextDouble() * 0.3d - 0.15d));
        g2d.setColor(new Color(128, 128, 128, 128));
        for (int xXX = -100; xXX < bufferedImage.getWidth() + 100; xXX=xXX+(rand.nextInt(9)+6)) {
            g2d.setStroke(new BasicStroke(
                    1, 
                    BasicStroke.CAP_SQUARE, 
                    BasicStroke.CAP_SQUARE, 
                    10, new float[] {rand.nextInt(10)+2, rand.nextInt(4)+2}, 
                    0));
            g2d.drawLine(xXX, -100, xXX, bufferedImage.getHeight() + 100);
        }
        g2d.setColor(new Color(188, 188, 128, 64));
        for (int yYY = -100; yYY < bufferedImage.getHeight() + 100; yYY=yYY+(rand.nextInt(8)+7)) {
            g2d.setStroke(new BasicStroke(
                    1, 
                    BasicStroke.CAP_SQUARE, 
                    BasicStroke.CAP_SQUARE, 
                    10, new float[] {rand.nextInt(10)+2, rand.nextInt(3)+2}, 
                    0));
            g2d.drawLine(-100, yYY , bufferedImage.getWidth() + 100, yYY );
        }	
	   	    g2d.dispose();
	    response.setContentType("image/png");
	    OutputStream os = response.getOutputStream();
	    ImageIO.write(bufferedImage, "png", os);
	    os.close();
  }
  protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
      processRequest(request, response);
  }
  protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
      processRequest(request, response);
  }
  public String nextString(SecureRandom random){
    return new BigInteger(130, random).toString(32);
  }

}
