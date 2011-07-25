package org.bloatedcode.guice.thymeleaf.module;

import org.bloatedcode.guice.thymeleaf.module.builder.TemplateResolverParameterBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateMode;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;

class TemplateEngineModuleBuilder extends AbstractModule {

	private Class<? extends TemplateResolver> resolverClass;
	private TemplateMode mode = null;
	private String prefix = null;
	private String suffix = null;
	private Long cache = null;

	@Override
	protected void configure() {
		Preconditions.checkNotNull(resolverClass,
				"TemplateResolver class must be specified.");
		try {
			TemplateResolver resolver = resolverClass.newInstance();;

			if (mode != null) {
				resolver.setTemplateMode(mode);
			}

			if (prefix != null) {
				resolver.setPrefix(prefix);
			}

			if (suffix != null) {
				resolver.setSuffix(suffix);
			}

			if (cache != null) {
				resolver.setCacheTTLMs(cache);
			}
			
			TemplateEngine engine = new TemplateEngine();
			engine.setTemplateResolver(resolver);
			
			bind(TemplateResolver.class).toInstance(resolver);
			bind(TemplateEngine.class).toInstance(engine);
			
		} catch (Exception e) {
			throw new RuntimeException("Could not create new TemplateResolver object.",e);
		}

	}

	public TemplateResolverParameterBuilder resolveWith(
			Class<? extends TemplateResolver> resolver) {
		this.resolverClass = resolver;
		return new TemplateResolverParameterBuilderImpl();
	}

	class TemplateResolverParameterBuilderImpl implements
			TemplateResolverParameterBuilder {

		@Override
		public TemplateResolverParameterBuilder using(TemplateMode mode) {
			TemplateEngineModuleBuilder.this.mode = mode;
			return this;
		}

		@Override
		public TemplateResolverParameterBuilder prefix(String prefix) {
			TemplateEngineModuleBuilder.this.prefix = prefix;
			return this;
		}

		@Override
		public TemplateResolverParameterBuilder suffix(String suffix) {
			TemplateEngineModuleBuilder.this.suffix = suffix;
			return this;
		}

		@Override
		public TemplateResolverParameterBuilder cacheTTLms(long cache) {
			TemplateEngineModuleBuilder.this.cache = cache;
			return this;
		}

	}

}
