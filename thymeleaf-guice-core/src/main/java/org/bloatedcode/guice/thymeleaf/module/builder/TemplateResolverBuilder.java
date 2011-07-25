package org.bloatedcode.guice.thymeleaf.module.builder;

import org.thymeleaf.templateresolver.TemplateResolver;

public interface TemplateResolverBuilder {
	
	TemplateResolverBuilder resolveWith(TemplateResolver resolver);
	
}
