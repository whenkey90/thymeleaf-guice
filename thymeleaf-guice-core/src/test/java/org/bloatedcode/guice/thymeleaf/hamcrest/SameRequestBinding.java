package org.bloatedcode.guice.thymeleaf.hamcrest;

import java.lang.reflect.Method;

import org.bloatedcode.guice.thymeleaf.RequestBinding;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsEqual;

public class SameRequestBinding extends TypeSafeMatcher<RequestBinding> {

	
	private Matcher<?> controllerClass;
	private Matcher<Method> method;
	private Matcher<String> template;
	private Matcher<String> url;

	public SameRequestBinding(RequestBinding expected) {
		this.controllerClass = IsEqual.equalTo(expected.getControllerClass()); 
		this.method = IsEqual.equalTo(expected.getMethod());
		this.template = IsEqual.equalTo(expected.getTemplate());
		this.url = IsEqual.equalTo(expected.getUrl());
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendText("RequestBinding:{")
			.appendText("controller:").appendDescriptionOf(controllerClass).appendText(",")
			.appendText("method:").appendDescriptionOf(method).appendText(",")
			.appendText("template:").appendDescriptionOf(template).appendText(",")
			.appendText("url:").appendDescriptionOf(url).appendText("}");
	}

	
	@Override
	protected void describeMismatchSafely(RequestBinding item, Description mismatchDescription) {
		mismatchDescription.appendText(" was not the same as ").appendValue(item);
		super.describeMismatchSafely(item, mismatchDescription);
	}
	
	@Override
	protected boolean matchesSafely(RequestBinding actual) {
		return controllerClass.matches(actual.getControllerClass()) && method.matches(actual.getMethod())
				&& template.matches(actual.getTemplate()) && url.matches(actual.getUrl());
	}
	
	@Factory
	public static SameRequestBinding same(RequestBinding expected) {
		return new SameRequestBinding(expected);
	}

}
