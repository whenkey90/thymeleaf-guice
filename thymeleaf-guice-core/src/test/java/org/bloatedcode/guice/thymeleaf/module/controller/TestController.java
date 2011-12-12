package org.bloatedcode.guice.thymeleaf.module.controller;

import org.bloatedcode.guice.thymeleaf.module.annotation.RequestBindingHandler;

public class TestController {
	
	@RequestBindingHandler(value="page_one.html",template="template")
	public void singleMethod(){};
	
	@RequestBindingHandler(value="page_two.html",template="template")
	public void overloadedMethod(){};
	
	@RequestBindingHandler(value="page_three.html",template="template")
	public void overloadedMethod(String param){};
	
}
