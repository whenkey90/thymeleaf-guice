package org.bloatedcode.guice.thymeleaf.module.builder;

import org.thymeleaf.TemplateMode;

public interface TemplateResolverParameterBuilder {
	
	TemplateResolverParameterBuilder using(TemplateMode mode);

	TemplateResolverParameterBuilder cacheTTLms(long cache);

	TemplateResolverParameterBuilder suffix(String suffix);

	TemplateResolverParameterBuilder prefix(String prefix);
	
}
