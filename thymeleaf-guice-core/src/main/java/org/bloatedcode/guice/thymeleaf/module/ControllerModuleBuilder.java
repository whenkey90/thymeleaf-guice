package org.bloatedcode.guice.thymeleaf.module;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bloatedcode.guice.thymeleaf.RequestBinding;
import org.bloatedcode.guice.thymeleaf.module.builder.AnnotationRequestMappingBuilder;
import org.bloatedcode.guice.thymeleaf.module.builder.ControllerMappingBuilder;
import org.bloatedcode.guice.thymeleaf.module.builder.RequestMappingBuilder;
import org.bloatedcode.guice.thymeleaf.module.exception.ThymeleafInitializationException;
import org.bloatedcode.guice.thymeleaf.module.reflect.MethodExtractor;
import org.bloatedcode.guice.thymeleaf.servlet.ThymeleafServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.inject.AbstractModule;

import static com.google.common.base.Preconditions.*;

public class ControllerModuleBuilder extends AbstractModule {

	private final Logger logger = LoggerFactory.getLogger(ControllerModuleBuilder.class);
	
	private ThymeleafServlet servlet;

	
	public ControllerModuleBuilder(ThymeleafServlet servlet) {
		this.servlet = servlet;
	}
	
	private MethodExtractor extractor = new MethodExtractor();

	private Set<RequestBinding> requestBindings = new LinkedHashSet<RequestBinding>();
	
	public Set<RequestBinding> getRequestBindings() {
		return requestBindings;
	}

	private Set<Method> exceptions = new HashSet<Method>();
	
	public Set<Method> getExceptions() {
		return exceptions;
	}
	
	@Override
	protected void configure() {
		logger.info("Registering URLs ...");
		for (RequestBinding binding : requestBindings) {
			Method method = binding.getMethod();
			if(exceptions.contains(method)){
				logger.debug("Ignoring method: {}",method );
				continue;
			}
			servlet.register(binding);
		}
	}

	public ControllerMappingBuilder register(Class<?> controllerClass) {
		return new ControllerMappingBuilderImpl(controllerClass);
	}
	
	class RequestMappingBuilderImpl implements RequestMappingBuilder {
		
		private Class<?> controllerClass;
		private Method method;
		
		public Class<?> getControllerClass() {
			return controllerClass;
		}
		
		public Method getMethod() {
			return method;
		}

		public RequestMappingBuilderImpl(Class<?> controllerClass, Method method) {
			this.controllerClass = controllerClass;
			this.method = method;
		}

		private String template;
		
		@Override
		public void to(String url) {
			checkArgument(!Strings.isNullOrEmpty(url),"The url must not be null or empty");
			checkState(template != null,"The template must be specified with RequestMappingBuilder#using(String)");
			
			requestBindings.add(new RequestBinding(controllerClass, method, template,url));
		}

		@Override
		public RequestMappingBuilder using(String template) {
			checkArgument(!Strings.isNullOrEmpty(template),"The template must not be null or empty");
			this.template = template;
			return this;
		}
		
	}

	class ControllerMappingBuilderImpl implements ControllerMappingBuilder {

		private Class<?> controllerClass;
		private Method method = null;
		
		public Class<?> getControllerClass() {
			return controllerClass;
		}
		
		public Method getMethod() {
			return method;
		}

		public ControllerMappingBuilderImpl(Class<?> controllerClass) {
			this.controllerClass = controllerClass;
		}

		@Override
		public RequestMappingBuilder method(String methodName) {
			method = extractor.findMethod(methodName, controllerClass);
			return new RequestMappingBuilderImpl(controllerClass,method);
		}

		@Override
		public RequestMappingBuilder method(String methodName,
				Class<?> ... parameters) {
			method = extractor.findMethod(methodName, controllerClass, parameters);
			return new RequestMappingBuilderImpl(controllerClass,method);
		}

		@Override
		public AnnotationRequestMappingBuilder byAnnotation() {
			 throw new RuntimeException("Not implemented");
		}

		@Override
		public ControllerMappingBuilder except(String methodName) {
			Method method = extractor.findMethod(methodName, controllerClass);
			if(method != null)
				exceptions.add(method);
			return this;
		}

		@Override
		public ControllerMappingBuilder except(String[] methods,
				Class<?>[] ... parameters) {
			for (int i = 0; i < methods.length; i++) {
				except(methods[i], parameters[i]);
			}
			return this;
		}

		@Override
		public ControllerMappingBuilder except(String methodName,
				Class<?> ... parameters) {
			Method method = extractor.findMethod(methodName, controllerClass, parameters);
			if(exceptions.contains(method)){
				throw new ThymeleafInitializationException(
						"The method '"+ methodName+ "' with parameters '"+ parameters.toString() + "' has already been added to exceptions.");
			}
			exceptions.add(method);
			return this;
		}

	}

	

}
