package org.bloatedcode.guice.thymeleaf;


public class RequestBinding {

	private String methodName;
	private Class<?> clazz;
	private String template;
	private String url;
	
	public RequestBinding(Class<?> controllerClass, String template, String methodName, String url) {
		this.clazz = controllerClass;
		this.template = template;
		this.methodName = methodName;
		this.url = url;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getUrl() {
		return url;
	}

	public String getTemplate() {
		return template;
	}

	public String getMethodName() {
		return methodName;
	}

}
