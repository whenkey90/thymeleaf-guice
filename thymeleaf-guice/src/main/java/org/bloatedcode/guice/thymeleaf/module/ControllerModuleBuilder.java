package org.bloatedcode.guice.thymeleaf.module;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import org.bloatedcode.guice.thymeleaf.RequestBinding;
import org.bloatedcode.guice.thymeleaf.module.builder.AnnotationRequestMappingBuilder;
import org.bloatedcode.guice.thymeleaf.module.builder.ControllerMappingBuilder;
import org.bloatedcode.guice.thymeleaf.module.builder.RequestMappingBuilder;
import org.bloatedcode.guice.thymeleaf.module.exception.ThymeleafInitializationException;
import org.bloatedcode.guice.thymeleaf.module.reflect.MethodExtractor;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class ControllerModuleBuilder extends AbstractModule {

	public ControllerModuleBuilder() {
	}
	
	private MethodExtractor extractor = new MethodExtractor();

	private Set<RequestBinding> requestBindings = new HashSet<RequestBinding>();

	private Set<Method> exceptions = new HashSet<Method>();
	
	public Set<Method> getExceptions() {
		return exceptions;
	}
	
	@Override
	protected void configure() {

		Multibinder<RequestBinding> multibinder = Multibinder.newSetBinder(
				binder(), RequestBinding.class);

		Set<String> bindedClasses = new HashSet<String>();
		for (RequestBinding binding : requestBindings) {
			if(exceptions.contains(binding.getMethod()))
			if (!bindedClasses.contains(binding.getControllerClass().getName())) {
				bind(binding.getControllerClass()).in(Singleton.class);
			}
			multibinder.addBinding().toInstance(binding);
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
			Preconditions.checkArgument(!Strings.isNullOrEmpty(url),"The url must not be null or empty");
			Preconditions.checkState(template != null,"The template must be specified with RequestMappingBuilder#using(String)");
			
			requestBindings.add(new RequestBinding(controllerClass, method, template,url));
		}

		@Override
		public RequestMappingBuilder using(String template) {
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

		public RequestMappingBuilder method(String methodName,
				Class<?>[] parameters) {
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
				Class<?>[][] parameters) {
			for (int i = 0; i < methods.length; i++) {
				except(methods[i], parameters[i]);
			}
			return this;
		}

		@Override
		public ControllerMappingBuilder except(String methodName,
				Class<?>[] parameters) {
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
