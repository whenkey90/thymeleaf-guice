package org.bloatedcode.guice.thymeleaf.example.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bloatedcode.guice.thymeleaf.module.annotation.RequestBindingHandler;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;

@Singleton
public class AnnotatedController {

	@RequestBindingHandler(value="/annotated.html",template="annotated")
	public IWebContext handleAnnotated(HttpServletRequest request, HttpServletResponse response){
		WebContext context = new WebContext(request, request.getLocale());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy",request.getLocale());
		
		context.setVariable("today", simpleDateFormat.format(new Date()));
		return context;
	}
	
}
