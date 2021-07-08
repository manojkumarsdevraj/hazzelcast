package com.unilog.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.code.appengine.awt.Color;
import com.google.code.appengine.awt.Font;
import com.google.code.appengine.awt.GradientPaint;
import com.google.code.appengine.awt.Graphics2D;
import com.google.code.appengine.awt.RenderingHints;
import com.google.code.appengine.awt.image.BufferedImage;
import com.google.code.appengine.imageio.ImageIO;

/**
 * Servlet implementation class CaptchaServletNew
 */
public class CaptchaServletNew extends HttpServlet {
	
	private static final long serialVersionUID = 3845261607933708826L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		int width = 250;
	    int height = 120;
	    SecureRandom random = new SecureRandom();
	    String temp = nextString(random);
	    temp = temp.substring(0, 6);
	    System.out.println("tets --- >"+temp);
	    char data[] = temp.toCharArray();
	    BufferedImage bufferedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2d = bufferedImage.createGraphics();
	    Font font = new Font("Chiller",Font.BOLD ,25);
	    g2d.setFont(font);
	    RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    rh.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
	    g2d.setRenderingHints(rh);
	    GradientPaint gp = new GradientPaint(0, 0,
	    Color.white, 0, height/2, Color.white, true);
	    g2d.setPaint(gp);
	    g2d.fillRect(0, 0, width, height);
	    g2d.setColor(new Color(0, 0, 0));
	    Random r = new Random();
	    // int index = Math.abs(r.nextInt()) % 5;
	    String captcha = String.copyValueOf(data);
	    request.getSession().setAttribute("captcha", captcha );
	    int x = 0;
	    int y = 0;

	 for (int i=0; i<data.length; i++) {
	        x += 20 + (Math.abs(r.nextInt()) % 25);
	        y = 45 + Math.abs(r.nextInt()) % 45;
	        System.out.println(x+":"+y);
	        g2d.drawChars(data, i, 1, x,y);
	    }
	   	    g2d.dispose();
	    response.setContentType("image/png");
	    OutputStream os = response.getOutputStream();
	    ImageIO.write(bufferedImage, "png", os);
	    os.close();
  }
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CaptchaServletNew() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException {
	      processRequest(request, response);
	  }
	  public String nextString(SecureRandom random){
	    return new BigInteger(130, random).toString(32);
	  }

}
