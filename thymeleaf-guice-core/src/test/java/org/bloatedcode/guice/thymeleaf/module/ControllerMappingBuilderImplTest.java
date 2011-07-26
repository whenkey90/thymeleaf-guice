package org.bloatedcode.guice.thymeleaf.module;

import java.lang.reflect.Method;

import org.bloatedcode.guice.thymeleaf.module.ControllerModuleBuilder.ControllerMappingBuilderImpl;
import org.bloatedcode.guice.thymeleaf.module.controller.TestController;
import org.bloatedcode.guice.thymeleaf.module.exception.ThymeleafInitializationException;
import org.bloatedcode.guice.thymeleaf.servlet.ThymeleafServlet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class ControllerMappingBuilderImplTest {
	
	private Class<?> controllerClass = TestController.class;
	

	
	private ControllerMappingBuilderImpl builder;
	private ControllerModuleBuilder moduleBuilder = new ControllerModuleBuilder(new ThymeleafServlet());
	
	@Before
	public void setup() throws Exception{
		builder = (ControllerMappingBuilderImpl) moduleBuilder.register(controllerClass);
	}
	
	
	@Test
	public void testExceptUniqueExistingMethod() throws Exception{

		builder.except("singleMethod");
		
		Method singleMethod = controllerClass.getMethod("singleMethod");
		
		assertThat(moduleBuilder.getExceptions(),  hasItem(singleMethod) );
		assertThat(moduleBuilder.getExceptions(),  hasSize(1));
		
	}
	
	@Test(expected=ThymeleafInitializationException.class)
	public void testExceptNonUniqueExistingMethod() throws Exception {
		builder.except("overloadedMethod");
	}
	
	@Test(expected=ThymeleafInitializationException.class)
	public void testExceptUnknownMethod() throws Exception {
		builder.except("unknownMethod");
	}
	
	@Test
	public void testExceptNonUniqueExistingMethodWithParams() throws Exception {
		builder.except("overloadedMethod", new Class<?>[]{});
		
		Method overloadedMethod = controllerClass.getMethod("overloadedMethod", new Class<?>[]{});
		
		assertThat(moduleBuilder.getExceptions(),  hasItem(overloadedMethod) );
		assertThat(moduleBuilder.getExceptions(),  hasSize(1));
	}
	
	
	@Test(expected=ThymeleafInitializationException.class)
	public void testExceptAddTwoWithSameSignature() throws Exception {
		builder.except(new String[]{"overloadedMethod","overloadedMethod"}, new Class<?>[][]{{},{}});
	}
	
	@Test 
	public void testExceptWithArrays() throws Exception{
		builder.except(new String[]{"overloadedMethod","singleMethod","overloadedMethod",},new Class<?>[][]{{},{},{String.class}});
		
		Method singleMethod = controllerClass.getMethod("singleMethod");
		Method overloadedMethodOne = controllerClass.getMethod("overloadedMethod");
		Method overloadedMethodTwo = controllerClass.getMethod("overloadedMethod", new Class<?>[]{});
		
		assertThat(moduleBuilder.getExceptions(), hasItems(singleMethod, overloadedMethodOne, overloadedMethodTwo));
		assertThat(moduleBuilder.getExceptions(), hasSize(3));
	}
	
	@Test(expected=ThymeleafInitializationException.class)
	public void testExceptWithArraysTwoMethodsSameSignature() throws Exception {
		builder.except(new String[]{"overloadedMethod","singleMethod","overloadedMethod",},new Class<?>[][]{{},{},{}});
	}
	
	@SuppressWarnings({"rawtypes" })
	@Test
	public void textMethodUniqueExistingMethod()throws Exception{
		builder.method("singleMethod");
		
		Method singleMethod = controllerClass.getMethod("singleMethod");
		
		assertThat(builder.getMethod(),  is(singleMethod));
		assertThat((Class) builder.getControllerClass(), equalTo((Class) controllerClass));
	}
	
	@Test(expected=ThymeleafInitializationException.class)
	public void testModuleNonUniqueExistingMethod() throws Exception {
		builder.method("overloadedMethod");
	}
	
	@Test(expected=ThymeleafInitializationException.class)
	public void testModuleUnknownMethod() throws Exception {
		builder.method("unknownMethod");
	}
	
	@Test(expected=ThymeleafInitializationException.class)
	public void testModuleUnknownMethodWithParameters() throws Exception {
		builder.method("unknownMethod", String.class );
	}
	
	@Test
	public void testModuleNonUniqueExistingMethodWithParams() throws Exception {
		builder.method("overloadedMethod", new Class<?>[]{});
		
		Method overloadedMethod = controllerClass.getMethod("overloadedMethod", new Class<?>[]{});
		
		assertThat(builder.getMethod(),  is(overloadedMethod));
	}
	
	

}
