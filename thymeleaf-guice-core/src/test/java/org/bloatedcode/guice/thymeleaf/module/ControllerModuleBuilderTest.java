package org.bloatedcode.guice.thymeleaf.module;

import org.bloatedcode.guice.thymeleaf.RequestBinding;
import org.bloatedcode.guice.thymeleaf.module.controller.ReflectionObjectMother;
import org.bloatedcode.guice.thymeleaf.module.controller.TestController;
import org.bloatedcode.guice.thymeleaf.servlet.ThymeleafServlet;
import org.easymock.EasyMock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.easymock.EasyMock.*;

@RunWith(JUnit4.class)
public class ControllerModuleBuilderTest {
	
	private ThymeleafServlet servlet = createMock(ThymeleafServlet.class);
	
	private ReflectionObjectMother objectMother = new ReflectionObjectMother();
	
	private Class<?> controllerClass = TestController.class;
	
	private ControllerModuleBuilder moduleBuilder = new ControllerModuleBuilder(servlet);
	
	@Before
	public void setup(){
		reset(servlet);
	}
	
	@Test
	public void testNoBindings(){

		replay(servlet);
		
		moduleBuilder.configure();
		
		assertThat(moduleBuilder.getExceptions(), hasSize(0));
		assertThat(moduleBuilder.getRequestBindings(), hasSize(0));
		verify(servlet);
		
	}
	
	@Test
	public void testOneBinding() throws Exception{

		RequestBinding expected = objectMother.newSimpleMethodBinding("index", "/index.html");
		
		servlet.register(EasyMock.isA(RequestBinding.class));
		
		replay(servlet);
		
		moduleBuilder.register(controllerClass).method("singleMethod").using("index").to("/index.html");
		
		moduleBuilder.configure();
		
		assertThat(moduleBuilder.getExceptions(), hasSize(0));
		assertThat(moduleBuilder.getRequestBindings(), hasSize(1));
		assertThat(moduleBuilder.getRequestBindings(), contains(samePropertyValuesAs(expected)));
		
		verify(servlet);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testTwoBindings() throws Exception {
		RequestBinding firstExpected =  objectMother.newOverridedMethodNoArgumentBinding("index", "/index.html");
		RequestBinding secondExpected = objectMother.newOverridedMethodOneArgumentBinding("other", "/other.html");
		
		servlet.register(EasyMock.isA(RequestBinding.class));
		servlet.register(EasyMock.isA(RequestBinding.class));
		
		replay(servlet);
		
		moduleBuilder.register(controllerClass).method("overloadedMethod",new Class<?>[]{}).using("index").to("/index.html");
		moduleBuilder.register(controllerClass).method("overloadedMethod", String.class ).using("other").to("/other.html");
		
		moduleBuilder.configure();
		
		assertThat(moduleBuilder.getExceptions(), hasSize(0));
		assertThat(moduleBuilder.getRequestBindings(), hasSize(2));
		assertThat(moduleBuilder.getRequestBindings(), contains(samePropertyValuesAs(firstExpected), samePropertyValuesAs(secondExpected)));

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testTwoBindingsOneException() throws Exception {
		RequestBinding firstExpected =  objectMother.newOverridedMethodNoArgumentBinding("index", "/index.html");
		RequestBinding secondExpected = objectMother.newOverridedMethodOneArgumentBinding("other", "/other.html");
		
		servlet.register(EasyMock.isA(RequestBinding.class));
		
		replay(servlet);
		
		moduleBuilder.register(controllerClass).method("overloadedMethod",new Class<?>[]{}).using("index").to("/index.html");
		moduleBuilder.register(controllerClass).method("overloadedMethod", String.class ).using("other").to("/other.html");
		moduleBuilder.register(controllerClass).except("overloadedMethod", String.class);
		
		moduleBuilder.configure();
		
		assertThat(moduleBuilder.getExceptions(), hasSize(1));
		assertThat(moduleBuilder.getExceptions(), contains(objectMother.newOverridedMethodOneArgument()));
		assertThat(moduleBuilder.getRequestBindings(), hasSize(2));
		assertThat(moduleBuilder.getRequestBindings(), contains(samePropertyValuesAs(firstExpected), samePropertyValuesAs(secondExpected)));

	}
	
}
