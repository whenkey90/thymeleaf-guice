package org.bloatedcode.guice.thymeleaf.example;


import org.bloatedcode.guice.thymeleaf.example.controller.IndexController;
import org.bloatedcode.guice.thymeleaf.module.ThymeleafModule;
import org.bloatedcode.guice.thymeleaf.servlet.ThymeleafServlet;
import org.thymeleaf.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class ThymeleafGuiceServletConfig extends GuiceServletContextListener  {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule(){
			@Override
			protected void configureServlets() {
				serve("*.html").with(ThymeleafServlet.class);
			}
		}, new ThymeleafModule(){
			
			@Override
			protected void configureThymeleaf() {
				resolveWith(ServletContextTemplateResolver.class)
					.using(TemplateMode.XHTML)
					.prefix("/WEB-INF/templates/")
					.suffix(".html")
					.cacheTTLms(3600000L);
				
				serve("/index.html").usingTemplate("index").with(IndexController.class,"handleIndex");
			}
		});
	}
}
