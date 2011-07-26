package org.bloatedcode.guice.thymeleaf.module.builder;

public interface RequestMappingBuilder {
	
	/**
	 * The mapped url
	 * @param url
	 */
	void to(String url);
	
	/**
	 * The name of the template to use with the mappped url
	 * @param template
	 * @return
	 */
	RequestMappingBuilder using(String template);

}
