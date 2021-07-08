package com.unilognew.servlet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CimmExecutorContextListener implements ServletContextListener {

	private ExecutorService executorService;

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext context = servletContextEvent.getServletContext();
		int noOfThreads = 1;

		try {
			noOfThreads = Integer.parseInt(context
					.getInitParameter("CIMM_EXECUTOR_THREAD_MAX_SIZE"));
		} catch (NumberFormatException ignore) {
			// nothing can be done
		}

		if (noOfThreads <= 1) {
			executorService = Executors.newSingleThreadExecutor();
		} else {
			executorService = Executors.newFixedThreadPool(noOfThreads);
		}
		context.setAttribute("CIMM_EXECUTOR", executorService);
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		executorService.shutdown();
	}

}