package com.unilog.VelocityTemplateEngine;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;

import com.unilog.products.HeaderAndMenuBuilder;

public class LeftLayout {
	public static void generateLeftContent(VelocityContext context, LinkedHashMap<String, Object> leftContentObject) {

		try {
			if (leftContentObject == null) {
				leftContentObject = new LinkedHashMap<String, Object>();
				leftContentObject.put("leftCategoryList", HeaderAndMenuBuilder.getTopCategories());
			} else {
				leftContentObject.put("leftCategoryList", HeaderAndMenuBuilder.getTopCategories());
			}

			if (leftContentObject != null && leftContentObject.size() > 0) {
				for (Map.Entry<String, Object> entry : leftContentObject.entrySet()) {
					context.put(entry.getKey(), entry.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
