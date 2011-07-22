package org.bloatedcode.guice.thymeleaf.module;

import org.bloatedcode.guice.thymeleaf.module.TemplateEngineModuleBuilder.TemplateResolverParameterBuilder;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.google.inject.AbstractModule;

public class ThymeleafModule extends AbstractModule {

	private TemplateEngineModuleBuilder templateEngineBuilder;
	private ControllerModuleBuilder controllerModuleBuilder;

	@Override
	protected void configure() {
		
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
	
	protected RequestBindingBuilder serve(String url){
		return controllerModuleBuilder.serve(url);
	}
	
	public static interface TemplateResolverBuilder {
		TemplateResolverBuilder resolveWith(TemplateResolver resolver);
	}
	
	public static interface RequestBindingBuilder {
		void with(Class<?> controllerClass, String method);
		RequestBindingBuilder usingTemplate(String template);

	}

}
