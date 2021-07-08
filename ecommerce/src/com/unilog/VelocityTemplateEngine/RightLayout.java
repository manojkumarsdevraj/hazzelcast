package com.unilog.VelocityTemplateEngine;

import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class RightLayout {
	public static String generateRightContent(String rightLayout, LinkedHashMap<String, Object> rightContentObject) {
		String rightContent = "";
		try {
			VelocityContext context = new VelocityContext();
			if (rightContentObject != null && rightContentObject.size() > 0) {
				for (Map.Entry<String, Object> entry : rightContentObject.entrySet()) {
					context.put(entry.getKey(), entry.getValue());
				}
			}

			context.put("", "");

			StringWriter writer = new StringWriter();
			Velocity.evaluate(context, writer, "", rightLayout);
			rightContent = writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rightContent;
	}
}
