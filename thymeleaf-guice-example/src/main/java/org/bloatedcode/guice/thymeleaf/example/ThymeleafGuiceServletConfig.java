package org.bloatedcode.guice.thymeleaf.example;


import org.bloatedcode.guice.thymeleaf.example.controller.AnnotatedController;
import org.bloatedcode.guice.thymeleaf.example.controller.IndexController;
import org.bloatedcode.guice.thymeleaf.module.ThymeleafModule;
import org.thymeleaf.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;


public class ThymeleafGuiceServletConfig extends GuiceServletContextListener  {
	
	@Override
	protected Injector getInjector() {
		
		final ThymeleafModule thymeleafModule = new ThymeleafModule(){
			
			@Override
			protected void configureThymeleaf() {
				// Defining the pattern of all the request that will be handled by the ThymeleafServlet
				serve("*.html");
				
				// Configure the remplate resolver
				resolveWith(ServletContextTemplateResolver.class)
					.using(TemplateMode.XHTML)
					.prefix("/WEB-INF/templates/")
					.suffix(".html")
					.cacheTTLms(3600000L);
				
				// Register specific method
				register(IndexController.class).method("handleIndex").using("index").to("/index.html");
				// Register all methods of the controller with annotation
				//register(AnnotatedController.class).byAnnotation();
				// Register all methods of the controller with annotation excepted 'AnnotatedController#doNotHandleThis'
				//register(AnnotatedController.class).except("doNotHandleThis").byAnnotation();

			}
			
		};
		
		return Guice.createInjector(thymeleafModule);
	}
	
	
}
