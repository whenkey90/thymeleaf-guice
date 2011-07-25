package org.bloatedcode.guice.thymeleaf.module.builder;

import org.bloatedcode.guice.thymeleaf.module.exception.ThymeleafInitializationException;


public interface ControllerMappingBuilder {
	
	RequestMappingBuilder method(String method);
	
	AnnotationRequestMappingBuilder byAnnotation();

	
	/**
	 * Prevents the methods specified to be registered, even if they are annotated
	 * @param method
	 * @param parameters
	 * @return
	 * 
	 * @throws ThymeleafInitializationException if the method doesn't exists, 
	 * or the method has already been marked as ignored
	 */
	ControllerMappingBuilder except(String method, Class<?>[] parameters);

	/**
	 * Search for one method with the specified name, and prevents is to be registered,
	 * even if they are annotated
	 * @param methods
	 * @param parameters
	 * @return
	 * @throws ThymeleafInitializationException one of the method doesn't exists, 
	 * or one of the method has already been marked as ignored
	 */
	ControllerMappingBuilder except(String[] methods, Class<?>[][] parameters);

	/**
	 * Prevents the method specified to be registered, even if it is annotated.
	 * @param method
	 * @return
	 * 
	 * @throws ThymeleafInitializationException if more than one method with the same name, 
	 * the method doesn't exists, or the method has already been marked as ignored
     *
	 */
	ControllerMappingBuilder except(String method);
	
}
