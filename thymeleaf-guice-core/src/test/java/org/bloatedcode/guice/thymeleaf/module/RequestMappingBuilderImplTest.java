package org.bloatedcode.guice.thymeleaf.module;

import java.lang.reflect.Method;

import org.bloatedcode.guice.thymeleaf.RequestBinding;
import org.bloatedcode.guice.thymeleaf.module.ControllerModuleBuilder.RequestMappingBuilderImpl;
import org.bloatedcode.guice.thymeleaf.module.controller.TestController;
import org.bloatedcode.guice.thymeleaf.servlet.ThymeleafServlet;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class RequestMappingBuilderImplTest {

	private Class<?> controllerClass = TestController.class;
	
	private ThymeleafServlet servlet = EasyMock.createMock(ThymeleafServlet.class);
	
	private RequestMappingBuilderImpl builder;
	private ControllerModuleBuilderTester moduleBuilder = new ControllerModuleBuilderTester(servlet);
	
	@Before
	public void setup() throws Exception{
		
	}
	
	@Test
	public void testBindUniqueExistingMethod() throws Exception{
		Method singleMethod = controllerClass.getMethod("singleMethod");
		
		builder = moduleBuilder.newBuilder(controllerClass, singleMethod);
		
		builder.using("template").to("/index.html");
		
		RequestBinding expected = new RequestBinding(controllerClass, singleMethod, "template", "/index.html");
		
		
		assertThat(moduleBuilder.getRequestBindings(), hasSize(1));
		assertThat(moduleBuilder.getRequestBindings(), contains(samePropertyValuesAs(expected)));
	}
	
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testBindOverloadedMethod() throws Exception {
		Method overloadedMethod = controllerClass.getMethod("overloadedMethod", String.class);
		
		builder = moduleBuilder.newBuilder(controllerClass, overloadedMethod);
		
		builder.using("template").to("/index.html");
		
		RequestBinding expected = new RequestBinding(controllerClass, overloadedMethod, "template", "/index.html");
		
		assertThat(moduleBuilder.getRequestBindings(), hasSize(1));
		assertThat(moduleBuilder.getRequestBindings(), contains(samePropertyValuesAs(expected)));
		
		assertThat(builder.getMethod(), is(overloadedMethod));
		assertThat((Class) builder.getControllerClass(), equalTo((Class) controllerClass));
	}
	
	@Test(expected=IllegalStateException.class)
	public void testBindWithoutTemplate() throws Exception {
		Method overloadedMethod = controllerClass.getMethod("overloadedMethod", String.class);
		
		builder = moduleBuilder.newBuilder(controllerClass, overloadedMethod);
		
		builder.to("/index.html");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBindWithEmptyTemplate() throws Exception {
	Method overloadedMethod = controllerClass.getMethod("overloadedMethod", String.class);
		
		builder = moduleBuilder.newBuilder(controllerClass, overloadedMethod);
		
		builder.using("").to("/index.html");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBindWithEmptyURL() throws Exception {
		Method overloadedMethod = controllerClass.getMethod("overloadedMethod", String.class);
		
		builder = moduleBuilder.newBuilder(controllerClass, overloadedMethod);
		
		builder.using("index").to("");
	}
	
	private static class ControllerModuleBuilderTester extends ControllerModuleBuilder {
		public ControllerModuleBuilderTester(ThymeleafServlet servlet) {
			super(servlet);
		}
		
		RequestMappingBuilderImpl newBuilder(Class<?> controllerClass, Method method){
			return new RequestMappingBuilderImpl(controllerClass, method);
		}
	}
}
