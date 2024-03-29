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

	private static final Logger logger = LoggerFactory.getLogger(ThymeleafServlet.class);

	private static final long serialVersionUID = 1L;

	private TemplateEngine templateEngine;

	private Map<String, RequestBinding> requestBindingMap = new HashMap<String, RequestBinding>();
	private Injector injector;

	public ThymeleafServlet() {
	}

	public Map<String, RequestBinding> getRequestBindingMap() {
		return requestBindingMap;
	}

	@Inject
	public void setInjector(Injector injector) {
		this.injector = injector;
	}

	@Inject
	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public void register(RequestBinding binding) {
		logger.info(" -->'{}' with template '{}' using '{}#{} as the controller.'", new String[] { binding.getUrl(), binding.getTemplate(),
				binding.getControllerClass().getName(), binding.getMethod().getName() });
		requestBindingMap.put(binding.getUrl(), binding);
	}

	public void register(Set<RequestBinding> bindings) {
		for (RequestBinding binding : bindings) {
			register(binding);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("Receiving GET request to '{}'.", req.getContextPath());
		doProcess(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("Receiving POST request to '{}'.", req.getContextPath());
		doProcess(req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("Receiving DELETE request to '{}'.", req.getContextPath());
		doProcess(req, resp);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("Receiving PUT request to '{}'.", req.getContextPath());
		doProcess(req, resp);
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("Receiving OPTIONS request to '{}'.", req.getContextPath());
		doProcess(req, resp);
	}
	
	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("Receiving HEAD request to '{}'.", req.getContextPath());
		doProcess(req, resp);
	}
	
	@Override
	protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("Receiving TRACE request to '{}'.", req.getContextPath());
		doProcess(req, resp);
	}

	protected void doProcess(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		try {

			String response = null;

			RequestBinding binding = requestBindingMap.get(req.getServletPath());

			if (binding == null)
				throw new ServletException("No bindings for url '" + req.getServletPath() + "'.");

			Object object = injector.getInstance(binding.getControllerClass());

			Method method = binding.getMethod();

			logger.trace("Passing request to '{}#{}'.", new String[] { binding.getControllerClass().getName(), method.getName() });
			IContext context = (IContext) method.invoke(object, req, resp);
			logger.trace("Passing context to template engine for processing.");
			response = templateEngine.process(binding.getTemplate(), context);
			logger.trace("Passing processed response to the output writer.");
			resp.getWriter().write(response);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
