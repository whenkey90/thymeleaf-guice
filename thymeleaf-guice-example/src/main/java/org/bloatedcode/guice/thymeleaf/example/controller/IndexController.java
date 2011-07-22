package org.bloatedcode.guice.thymeleaf.example.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;

@Singleton
public class IndexController {
	
	@Inject
	public IndexController() {}

	public IWebContext handleIndex(HttpServletRequest request, HttpServletResponse response){
		WebContext context = new WebContext(request, request.getLocale());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy",request.getLocale());
		
		context.setVariable("today", simpleDateFormat.format(new Date()));
		return context;
	}
	
	
}
