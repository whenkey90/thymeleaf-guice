package org.bloatedcode.guice.thymeleaf.module;

import org.bloatedcode.guice.thymeleaf.module.TemplateEngineModuleBuilder.TemplateResolverParameterBuilder;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.google.inject.servlet.ServletModule;

public class ThymeleafModule extends ServletModule {

	private TemplateEngineModuleBuilder templateEngineBuilder;
	private ControllerModuleBuilder controllerModuleBuilder;

	@Override
	protected void configureServlets() {
		
	    templateEngineBuilder = new TemplateEngineModuleBuilder();
	    controllerModuleBuilder = new ControllerModuleBuilder();

	    
	    try {
	    	
	    	configureThymeleaf();
	        
	    	install(controllerModuleBuilder);
	    	install(templateEngineBuilder);

	    }finally {
	    	
	    	controllerModuleBuilder = null;
	    	templateEngineBuilder = null;
	    	
	    }

	}
	
	protected void configureThymeleaf(){
		
	}
	
	
	protected TemplateResolverParameterBuilder resolveWith(Class<? extends TemplateResolver> resolver){
		return templateEngineBuilder.resolveWith(resolver);
	}
	
	protected RequestBindingBuilder register(String url){
		return controllerModuleBuilder.register(url);
	}
	
	public static interface TemplateResolverBuilder {
		TemplateResolverBuilder resolveWith(TemplateResolver resolver);
	}
	
	public static interface RequestBindingBuilder {
		void with(Class<?> controllerClass, String method);
		RequestBindingBuilder usingTemplate(String template);

	}

}
