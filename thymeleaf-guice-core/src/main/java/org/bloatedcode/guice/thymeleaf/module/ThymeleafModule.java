package org.bloatedcode.guice.thymeleaf.module;


import org.bloatedcode.guice.thymeleaf.module.builder.ControllerMappingBuilder;
import org.bloatedcode.guice.thymeleaf.module.builder.TemplateResolverParameterBuilder;
import org.bloatedcode.guice.thymeleaf.servlet.ThymeleafServlet;
import org.thymeleaf.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletModule;

public class ThymeleafModule extends AbstractModule {

	private TemplateEngineModuleBuilder templateEngineBuilder;
	private ControllerModuleBuilder controllerModuleBuilder;
	private ServletModule servletModule;

	
	private ThymeleafServlet servlet;
	private String urlPattern;
	private String[] morePatterns;
	
	@Override
	protected final void configure() {
		
		servlet = new ThymeleafServlet();
		

		
		bind(ThymeleafServlet.class).toInstance(servlet);

		templateEngineBuilder = new TemplateEngineModuleBuilder();
		controllerModuleBuilder = new ControllerModuleBuilder(servlet);
		
		servletModule = new ServletModule() {
			@Override
			protected void configureServlets() {
				Preconditions.checkState(urlPattern != null, "There must be at least one pattern configured with 'ThymeleafModule#serve'");
				serve(urlPattern, morePatterns).with(servlet);
			}
		};

		try {
			
			configureThymeleaf();

			install(servletModule);
			install(controllerModuleBuilder);
			install(templateEngineBuilder);

		} finally {

			controllerModuleBuilder = null;
			templateEngineBuilder = null;

		}

	}

	protected void configureThymeleaf() {

	}

	protected void serve(final String urlPattern, final String ...morePatterns ) {
		Preconditions.checkNotNull(urlPattern,"The urlPattern must not be null.");
		Preconditions.checkState(this.urlPattern == null, "The url patterns has already been configured.");
		
		this.urlPattern = urlPattern;
		this.morePatterns = morePatterns;
		
	}

	protected TemplateResolverParameterBuilder resolveWith(Class<? extends TemplateResolver> resolver) {
		return templateEngineBuilder.resolveWith(resolver);
	}
	
	protected void resolveWith(ITemplateResolver resolver){
		templateEngineBuilder.resolveWith(resolver);
	}

	protected ControllerMappingBuilder register(Class<?> controllerClass) {
		return controllerModuleBuilder.register(controllerClass);
	}

}
