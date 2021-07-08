package com.unilog.VelocityTemplateEngine;

import org.apache.velocity.VelocityContext;

import com.unilog.products.HeaderAndMenuBuilder;

public class HeaderLayout {
	public static void generateHeaderContent(VelocityContext context) {

		try {
			context.put("topStaticMenu", HeaderAndMenuBuilder.topStaticMenu());
			context.put("topStaticMenuThirdLevel", HeaderAndMenuBuilder.topStaticMenuThirdLevel());
			context.put("topStaticMenuFourthLevel", HeaderAndMenuBuilder.topStaticMenuFourthLevel());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
