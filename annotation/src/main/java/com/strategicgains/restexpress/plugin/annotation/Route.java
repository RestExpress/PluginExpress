package com.strategicgains.restexpress.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {

    public String uri() default "";
    
    public String name() default "";
    
    public String httpMethod() default "GET";
}
