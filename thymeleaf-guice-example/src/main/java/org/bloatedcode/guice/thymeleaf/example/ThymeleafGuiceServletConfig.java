package org.bloatedcode.guice.thymeleaf.example;


import org.bloatedcode.guice.thymeleaf.example.controller.IndexController;
import org.bloatedcode.guice.thymeleaf.module.ThymeleafModule;
import org.bloatedcode.guice.thymeleaf.servlet.ThymeleafServlet;
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
				
				serve("*.html").with(ThymeleafServlet.class);
				
				resolveWith(ServletContextTemplateResolver.class)
					.using(TemplateMode.XHTML)
					.prefix("/WEB-INF/templates/")
					.suffix(".html")
					.cacheTTLms(3600000L);
				
				register("/index.html").usingTemplate("index").with(IndexController.class,"handleIndex");
			}
			
		};
		
		return Guice.createInjector(thymeleafModule);
	}
}
