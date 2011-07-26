package org.bloatedcode.guice.thymeleaf.module;

import org.bloatedcode.guice.thymeleaf.module.builder.TemplateResolverParameterBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.google.inject.AbstractModule;

/**
 * Module used to configure Thymeleaf's template engine
 * @author gg250062
 *
 */
class TemplateEngineModuleBuilder extends AbstractModule {

	private ITemplateResolver resolver = null;

	@Override
	protected void configure() {

		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(resolver);

		bind(ITemplateResolver.class).toInstance(resolver);
		bind(TemplateEngine.class).toInstance(engine);

	}

	/**
	 * Configure the TemplateResolver for the TemplateEngine
	 * @param resolverClass
	 * @return
	 */
	public TemplateResolverParameterBuilder resolveWith(Class<? extends TemplateResolver> resolverClass) {
		return new TemplateResolverParameterBuilderImpl(resolverClass);
	}

	/**
	 * Use a custom, pre-configured template resolver with the template engine
	 * @param resolver
	 */
	public void resolveWith(ITemplateResolver resolver) {
		this.resolver = resolver;
	}

	class TemplateResolverParameterBuilderImpl implements
			TemplateResolverParameterBuilder {

		public TemplateResolverParameterBuilderImpl(Class<? extends TemplateResolver> resolverClass) {
			try {
				resolver = resolverClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Could not create new TemplateResolver object.", e);
			}
		}
		
		private TemplateResolver resolver(){
			return (TemplateResolver) resolver;
		}

		@Override
		public TemplateResolverParameterBuilder mode(TemplateMode mode) {
			resolver().setTemplateMode(mode);
			return this;
		}

		@Override
		public TemplateResolverParameterBuilder prefix(String prefix) {
			resolver().setPrefix(prefix);
			return this;
		}

		@Override
		public TemplateResolverParameterBuilder suffix(String suffix) {
			resolver().setSuffix(suffix);
			return this;
		}

		@Override
		public TemplateResolverParameterBuilder encoding(String encoding) {
			resolver().setCharacterEncoding(encoding);
			return this;
		}

		@Override
		public TemplateResolverParameterBuilder cacheTTLms(long cache) {
			resolver().setCacheTTLMs(cache);
			return this;
		}

	}
}