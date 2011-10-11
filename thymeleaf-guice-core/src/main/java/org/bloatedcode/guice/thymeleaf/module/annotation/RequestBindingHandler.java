package org.bloatedcode.guice.thymeleaf.module.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation used to declare a method that will be handling a request. Currently, the signature of the method must be
 * <code>IWebContext myMethod(HttpServletRequest,HttpServletResponse)</code>
 * @author gg250062
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestBindingHandler {

	/**
	 * The url to map, without the prefix and the suffix declared in Thymeleaf's module declaration
	 * @return
	 */
	String value();
	/**
	 * The template to use with the request
	 * @return
	 */
	String template();
	
}
