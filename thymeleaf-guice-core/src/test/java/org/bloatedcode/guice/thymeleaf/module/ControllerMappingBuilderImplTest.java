package org.bloatedcode.guice.thymeleaf.module;

import java.lang.reflect.Method;
import java.util.Collection;

import org.bloatedcode.guice.thymeleaf.RequestBinding;
import org.bloatedcode.guice.thymeleaf.module.ControllerModuleBuilder.ControllerMappingBuilderImpl;
import org.bloatedcode.guice.thymeleaf.module.controller.TestController;
import org.bloatedcode.guice.thymeleaf.module.exception.ThymeleafInitializationException;
import org.bloatedcode.guice.thymeleaf.servlet.ThymeleafServlet;
import static org.bloatedcode.guice.thymeleaf.hamcrest.SameRequestBinding.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class ControllerMappingBuilderImplTest {

	private Class<?> controller = TestController.class;

	private ControllerMappingBuilderImpl builder;
	private ControllerModuleBuilder moduleBuilder = new ControllerModuleBuilder(new ThymeleafServlet());

	@Before
	public void setup() throws Exception {
		builder = (ControllerMappingBuilderImpl) moduleBuilder.register(controller);
	}

	@Test
	public void testExceptUniqueExistingMethod() throws Exception {

		builder.except("singleMethod");

		Method singleMethod = controller.getMethod("singleMethod");

		assertThat(moduleBuilder.getExceptions(), hasItem(singleMethod));
		assertThat(moduleBuilder.getExceptions(), hasSize(1));

	}

	@Test(expected = ThymeleafInitializationException.class)
	public void testExceptNonUniqueExistingMethod() throws Exception {
		builder.except("overloadedMethod");
	}

	@Test(expected = ThymeleafInitializationException.class)
	public void testExceptUnknownMethod() throws Exception {
		builder.except("unknownMethod");
	}

	@Test
	public void testExceptNonUniqueExistingMethodWithParams() throws Exception {
		builder.except("overloadedMethod", new Class<?>[] {});

		Method overloadedMethod = controller.getMethod("overloadedMethod", new Class<?>[] {});

		assertThat(moduleBuilder.getExceptions(), hasItem(overloadedMethod));
		assertThat(moduleBuilder.getExceptions(), hasSize(1));
	}

	@Test(expected = ThymeleafInitializationException.class)
	public void testExceptAddTwoWithSameSignature() throws Exception {
		builder.except(new String[] { "overloadedMethod", "overloadedMethod" }, new Class<?>[][] { {}, {} });
	}

	@Test
	public void testExceptWithArrays() throws Exception {
		builder.except(new String[] { "overloadedMethod", "singleMethod", "overloadedMethod", }, new Class<?>[][] { {}, {}, { String.class } });

		Method singleMethod = controller.getMethod("singleMethod");
		Method overloadedMethodOne = controller.getMethod("overloadedMethod");
		Method overloadedMethodTwo = controller.getMethod("overloadedMethod", new Class<?>[] {});

		assertThat(moduleBuilder.getExceptions(), hasItems(singleMethod, overloadedMethodOne, overloadedMethodTwo));
		assertThat(moduleBuilder.getExceptions(), hasSize(3));
	}

	@Test(expected = ThymeleafInitializationException.class)
	public void testExceptWithArraysTwoMethodsSameSignature() throws Exception {
		builder.except(new String[] { "overloadedMethod", "singleMethod", "overloadedMethod", }, new Class<?>[][] { {}, {}, {} });
	}

	@SuppressWarnings({ "rawtypes" })
	@Test
	public void textMethodUniqueExistingMethod() throws Exception {
		builder.method("singleMethod");

		Method singleMethod = controller.getMethod("singleMethod");

		assertThat(builder.getMethod(), is(singleMethod));
		assertThat((Class) builder.getControllerClass(), equalTo((Class) controller));
	}

	@Test(expected = ThymeleafInitializationException.class)
	public void testModuleNonUniqueExistingMethod() throws Exception {
		builder.method("overloadedMethod");
	}

	@Test(expected = ThymeleafInitializationException.class)
	public void testModuleUnknownMethod() throws Exception {
		builder.method("unknownMethod");
	}

	@Test(expected = ThymeleafInitializationException.class)
	public void testModuleUnknownMethodWithParameters() throws Exception {
		builder.method("unknownMethod", String.class);
	}

	@Test
	public void testModuleNonUniqueExistingMethodWithParams() throws Exception {
		builder.method("overloadedMethod", new Class<?>[] {});

		Method overloadedMethod = controller.getMethod("overloadedMethod", new Class<?>[] {});

		assertThat(builder.getMethod(), is(overloadedMethod));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testByAnnotation() throws Exception {
		builder.byAnnotation();

		Method singleMethod = controller.getMethod("singleMethod", new Class<?>[] {});
		RequestBinding singleMethodBinding = new RequestBinding(controller, singleMethod, "template", "page_one.html");

		Method overloadedMethod = controller.getMethod("overloadedMethod", new Class<?>[] {});
		RequestBinding overloadedMethodBinding = new RequestBinding(controller, overloadedMethod, "template", "page_two.html");

		Method overloadedMethodWithParam = controller.getMethod("overloadedMethod", new Class<?>[] { String.class });
		RequestBinding overloadedMethodWithParamBinding = new RequestBinding(controller, overloadedMethodWithParam, "template", "page_three.html");

		assertThat(moduleBuilder.getRequestBindings(),
				containsInAnyOrder(same(overloadedMethodBinding), same(overloadedMethodWithParamBinding), same(singleMethodBinding)));
		assertThat((Collection) moduleBuilder.getExceptions(),  empty());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testByAnnotationWithException() throws Exception {
		builder.byAnnotation().except("singleMethod");

		Method singleMethod = controller.getMethod("singleMethod", new Class<?>[] {});
		RequestBinding singleMethodBinding = new RequestBinding(controller, singleMethod, "template", "page_one.html");

		Method overloadedMethod = controller.getMethod("overloadedMethod", new Class<?>[] {});
		RequestBinding overloadedMethodBinding = new RequestBinding(controller, overloadedMethod, "template", "page_two.html");

		Method overloadedMethodWithParam = controller.getMethod("overloadedMethod", new Class<?>[] { String.class });
		RequestBinding overloadedMethodWithParamBinding = new RequestBinding(controller, overloadedMethodWithParam, "template", "page_three.html");

		assertThat(moduleBuilder.getRequestBindings(),containsInAnyOrder(
				same(overloadedMethodBinding), same(overloadedMethodWithParamBinding), same(singleMethodBinding)));
		
		assertThat(moduleBuilder.getExceptions(),  contains(equalTo(singleMethod)));
	}

}
