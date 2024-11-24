package io.connectevent.connectevent.auth.password.annotation;

public @interface TargetMapping {

	Class<?> clazz();

	String[] fields() default {};
}
