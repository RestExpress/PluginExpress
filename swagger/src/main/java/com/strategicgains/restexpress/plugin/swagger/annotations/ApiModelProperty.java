package com.strategicgains.restexpress.plugin.swagger.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelProperty {
    String defaultValue() default "";
    String allowableValues() default "";
    String access() default "";
    String notes() default "";
    String dataType() default "";
    String format() default "";
    boolean required() default false;
    int position() default 0;
    boolean hidden() default false;
    String[] excludeFromModels() default {};
}