package com.unilog.utility;

import com.unilog.database.CommonDBQuery;

public class ThemeGeneratorScheduledCheck implements Runnable 
{
	public void run() {
		try {
			//CommonDBQuery.setThemeDate(null);
			CommonDBQuery.checkForTheme();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}