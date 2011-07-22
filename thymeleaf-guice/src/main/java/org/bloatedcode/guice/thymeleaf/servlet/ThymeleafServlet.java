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
		logger.info("Registering URLs ...");
		for (RequestBinding requestBinding : requestRegistrations) {
			logger.debug(" -->'{}' with template '{}' using '{}#{} as the controller.'", new String[]{requestBinding.getUrl(), requestBinding.getTemplate(), requestBinding.getClazz().getName(), requestBinding.getMethodName()});
			requestBindingMap.put(requestBinding.getUrl(),requestBinding);
		}
		logger.info("Registering URLs ... done.");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.debug("Receiving GET request to '{}'.", req.getContextPath());
		doProcess(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		logger.debug("Receiving POST request to '{}'.", req.getContextPath());
		doProcess(req, resp);
	}

	protected void doProcess(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
		try {

			String response = null;
			
			RequestBinding binding = requestBindingMap.get(req.getServletPath());
			
			if(binding == null)
				throw new ServletException("No bindings for url '" + req.getServletPath() + "'.");
	
			Object object = injector.getInstance(binding.getClazz());
			
		    Class<?> controllerClass = binding.getClazz();
		    Method method = controllerClass.getMethod(binding.getMethodName(), HttpServletRequest.class, HttpServletResponse.class);
			
		    logger.trace("Passing request to '{}#{}'.", new String[]{controllerClass.getName(),method.getName()});
			IContext context = (IContext) method.invoke(object, req ,resp);
			logger.trace("Passing context to template engine for processing.");
			response = templateEngine.process(binding.getTemplate(), context);
			logger.trace("Passing processed response to the output writer.");
			resp.getWriter().write(response);
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
