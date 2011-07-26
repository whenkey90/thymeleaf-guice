package org.bloatedcode.guice.thymeleaf.module.builder;

import org.thymeleaf.TemplateMode;
import org.thymeleaf.templateresolver.TemplateResolver;

public interface TemplateResolverParameterBuilder {

	/**
	 * Sets a new value for the cache TTL for resolved templates.
	 * 
	 * @param cache
	 * @return
	 * @see TemplateResolver#setCacheTTLMs(Long)
	 */
	TemplateResolverParameterBuilder cacheTTLms(long cache);

	/**
	 * Sets a new (optional) suffix to be added to all template names in order
	 * to convert template names into resource names.
	 * 
	 * @param suffix
	 * @return
	 * @see TemplateResolver#setSuffix(String)
	 */
	TemplateResolverParameterBuilder suffix(String suffix);

	/**
	 * Sets a new (optional) prefix to be added to all template names in order
	 * to convert template names into resource names.
	 * 
	 * 
	 * @param prefix
	 * @return
	 * @see TemplateResolver#setPrefix(String)
	 */
	TemplateResolverParameterBuilder prefix(String prefix);

	/**
	 * Sets a new character encoding for reading template resources. 
	 * @param encoding
	 * @return
	 * @see TemplateResolver#setCharacterEncoding(String)
	 */
	TemplateResolverParameterBuilder encoding(String encoding);

	/**
	 * Sets the template mode to be applied to templates resolved by this resolver.
	 * @param mode
	 * @return
	 * @see TemplateResolver#setTemplateMode(TemplateMode)
	 */
	TemplateResolverParameterBuilder mode(TemplateMode mode);

}
