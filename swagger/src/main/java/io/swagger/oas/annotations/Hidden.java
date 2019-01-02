package io.swagger.oas.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(value={ElementType.METHOD,ElementType.TYPE,ElementType.FIELD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Hidden {
	
}
