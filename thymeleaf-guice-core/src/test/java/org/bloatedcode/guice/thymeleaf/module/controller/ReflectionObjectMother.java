package org.bloatedcode.guice.thymeleaf.module.controller;

import java.lang.reflect.Method;

import org.bloatedcode.guice.thymeleaf.RequestBinding;

public class ReflectionObjectMother {

	private Class<?> controllerClass = TestController.class;
	
	public Method newSimpleMethod() throws Exception{
		return controllerClass.getMethod("singleMethod");
	}
	
	public Method newOverridedMethodNoArgument() throws Exception {
		return controllerClass.getMethod("overloadedMethod");
	}
	
	public Method newOverridedMethodOneArgument() throws Exception {
		return controllerClass.getMethod("overloadedMethod", String.class);
	}
	
	public RequestBinding newSimpleMethodBinding(String template,String url) throws Exception{
		return new RequestBinding(controllerClass, newSimpleMethod(), template, url);
	}
	
	public RequestBinding newOverridedMethodNoArgumentBinding(String template,String url) throws Exception{
		return new RequestBinding(controllerClass, newOverridedMethodNoArgument(), template, url);
	}	
	
	public RequestBinding newOverridedMethodOneArgumentBinding(String template,String url) throws Exception{
		return new RequestBinding(controllerClass, newOverridedMethodOneArgument(), template, url);
	}
}
