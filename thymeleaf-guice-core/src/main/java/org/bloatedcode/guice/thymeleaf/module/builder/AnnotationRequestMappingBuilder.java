package org.bloatedcode.guice.thymeleaf.module.builder;

public interface AnnotationRequestMappingBuilder {
	
	void except(String method, String ... otherMethods);

}
