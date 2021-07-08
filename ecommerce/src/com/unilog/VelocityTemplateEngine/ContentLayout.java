package com.unilog.VelocityTemplateEngine;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;

public class ContentLayout {

	public static void generateBodyContent(VelocityContext context, LinkedHashMap<String, Object> contentObject) {

		try {

			if (contentObject != null && contentObject.size() > 0) {
				for (Map.Entry<String, Object> entry : contentObject.entrySet()) {
					context.put(entry.getKey(), entry.getValue());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
