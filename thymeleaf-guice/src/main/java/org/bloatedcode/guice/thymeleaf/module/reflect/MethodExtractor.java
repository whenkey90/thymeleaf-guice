package org.bloatedcode.guice.thymeleaf.module.reflect;

import java.lang.reflect.Method;

import org.bloatedcode.guice.thymeleaf.module.exception.ThymeleafInitializationException;

public class MethodExtractor {

	public Method findMethod(String name,Class<?> clazz){
		Method foundMethod = null;
		for (Method method : clazz.getMethods()) {
			if ( method.getName().equals(name) && foundMethod != null) {
				throw new ThymeleafInitializationException(
						"More than one method with name '"
								+ name
								+ "', please specify the parameters.");
			}
			
			if(method.getName().equals(name)){
				foundMethod = method;
			}
		}

		if (foundMethod == null) {
			throw new ThymeleafInitializationException(
					"No method with name '" + name + "'");
		}
		
		return foundMethod;
	}
	
	
	public Method findMethod(String name,Class<?> clazz, Class<?> ... parameters){
		Method findMethod = null;
		try {			
			findMethod = clazz.getMethod(name,parameters);
		} catch (NoSuchMethodException e) {
			throw new ThymeleafInitializationException(
					"No method with name '" + name
							+ "' and arguments '" + parameters.toString()
							+ "'", e);
		}
		return findMethod;
	}
}
