package org.bloatedcode.guice.thymeleaf.module;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.thymeleaf.templateresolver.ITemplateResolver;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
public class TemplateEngineModuleBuilderTest {
	
	private ITemplateResolver customResolver = createMock(ITemplateResolver.class); 
	
	private TemplateEngineModuleBuilder moduleBuilder = null;
	
	@Before
	public void setup(){
		 moduleBuilder = new TemplateEngineModuleBuilder();
		 reset(customResolver);
	}
	
	
	@Test
	public void testCustomTemplateResolver(){
		
		replay(customResolver);
		
		moduleBuilder.resolveWith(customResolver);
		
		verify(customResolver);
		
		assertThat(moduleBuilder.getResolver(), is(equalTo(customResolver)));
		
	}

}