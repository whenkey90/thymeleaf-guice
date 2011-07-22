package org.bloatedcode.guice.thymeleaf.module;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import org.bloatedcode.guice.thymeleaf.RequestBinding;
import org.bloatedcode.guice.thymeleaf.module.ThymeleafModule.RequestBindingBuilder;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class ControllerModuleBuilder extends AbstractModule {
	
	public ControllerModuleBuilder() {}

	private Set<RequestBinding> requestBindings = new HashSet<RequestBinding>();

	@Override
	protected void configure() {

		Multibinder<RequestBinding> multibinder = Multibinder.newSetBinder(
				binder(), RequestBinding.class);

		Set<String> bindedClasses = new HashSet<String>();
		for (RequestBinding binding : requestBindings) {
			if (!bindedClasses.contains(binding.getClazz().getName())) {
				bind(binding.getClazz()).in(Singleton.class);
			}
			multibinder.addBinding().toInstance(binding);
		}
	}

	public RequestBindingBuilder serve(String url) {
		return new RequestBindingBuilderImpl(url);
	}

	class RequestBindingBuilderImpl implements RequestBindingBuilder {

		private String template;
		private String url;

		public RequestBindingBuilderImpl(String url) {
			this.url = url;
		}

		@Override
		public void with(Class<?> controllerClass, String method) {
			requestBindings.add(new RequestBinding(controllerClass,
					template, method, url));
		}

		@Override
		public RequestBindingBuilder usingTemplate(String template) {
			this.template = template;
			return this;
		}

	}
}
