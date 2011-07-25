package org.bloatedcode.guice.thymeleaf.module.builder;

public interface RequestMappingBuilder {
	
	void to(String url);
	RequestMappingBuilder using(String template);

}
