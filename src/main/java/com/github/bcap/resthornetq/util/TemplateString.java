package com.github.bcap.resthornetq.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TemplateString {
	
	private String template;
	private List<Pattern> patterns;
	
	public TemplateString(String template) {
		this.template = template;
		this.patterns = createPatternList(this.template);
	}
	
	private List<Pattern> createPatternList(String template) {
		List<Pattern> result = new ArrayList<Pattern>();
		int i = 0;
		String placeholder = "${" + i + "}";
		while(template.contains(placeholder)) {
			result.add(Pattern.compile("\\$\\{" + i + "\\}"));
			placeholder = "${" + ++i + "}";
		}
		return result;
	}

	public String apply(String... args) {
		String result = this.template;
		if(args != null) {
			if(args.length <= patterns.size()) {
				for (int i = 0; i < args.length; i++) {
					result = patterns.get(i).matcher(result).replaceAll(args[i]);
				}
			} else {
				throw new IllegalArgumentException("Cannot apply " + args.length + " arguments to a template with " + patterns.size() + " argument placeholders");
			}
		}
		return result;
	}

}
