package ca.loobo.rats.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.loobo.rats.filter.CaseFilter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Filter {
	Class<? extends CaseFilter> value();
	String before() default "";
	String after() default "";
}
