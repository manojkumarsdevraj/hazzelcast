package com.unilog.products;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class XSSFilters implements Filter {
	
	private static String UrlString;
	private static String RequrlString;

	

	public static String getUrlString() {
		return UrlString;
	}

	public void setUrlString(String urlString) {
		XSSFilters.UrlString = urlString;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	    }

	    public void destroy() {
	    }

	   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
		     	chain.doFilter(new XSSRequestWrappers((HttpServletRequest) request), response);
	    }

	public static String getRequrlString() {
		return RequrlString;
	}

	public void setRequrlString(String requrlString) {
		XSSFilters.RequrlString = requrlString;
	}

}
