package org.bloatedcode.guice.thymeleaf.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bloatedcode.guice.thymeleaf.RequestBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;

import com.google.inject.Injector;

@Singleton
public class ThymeleafServlet extends HttpServlet {

	private static final Logger logger = LoggerFactory
			.getLogger(ThymeleafServlet.class);

	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;

	private Map<String,RequestBinding> requestBindingMap = new HashMap<String, RequestBinding>();
	private Injector injector;

	@Inject
	public ThymeleafServlet(Injector injector,TemplateEngine templateEngine,Set<RequestBinding> requestRegistrations) {
		this.templateEngine = templateEngine;
		this.injector = injector;
		for (RequestBinding requestBinding : requestRegistrations) {
			logger.debug("Registering URL '{}' with template '{}' using '{}#{} as the controller.'", new String[]{requestBinding.getUrl(), requestBinding.getTemplate(), requestBinding.getClazz().getName(), requestBinding.getMethodName()});
			requestBindingMap.put(requestBinding.getUrl(),requestBinding);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doProcess(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doProcess(req, resp);
	}

	protected void doProcess(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
		try {
			logger.debug("Context Path: {}", req.getContextPath());
			logger.debug("Servlet Path: {}", req.getServletPath());

			String response = null;
			
			RequestBinding binding = requestBindingMap.get(req.getServletPath());
			
			if(binding == null)
				throw new ServletException("No bindings for url '" + req.getServletPath() + "'");
	
			Object object = injector.getInstance(binding.getClazz());
			
		    Class<?> controllerClass = binding.getClazz();
		    Method method = controllerClass.getMethod(binding.getMethodName(), HttpServletRequest.class, HttpServletResponse.class);
			
			IContext context = (IContext) method.invoke(object, req ,resp);

			response = templateEngine.process(binding.getTemplate(), context);
			
			resp.setContentType("text/html;charset=UTF-8");
			resp.setHeader("Pragma", "no-cache");
			resp.setHeader("Cache-Control", "no-cache");
			resp.setDateHeader("Expires", 0);

			resp.getWriter().write(response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
