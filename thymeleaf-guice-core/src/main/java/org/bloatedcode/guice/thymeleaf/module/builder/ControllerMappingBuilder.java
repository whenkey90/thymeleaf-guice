package org.bloatedcode.guice.thymeleaf.module.builder;

import org.bloatedcode.guice.thymeleaf.module.exception.ThymeleafInitializationException;


public interface ControllerMappingBuilder {
	
	/**
	 * Detects a method of the controller class with the specified name and registers
 	 * it to be used  as a handler for a request.
	 * @param method
	 * @return
	 * @throws ThymeleafInitializationException if no method with the name exists.
	 */
	RequestMappingBuilder method(String name);

	/**
	 * Registers a method of the controller class with the specified name and arguments 
	 * to be used as a handler for a request.
	 * @param method
	 * @param parameters
	 * @return
	 * @throws ThymeleafInitializationException if no method with the name exists or
	 * if the parameters are incorrect.
	 */
	RequestMappingBuilder method(String method, Class<?> ... parameters);
	
	/**
	 * Detects all methods annotated with the RequestBindingHandler annotation and
	 * register them to be used as handlers for requests.
	 * @return
	 */
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
	ControllerMappingBuilder except(String method, Class<?> ... parameters);

	/**
	 * Search for one method with the specified name, and prevents is to be registered,
	 * even if they are annotated
	 * @param methods
	 * @param parameters
	 * @return
	 * @throws ThymeleafInitializationException one of the method doesn't exists, 
	 * or one of the method has already been marked as ignored
	 */
	ControllerMappingBuilder except(String[] methods, Class<?>[] ... parameters);

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
