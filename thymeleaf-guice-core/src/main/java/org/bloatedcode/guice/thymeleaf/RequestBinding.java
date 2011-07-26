package org.bloatedcode.guice.thymeleaf;

import java.lang.reflect.Method;


public class RequestBinding {

	private Method method;
	private Class<?> controllerClass;
	private String template;
	private String url;
	
	public RequestBinding(Class<?> controllerClass,Method method, String template, String url) {
		this.controllerClass = controllerClass;
		this.method = method;
		this.template = template;
		this.url = url;
	}

	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public Method getMethod() {
		return method;
	}
	
	public String getUrl() {
		return url;
	}

	public String getTemplate() {
		return template;
	}

	
	@Override
	public String toString() {
		return new StringBuilder("{")
			.append("controllerClass: <").append(controllerClass.toString()).append(">, ")
			.append("method: <").append(method.toString()).append(">, ")
			.append("template: ").append(template).append(", ")
			.append("url: ").append(url).append("}").toString();
	}
}
